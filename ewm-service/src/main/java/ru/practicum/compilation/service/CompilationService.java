package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationAddDto;
import ru.practicum.compilation.dto.CompilationLogDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {
    CompilationLogDto save(CompilationAddDto compilationAddDto);

    void delete(Long compId);

    CompilationLogDto update(CompilationUpdateDto compilationUpdateDto, Long compId);

    List<CompilationLogDto> getAll(Boolean pinned, int from, int size);

    CompilationLogDto getById(Long compId);

}
