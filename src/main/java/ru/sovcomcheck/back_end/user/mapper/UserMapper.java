package ru.sovcomcheck.back_end.user.mapper;

import org.springframework.stereotype.Service;
import ru.sovcomcheck.back_end.user.dto.UserDto;
import ru.sovcomcheck.back_end.user.entity.User;

@Service
public class UserMapper {

    public UserDto entityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .avatar(user.getAvatar())
                .build();
    }
}
