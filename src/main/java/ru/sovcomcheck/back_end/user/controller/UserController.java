package ru.sovcomcheck.back_end.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sovcomcheck.back_end.photoservice.enums.BucketEnum;
import ru.sovcomcheck.back_end.user.dto.UserDto;
import ru.sovcomcheck.back_end.user.dto.UserRegisterDto;
import ru.sovcomcheck.back_end.user.service.UserService;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestPart("file") MultipartFile file, BucketEnum bucket, @PathVariable String id) throws Exception{
        return ResponseEntity.ok().body(userService.uploadAvatar(file, bucket.getBucketName(), id));
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<ByteArrayResource> showAvatar(@PathVariable("id") String id, BucketEnum bucket) {
        return downloadAvatar(id, bucket.getBucketName(), "image/jpeg", "inline");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, UserDto userDto) {
        userService.updateUserById(id, userDto);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<ByteArrayResource> downloadAvatar(String file, String bucket, String contentType, String contentDisposition) {
        byte[] data = userService.downloadAvatar(file, bucket);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", contentType)
                .header("Content-disposition", contentDisposition + "; filename=\"" + file + "\"")
                .body(resource);
    }
}
