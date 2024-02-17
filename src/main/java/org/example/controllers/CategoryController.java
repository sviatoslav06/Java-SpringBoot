package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.dto.CategoryCreateDTO;
import org.example.entities.CategoryEntity;
import org.example.repositories.CategoryRepository;
import org.example.storage.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<CategoryEntity>> index() {
        List<CategoryEntity> list = categoryRepository.findAll();
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
            category.setDateCreated(new Date());

            categoryRepository.save(category);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
