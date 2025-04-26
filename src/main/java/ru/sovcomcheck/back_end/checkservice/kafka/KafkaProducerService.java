package ru.sovcomcheck.back_end.checkservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sovcomcheck.back_end.checkservice.documents.CheckDocument;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, CheckDocument> kafkaTemplate;

    public void sendCheck(CheckDocument check) {
        kafkaTemplate.send("check-topic", check);
    }

}