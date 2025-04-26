package ru.sovcomcheck.back_end.notification.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {

    @MessageMapping("/createEntity")
    @SendTo("/topic/entityCreated")
    public String handleCreateEntity(String message) {
        // Логика обработки сообщения (например, сохранение в базу данных)
        System.out.println("Получено сообщение через WebSocket: " + message);

        // Возвращаем ответ всем подписчикам топика /topic/entityCreated
        return "Сущность создана: " + message;
    }
}
