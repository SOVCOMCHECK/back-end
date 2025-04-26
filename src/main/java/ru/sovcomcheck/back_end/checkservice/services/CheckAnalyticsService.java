package ru.sovcomcheck.back_end.checkservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sovcomcheck.back_end.checkservice.documents.CheckDocument;
import ru.sovcomcheck.back_end.checkservice.mappers.CheckMapper;
import ru.sovcomcheck.back_end.checkservice.repositories.CheckRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckAnalyticsService {

    private final CheckRepository checkRepository;
    private final CheckMapper checkMapper;

    /**
     * Получение сводной аналитики расходов
     *
     * @param userId ID пользователя
     * @param from   Начальная дата периода
     * @param to     Конечная дата периода
     * @return Объект SpendingAnalysis с агрегированными данными
     */
    public Map<String, Double> getTotalSumByCategory(String userId, LocalDateTime from, LocalDateTime to) {

        Map<String, List<CheckDocument>> checksGroupByCategory = checkRepository.findAllByUserId(userId).stream()
                .collect(Collectors.groupingBy(CheckDocument::getCategory));

        Map<String, Double> result = new HashMap<>();

        for (String category : checksGroupByCategory.keySet()) {

            double categorySum = checksGroupByCategory.get(category).stream()
                    .filter(x -> x.getCategory() != null)
                    .filter(x -> x.getProcessedAt().isAfter(from) && x.getProcessedAt().isBefore(to))
                    .map(checkMapper::toAnalyticsDTO)
                    .mapToDouble(check -> (double) check.getTotalSum() / 100)
                    .sum();

            result.put(category, categorySum);

        }

        return result;
    }
}
