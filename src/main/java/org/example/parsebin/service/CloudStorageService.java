//package org.example.parsebin.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import io.lettuce.core.RedisClient;
//import io.lettuce.core.api.StatefulRedisConnection;
//import io.lettuce.core.api.async.RedisAsyncCommands;
//import lombok.RequiredArgsConstructor;
//import org.example.parsebin.model.Bin;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CloudStorageService {
//
//    private final Storage storage = StorageOptions.getDefaultInstance().getService();
//    private final ObjectMapper mapper;
//    private static final String METADATA_KEY = "metadataHash:%s";
//
//    public String uploadFile(String bucketName, String objectName, String content) {
//        objectName += ".txt";
//        Blob blob = storage.create(
//                Blob.newBuilder(bucketName, objectName).build(),
//                content.getBytes()
//        );
//        System.out.println("File uploaded to bucket " + bucketName + " as " + objectName);
//        return objectName;
//    }
//
//    public Bin downloadCachedFile(String bucketName, String objectName) {
//        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
//            RedisAsyncCommands<String, String> asyncCommands = connection.async();
//            String key = String.format(METADATA_KEY, objectName);
//            String raw = asyncCommands.get(objectName).get();
//
//            if (raw != null) {
//                return mapper.readValue(raw, Bin.class);
//            }
//
//            String string = downloadFile(bucketName, objectName);
//            asyncCommands.set(bucketName, string);
//
//            Bin bin = mapper.readValue(string, Bin.class);
//            return bin;
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String downloadFile(String bucketName, String objectName) {
//        Blob blob = storage.get(bucketName, objectName);
//        return new String(blob.getContent());
//    }
//}
