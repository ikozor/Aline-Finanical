package com.aline.core.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", value = "sms.enable", havingValue = "true")
@RequiredArgsConstructor
public class AWSSMSConfig {

    private final AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    @Bean
    public AmazonSNS snsClient() {
        return AmazonSNSClientBuilder
                .standard()
                .withCredentials(awsStaticCredentialsProvider)
                .withRegion(Regions.US_WEST_2)
                .build();
    }

}
