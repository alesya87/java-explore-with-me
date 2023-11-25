package ru.practicum.category.model;

import ru.practicum.category.dto.CategoryAddDto;
import ru.practicum.category.dto.CategoryLogDto;
import ru.practicum.category.dto.CategoryUpdateDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static Category toCategory(CategoryAddDto categoryAddDto) {
        return new Category(null, categoryAddDto.getName());
    }

    public static Category toCategory(CategoryUpdateDto categoryUpdateDto, Long catId) {
        return new Category(catId, categoryUpdateDto.getName());
    }

    public static CategoryLogDto toCategoryLogDto(Category category) {
        return new CategoryLogDto(category.getId(), category.getName());
    }

    public static List<CategoryLogDto> toListCategoryLogDto(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toCategoryLogDto)
                .collect(Collectors.toList());
    }
}