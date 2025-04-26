package ru.sovcomcheck.back_end.checkservice.dtos;

import lombok.Builder;
import lombok.Data;
import ru.sovcomcheck.back_end.checkservice.enums.ProcessingStatus;

@Data
@Builder
public class CheckProcessingResponse {
    private String checkId;
    private Check checkData;
    private ProcessingStatus status;
    private String message;
}
