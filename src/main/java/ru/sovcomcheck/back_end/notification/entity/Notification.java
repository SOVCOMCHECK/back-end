package ru.sovcomcheck.back_end.notification.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("notifications")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Notification {
    @Id
    private String id;
    @Field("userId")
    private String userId;
    @Field("message")
    private String message;
}
