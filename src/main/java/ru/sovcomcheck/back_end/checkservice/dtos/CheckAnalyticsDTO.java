package ru.sovcomcheck.back_end.checkservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sovcomcheck.back_end.checkservice.enums.CheckStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckAnalyticsDTO {
    private String id;
    private String userId;
    private String category;
    private LocalDateTime dateTime;
    private String organization;
    private String address;
    private String inn;
    private Integer operationType;
    private List<Item> items;
    private Long totalSum;
    private Long cashTotalSum;
    private Long ecashTotalSum;
    private Integer taxationType;
    private CheckStatus status;
    private LocalDateTime processedAt;
    private LocalDateTime confirmedAt;
}
