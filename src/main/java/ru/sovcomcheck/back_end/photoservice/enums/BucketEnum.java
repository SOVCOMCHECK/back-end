package ru.sovcomcheck.back_end.photoservice.enums;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sovcomcheck.back_end.photoservice.config.BucketConfiguration;

@Getter
public enum BucketEnum {
    AVATARS,
    CHECKS;

    private String bucketName;

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Component
    public static class BucketEnumInitializer {

        @Autowired
        public BucketEnumInitializer(BucketConfiguration bucketConfiguration) {
            AVATARS.setBucketName(bucketConfiguration.getAvatarsBucket());
            CHECKS.setBucketName(bucketConfiguration.getChecksBucket());
        }
    }
}
