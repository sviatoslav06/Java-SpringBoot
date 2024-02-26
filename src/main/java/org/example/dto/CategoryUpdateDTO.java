package org.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryUpdateDTO {
    private int id;
    private String name;
    private MultipartFile file;
    private String description;
}