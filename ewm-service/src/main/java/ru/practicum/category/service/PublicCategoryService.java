package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryLogDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryLogDto> get(int from, int size);

    CategoryLogDto get(Long catId);
}
