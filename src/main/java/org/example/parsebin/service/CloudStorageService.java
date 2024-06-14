package org.example.parsebin.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final Storage storage;

    @Value("${app.cloud.bucketName}")
    private String BUCKET_NAME;


    public void uploadFile(String objectName, String content) {
        objectName += ".txt";
        Blob blob = storage.create(
                Blob.newBuilder(BUCKET_NAME, objectName).build(),
                content.getBytes()
        );
        System.out.println("File uploaded to bucket " + BUCKET_NAME + " as " + objectName);
    }


    public String downloadFile(String objectName) {
        objectName += ".txt";
        Blob blob = storage.get(BUCKET_NAME, objectName);
        return new String(blob.getContent(), StandardCharsets.UTF_8);
    }
}
