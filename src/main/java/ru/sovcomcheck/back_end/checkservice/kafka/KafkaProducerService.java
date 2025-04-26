package ru.sovcomcheck.back_end.checkservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Check> kafkaTemplate;

    public void sendCheck(Check check) {
        kafkaTemplate.send("check-topic", check);
    }

}