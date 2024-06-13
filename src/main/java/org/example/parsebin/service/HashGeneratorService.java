package org.example.parsebin.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.RequiredArgsConstructor;
import org.example.parsebin.repository.BinRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class HashGeneratorService {

    private final Base64.Encoder base64Encoder = Base64.getEncoder();
    private final BinRepository binRepository;
    private final RedisClient redisClient;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Value("${app.hashGenerateSize}")
    private int size;


    public void generateHash() {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisAsyncCommands<String, String> asyncCommands = connection.async();

            Long startId = binRepository.getNextId();
            for (int i = 1; i < size; i++) {
                Long id = startId + i;
                String key = "binHash:%d".formatted(id);
                String hash = getHash(id);
                asyncCommands.set(key, hash);
            }
            executor.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getHash(Long id) {
        return base64Encoder.encodeToString(String.valueOf(id).getBytes());
    }

    public String getCachedHash(Long id) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisAsyncCommands<String, String> asyncCommands = connection.async();
            String key = "binHash:%d".formatted(id);
            String raw = asyncCommands.get(key).get();

            if (raw != null) {
                asyncCommands.del(key);
                return raw;
            }
            generateHash();
            System.out.println("Hash generated");
            return getHash(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
