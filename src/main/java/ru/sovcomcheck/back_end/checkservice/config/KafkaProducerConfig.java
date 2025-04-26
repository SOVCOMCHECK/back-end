package ru.sovcomcheck.back_end.checkservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.sovcomcheck.back_end.checkservice.documents.CheckDocument;
import ru.sovcomcheck.back_end.checkservice.dtos.Check;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.topic.checkTopic}")
    private String checkTopic;

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(JsonSerializer.TYPE_MAPPINGS, "check:ru.sovcomcheck.back_end.checkservice.documents.CheckDocument");
        return props;
    }

    @Bean
    public ProducerFactory<String, CheckDocument> checkProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, CheckDocument> checkKafkaTemplate() {
        return new KafkaTemplate<>(checkProducerFactory());
    }

    @Bean
    public NewTopic checkTopic() {
        return TopicBuilder.name(checkTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
