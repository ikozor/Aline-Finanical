package com.aline.core.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemailv2.AmazonSimpleEmailServiceV2;
import com.amazonaws.services.simpleemailv2.AmazonSimpleEmailServiceV2ClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(prefix = "app", name = "email.enable", havingValue = "true")
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.aws.email")
public class AWSEmailConfig {

    /**
     * The S3 Bucket that holds the email templates.
     */
    private String templateBucketName;

    private final AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    @Bean
    @SessionScope
    public AmazonSimpleEmailServiceV2 clientBuilder() {
        return AmazonSimpleEmailServiceV2ClientBuilder.standard()
                .withCredentials(awsStaticCredentialsProvider)
                .withRegion(Regions.US_EAST_2)
                .build();
    }

}
