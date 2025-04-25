package ru.sovcomcheck.back_end.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sovcomcheck.back_end.notification.dto.NotificationDto;
import ru.sovcomcheck.back_end.notification.entity.Notification;
import ru.sovcomcheck.back_end.notification.mapper.NotificationMapper;
import ru.sovcomcheck.back_end.notification.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public void createNotification(NotificationDto notificationDto) {
        String id = UUID.randomUUID().toString();
        Notification notification = notificationMapper.dtoToEntity(notificationDto);
        notification.setId(id);
        notificationRepository.save(notification);
    }

    public List<NotificationDto> getNotificationDtoList(String userId) {
        List<Notification> notificationList = getNotificationList(userId);
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        for (Notification notification : notificationList) {
            notificationDtoList.add(notificationMapper.entityToDto(notification));
            deleteById(notification.getId());
        }
        return notificationDtoList;
    }

    public List<Notification> getNotificationList(String userId) {
        return notificationRepository.findAllByUserId(userId);
    }

    public void deleteById(String id) {
        notificationRepository.deleteById(id);
    }

    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }
}
