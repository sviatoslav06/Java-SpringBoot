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

    @PostMapping(value = "update/{category_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryEntity> update(int category_id, @ModelAttribute CategoryUpdateDTO dto){
        try {
            Optional<CategoryEntity> optionalCategory = categoryRepository.findById(category_id);
            if (optionalCategory.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            CategoryEntity category = optionalCategory.get();
            category.setName(dto.getName());
            if (dto.getImage() != null) {
                String image = storageService.SaveImage(dto.getImage(), FileSaveFormat.WEBP);
                category.setImage(image);
            }
            category.setDescription(dto.getDescription());
            category.setCreationTime(new Date());

            categoryRepository.save(category);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{category_id}")
    public ResponseEntity<CategoryEntity> delete(int category_id){
        try {
            Optional<CategoryEntity> optionalCategory = categoryRepository.findById(category_id);
            if (optionalCategory.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            CategoryEntity category = optionalCategory.get();

            categoryRepository.delete(category);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
