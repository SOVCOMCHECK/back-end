package ru.sovcomcheck.back_end.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Разрешить все маршруты
//                .allowedOrigins("*") // Разрешить запросы с любого источника
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешенные методы
//                .allowedHeaders("*") // Разрешить все заголовки
//                .allowCredentials(false);
    }
}
