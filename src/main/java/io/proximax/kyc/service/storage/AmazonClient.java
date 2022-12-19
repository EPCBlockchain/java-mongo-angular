package io.proximax.kyc.service.storage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.mediastoredata.model.PutObjectRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import io.proximax.kyc.config.ApplicationProperties;

@Service
public class AmazonClient {

    private final AmazonS3 s3client;
    private final String bucketName;

    public AmazonClient(ApplicationProperties appConfig) {
        AWSCredentials credentials = new BasicAWSCredentials(appConfig.getAmazonProperties().getAccessKey(),
                appConfig.getAmazonProperties().getSecretKey());

        s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_1).build();
        bucketName = appConfig.getAmazonProperties().getBucketName();

    }

    // uploading object
    public PutObjectResult upload(String key, String base64String) {
        return s3client.putObject(bucketName, key, base64String);
    }

    public String get(String key) {
        InputStream inputStream = s3client.getObject(bucketName, key).getObjectContent();
        StringWriter writer = new StringWriter();
        String encoding = StandardCharsets.UTF_8.name();
        try {
            IOUtils.copy(inputStream, writer, encoding);
        } catch (IOException ex) {
            return "";
        }
        return writer.toString();
    }
}
