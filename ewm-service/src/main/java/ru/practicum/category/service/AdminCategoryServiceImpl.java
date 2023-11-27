package ru.practicum.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryAddDto;
import ru.practicum.category.dto.CategoryLogDto;
import ru.practicum.category.dto.CategoryUpdateDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.excception.EntityNotFoundException;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;

    public AdminCategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryLogDto save(CategoryAddDto categoryAddDto) {
        return CategoryMapper.toCategoryLogDto(categoryRepository.save(CategoryMapper.toCategory(categoryAddDto)));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryLogDto update(CategoryUpdateDto categoryUpdateDto, Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Категория с id " + catId + " не найдена"));
        Category categoryToUpdate = CategoryMapper.toCategory(categoryUpdateDto, catId);
        return CategoryMapper.toCategoryLogDto(categoryRepository.save(categoryToUpdate));
    }
}