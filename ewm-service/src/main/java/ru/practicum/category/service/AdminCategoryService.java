package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryAddDto;
import ru.practicum.category.dto.CategoryLogDto;
import ru.practicum.category.dto.CategoryUpdateDto;

public interface AdminCategoryService {
    CategoryLogDto save(CategoryAddDto categoryAddDto);

    void delete(Long catId);

    CategoryLogDto update(CategoryUpdateDto categoryUpdateDto, Long catId);
}
