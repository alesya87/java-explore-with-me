package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryAddDto;
import ru.practicum.category.dto.CategoryLogDto;
import ru.practicum.category.dto.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {
    CategoryLogDto save(CategoryAddDto categoryAddDto);

    void delete(Long catId);

    CategoryLogDto update(CategoryUpdateDto categoryUpdateDto, Long catId);


    List<CategoryLogDto> get(int from, int size);

    CategoryLogDto get(Long catId);
}
