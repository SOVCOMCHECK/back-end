package ru.sovcomcheck.back_end.notification.controller;

import lombok.AllArgsConstructor;
import org.simpleframework.xml.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sovcomcheck.back_end.notification.dto.NotificationDto;
import ru.sovcomcheck.back_end.notification.entity.Notification;
import ru.sovcomcheck.back_end.notification.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody NotificationDto notificationDto) {
        notificationService.createNotification(notificationDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotificationListByUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(notificationService.getNotificationDtoList(userId));
    }

    @GetMapping
    public ResponseEntity<?> getNotificationListByUser() {
        return ResponseEntity.ok().body(notificationService.getAllNotification());
    }

    @DeleteMapping("/{id}")
    public Notification delete(@PathVariable String id) {
        return notificationService.delete(id);
    }
}
