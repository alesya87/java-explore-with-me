package ru.practicum.compilation.model;

import ru.practicum.compilation.dto.CompilationAddDto;
import ru.practicum.compilation.dto.CompilationLogDto;
import ru.practicum.event.model.Event;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(CompilationAddDto compilationAddDto, Set<Event> events) {
        return new Compilation(
                null,
                events,
                compilationAddDto.getPinned() != null ? compilationAddDto.getPinned() : false,
                compilationAddDto.getTitle()
        );
    }

    public static CompilationLogDto toCompilationLogDto(Compilation compilation) {
        return new CompilationLogDto(
                compilation.getId(),
                compilation.getEvents(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public static List<CompilationLogDto> toListCompilationLogDto(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationLogDto)
                .collect(Collectors.toList());
    }
}