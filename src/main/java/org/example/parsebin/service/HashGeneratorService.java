package org.example.parsebin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class HashGeneratorService {

    private final Base64.Encoder base64Encoder = Base64.getEncoder();
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExecutorService executorService;


    @Value(value = "${app.hashGenerateSize}")
    private int size;

    public HashGeneratorService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void generateHash() {
        for (int i = 0; i < size; i++) {
            executorService.submit(() -> {
                String hash = base64Encoder.encodeToString(String.valueOf(size).getBytes());
                redisTemplate.opsForList().leftPush("hashQueue", hash);
            });
        }
    }

    public String getNextHash() {
        Future<String> futureHash = executorService.submit(() -> (String) redisTemplate.opsForList().rightPop("hashQueue"));
        try {
            return futureHash.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
