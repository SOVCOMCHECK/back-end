package ru.sovcomcheck.back_end.checkservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sovcomcheck.back_end.checkservice.services.CheckAnalyticsService;

import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class CheckAnalyticsController {

    private final CheckAnalyticsService checkAnalyticsService;

    @GetMapping("/summary")
    public Map<String, Double> getAnalyticsByUserId(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return checkAnalyticsService.getTotalSumByCategory(userId, from, to);
    }

}
