package org.example.mapper;

import org.example.dto.CategoryItemDTO;
import org.example.dto.CategoryUpdateDTO;
import org.example.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "creationTime", target = "dateCreated", dateFormat = "dd.MM.yyyy HH:mm:ss")
    CategoryItemDTO categoryItemDTO(CategoryEntity category);
    List<CategoryItemDTO> categoryListItemDTO(List<CategoryEntity> list);

    @Mapping(target = "image", ignore = true)
    CategoryEntity categoryEditDto(CategoryUpdateDTO dto);
}
