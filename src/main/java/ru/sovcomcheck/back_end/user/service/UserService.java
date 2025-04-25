package ru.sovcomcheck.back_end.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.photoservice.dtos.FileDTO;
import ru.sovcomcheck.back_end.photoservice.services.FileService;
import ru.sovcomcheck.back_end.user.dto.UserDto;
import ru.sovcomcheck.back_end.user.dto.UserRegisterDto;
import ru.sovcomcheck.back_end.user.entity.User;
import ru.sovcomcheck.back_end.user.mapper.UserMapper;
import ru.sovcomcheck.back_end.user.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileService fileService;

    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        User user = User.builder()
                .id(userRegisterDto.getId())
                .email(userRegisterDto.getEmail())
                .password(userRegisterDto.getPassword())
                .firstname(userRegisterDto.getFirstname())
                .lastname(userRegisterDto.getLastname())
                .build();
        userRepository.save(user);
        return userMapper.entityToDto(user);
    }

    public UserDto getUserDtoById(String id) {
        Optional<User> user = userRepository.findById(id);
        return userMapper.entityToDto(user.orElseThrow());
    }

    public FileDTO uploadAvatar(MultipartFile file, String bucket, String id) throws Exception{
        User user = getUserById(id);
        FileDTO fileDTO = fileService.uploadFile(file, bucket);
        user.setAvatar(fileDTO.getFilename());
        return fileDTO;
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow();
    }
}
