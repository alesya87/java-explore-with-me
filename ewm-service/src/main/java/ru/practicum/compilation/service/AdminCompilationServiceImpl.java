package ru.practicum.compilation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationAddDto;
import ru.practicum.compilation.dto.CompilationLogDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.excception.EntityNotFoundException;

import java.util.Set;

@Service
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public AdminCompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CompilationLogDto save(CompilationAddDto compilationAddDto) {
        Set<Event> events = getCompilationEvents(compilationAddDto.getEvents());
        return CompilationMapper.toCompilationLogDto(compilationRepository.save(CompilationMapper
                .toCompilation(compilationAddDto, events)));
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationLogDto update(CompilationUpdateDto compilationUpdateDto, Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Подборка с id=" + id + " не найдена")
        );
        Set<Event> events = getCompilationEvents(compilationUpdateDto.getEvents());
        if (compilationUpdateDto.getEvents() != null) {
            compilation.setEvents(events);
        }
        if (compilationUpdateDto.getPinned() != null) {
            compilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        return CompilationMapper.toCompilationLogDto(
                compilationRepository.save(compilation)
        );
    }

    private Set<Event> getCompilationEvents(Set<Long> ids) {
        if (ids == null) {
            return Set.of();
        }
        return eventRepository.findByIdIn(ids);
    }
}