package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.dto.CategoryCreateDTO;
import org.example.dto.CategoryItemDTO;
import org.example.dto.CategoryUpdateDTO;
import org.example.entities.CategoryEntity;
import org.example.mapper.CategoryMapper;
import org.example.repositories.CategoryRepository;
import org.example.storage.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryItemDTO>> index() {
        List<CategoryItemDTO> list = categoryMapper.categoryListItemDTO(categoryRepository.findAll());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryEntity> create(@ModelAttribute CategoryCreateDTO dto) {
        try {
            CategoryEntity category = new CategoryEntity();
            category.setName(dto.getName());
            String image = storageService.SaveImage(dto.getImage(), FileSaveFormat.WEBP);
            category.setImage(image);
            category.setDescription(dto.getDescription());
            category.setCreationTime(new Date());

            categoryRepository.save(category);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryItemDTO> edit(@ModelAttribute CategoryUpdateDTO model) {
        var old = categoryRepository.findById(model.getId()).orElse(null);
        if (old == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var entity = categoryMapper.categoryEditDto(model);
        if(model.getFile()==null) {
            entity.setImage(old.getImage());
        }
        else {
            try {
                storageService.deleteImage(old.getImage());
                String fileName = storageService.SaveImage(model.getFile(), FileSaveFormat.WEBP);
                entity.setImage(fileName);
            }
            catch (Exception exception) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        entity.setCreationTime(old.getCreationTime());
        categoryRepository.save(entity);
        var result = categoryMapper.categoryItemDTO(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable int categoryId) {
        var entity = categoryRepository.findById(categoryId).orElse(null);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            storageService.deleteImage(entity.getImage());
            categoryRepository.deleteById(categoryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryItemDTO> getById(@PathVariable int categoryId) {
        var entity = categoryRepository.findById(categoryId).orElse(null);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var result =  categoryMapper.categoryItemDTO(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CategoryItemDTO>> searchByName(@RequestParam(required = false) String name,
                                                              Pageable pageable) {
        Page<CategoryEntity> categories = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
        Page<CategoryItemDTO> result = categories.map(categoryMapper::categoryItemDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
