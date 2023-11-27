package ru.practicum.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryAddDto;
import ru.practicum.category.dto.CategoryLogDto;
import ru.practicum.category.dto.CategoryUpdateDto;
import ru.practicum.category.service.AdminCategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryLogDto> save(@Valid @RequestBody CategoryAddDto categoryAddDto) {
        log.info("Получен POST-запрос к эндпоинту: '/admin/categories' на добавление категории: {}", categoryAddDto);
        return new ResponseEntity<>(adminCategoryService.save(categoryAddDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/admin/categories' на удаление категории по id={}", catId);
        adminCategoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryLogDto> update(@Valid @RequestBody CategoryUpdateDto categoryUpdateDto, @PathVariable Long catId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/admin/categories' на изменение категории по id={} {}", catId, categoryUpdateDto);
        return new ResponseEntity<>(adminCategoryService.update(categoryUpdateDto, catId), HttpStatus.OK);
    }
}
