package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationAddDto;
import ru.practicum.compilation.dto.CompilationLogDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;

public interface AdminCompilationService {
    CompilationLogDto save(CompilationAddDto compilationAddDto);

    void delete(Long compId);

    CompilationLogDto update(CompilationUpdateDto compilationUpdateDto, Long compId);
}