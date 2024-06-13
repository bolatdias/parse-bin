package org.example.parsebin.service;

import org.example.parsebin.repository.BinRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Base64;

@Service
public class HashGeneratorService {

    private final Base64.Encoder base64Encoder = Base64.getEncoder();
    private final BinRepository binRepository;
    JedisPool jedisPool = new JedisPool("localhost", 6379);


    @Value("${app.hashGenerateSize}")
    private int size;

    public HashGeneratorService(BinRepository binRepository) {
        this.binRepository = binRepository;
    }

    public void generateHash() {
        try (Jedis jedis = jedisPool.getResource()) {
            Long startId = binRepository.getNextId();
            for (int i = 0; i < size; i++) {
                Long id = startId + i;
                String key = "binHash:%d".formatted(id);
                String hash = getHash(id);
                jedis.set(key, hash);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getHash(Long id) {
        return base64Encoder.encodeToString(String.valueOf(id).getBytes());
    }

    public String getCachedHash(Long id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "binHash:%d".formatted(id);
            String raw = jedis.get(key);

            if (raw != null) {
                jedis.del(key);
                return raw;
            }
            generateHash();
            System.out.println("Hash generated");
            return getHash(id);
        }
    }


}
