package ru.sovcomcheck.back_end.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationDto {
    private String userId;
    private String message;
}
