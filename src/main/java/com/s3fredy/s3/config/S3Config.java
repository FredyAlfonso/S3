package com.s3fredy.s3.config;


import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.credentials.session-token}")
    private String token;

    @Bean
    public AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                //new BasicAWSCredentials(accessKey,secretKey)
                                new AWSSessionCredentials() {
                                    @Override
                                    public String getSessionToken() {
                                        return  token;
                                    }

                                    @Override
                                    public String getAWSAccessKeyId() {
                                        return accessKey;
                                    }

                                    @Override
                                    public String getAWSSecretKey() {
                                        return secretKey;
                                    }
                                }
                        )
                )
                .build();
    }
}
