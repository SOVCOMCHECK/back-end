package ru.sovcomcheck.back_end.notification.mapper;

import org.springframework.stereotype.Service;
import ru.sovcomcheck.back_end.notification.dto.NotificationDto;
import ru.sovcomcheck.back_end.notification.entity.Notification;

@Service
public class NotificationMapper {

    public Notification dtoToEntity(NotificationDto notificationDto) {
        return Notification.builder()
                .userId(notificationDto.getUserId())
                .message(notificationDto.getMessage())
                .build();
    }

    public NotificationDto entityToDto(Notification notification) {
        return NotificationDto.builder()
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .build();
    }
}
