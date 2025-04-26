package ru.sovcomcheck.back_end.checkservice.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;
import ru.sovcomcheck.back_end.checkservice.enums.CheckStatus;

import java.time.LocalDateTime;

@Document(collection = "checks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckDocument {
    @Id
    private String id;
    private String minioImageUrl;
    private Check checkData;
    private String category;
    private CheckStatus status;
    private LocalDateTime processedAt;
    private LocalDateTime confirmedAt;
}