package ru.sovcomcheck.back_end.photoservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BucketConfiguration {

    @Value("${minio.buckets.checks}")
    private String checksBucket;

    @Value("${minio.buckets.avatars}")
    private String avatarsBucket;

}
