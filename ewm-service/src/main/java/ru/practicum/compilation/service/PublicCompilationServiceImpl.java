package ru.practicum.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationLogDto;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.excception.EntityNotFoundException;

import java.util.List;

@Service
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public PublicCompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
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
}