package ru.practicum.user.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserAddDto;
import ru.practicum.user.dto.UserLogDto;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserLogDto save(UserAddDto userAddDto) {
        return UserMapper.toUserLogDto(userRepository.save(UserMapper.toUser(userAddDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLogDto> get(Long[] ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null) {
            return UserMapper.toListUserLogDto(userRepository.findAll(pageable).toList());
        } else {
            return UserMapper.toListUserLogDto(userRepository.findByIdIn(ids, pageable));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}