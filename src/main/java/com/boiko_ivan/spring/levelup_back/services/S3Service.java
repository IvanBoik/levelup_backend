package com.boiko_ivan.spring.levelup_back.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.boiko_ivan.spring.levelup_back.exceptions.InvalidS3ObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3Client;

    public static final String BUCKET_NAME = "levelup1";
    public static final long URL_EXPIRATION = 604800000;

    public String uploadAndGetURL(byte[] file, String key) {
        amazonS3Client.putObject(
                new PutObjectRequest(BUCKET_NAME, key, new ByteArrayInputStream(file), new ObjectMetadata())
        );
        return getURL(key);
    }

    public byte[] download(String key) {
        S3Object s3object = amazonS3Client.getObject(BUCKET_NAME, key);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            return inputStream.readAllBytes();
        }
        catch (IOException e) {
            throw new InvalidS3ObjectException(e);
        }
    }

    public String getURL(String key) {
        return amazonS3Client.generatePresignedUrl(
                BUCKET_NAME,
                key,
                new Date(System.currentTimeMillis() + URL_EXPIRATION)
        ).toString();
    }

    public void delete(String key) {
        amazonS3Client.deleteObject(BUCKET_NAME, key);
    }

    public String generateKey() {
        return UUID.randomUUID().toString();
    }
}
