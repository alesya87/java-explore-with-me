package ru.practicum.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import java.util.List;
import java.util.Set;

@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
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

    @Override
    @Transactional(readOnly = true)
    public List<CompilationLogDto> getAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return CompilationMapper.toListCompilationLogDto(compilationRepository.findAllByPinned(pinned, pageable));
        } else {
            return CompilationMapper.toListCompilationLogDto(compilationRepository.findAll(pageable).toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationLogDto getById(Long compId) {
        return CompilationMapper.toCompilationLogDto(compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException("Подборка с id=" + compId + " не найдена")
        ));
    }

    private Set<Event> getCompilationEvents(Set<Long> ids) {
        if (ids == null) {
            return Set.of();
        }
        return eventRepository.findByIdIn(ids);
    }
}