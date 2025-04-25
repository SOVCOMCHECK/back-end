package ru.sovcomcheck.back_end.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.photoservice.enums.BucketEnum;
import ru.sovcomcheck.back_end.user.dto.UserRegisterDto;
import ru.sovcomcheck.back_end.user.service.UserService;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        return ResponseEntity.ok().body(userService.registerUser(userRegisterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserDtoById(id));
    }

    @PostMapping("/{id}/uploadAvatar")
    public ResponseEntity<?> uploadAvatar(@RequestPart("file") MultipartFile file, BucketEnum bucket, @PathVariable String id) throws Exception{
        return ResponseEntity.ok().body(userService.uploadAvatar(file, bucket.getBucketName(), id));
    }
}
