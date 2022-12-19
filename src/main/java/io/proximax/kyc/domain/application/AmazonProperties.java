package io.proximax.kyc.domain.application;

public class AmazonProperties {
    String accessKey;
    String secretKey;
    String bucketName;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return this.bucketName;
    }
}
