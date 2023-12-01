package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationLogDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationLogDto> getAll(Boolean pinned, int from, int size);

    CompilationLogDto getById(Long compId);
}