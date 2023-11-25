package ru.practicum.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserAddDto;
import ru.practicum.user.dto.UserLogDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserLogDto> save(@Valid @RequestBody UserAddDto userAddDto) {
        log.info("Получен POST-запрос к эндпоинту: '/admin/users' на добавление пользователя: {}", userAddDto);
        return new ResponseEntity<>(userService.save(userAddDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserLogDto>> get(@RequestParam(required = false) Long[] ids,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/admin/users' на получение списка пользователей: {}", ids);
        return new ResponseEntity<>(userService.get(ids, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Получен DELETE-запрос к эндпоинту: '/admin/users' на удаление пользователя по id: {}", id);
        userService.delete(id);
    }
}