package org.example.parsebin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.parsebin.model.Bin;
import org.example.parsebin.payload.BinAddRequest;
import org.example.parsebin.repository.BinRepository;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.OffsetDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BinService {
    private final BinRepository binRepository;
    private final HashGeneratorService hashGeneratorService;
    private final TrendingService trendingService;
    private final CloudStorageService cloudService;
    private final JedisPool jedisPool = new JedisPool("localhost", 6379);
    private final ObjectMapper objectMapper;

    private final static String BIN_KEY = "bin:%s";

    public void saveBin(BinAddRequest request) {
        Bin bin = new Bin();
        bin.setDeleteTime(request.getDeleteTime());

        Long id = binRepository.getNextId();
        String hashed = hashGeneratorService.getCachedHash(id);
        String mediaLink = cloudService.uploadFile(hashed, request.getText());
        bin.setUrl(hashed);
        bin.setMediaLink(mediaLink);

        binRepository.save(bin);
    }


    public Bin getBinByURL(String url) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "bin:%s".formatted(url);
            String raw = jedis.get(key);

            System.out.println("Check 1");
            Bin bin;
            if (raw != null) {
                bin = objectMapper.readValue(raw, Bin.class);
                return checkTime(bin, jedis, key);
            }


            bin = getBinFromBD(url);
            System.out.println("Check 2");
            String binStr = objectMapper.writeValueAsString(bin);
            jedis.set(key, binStr);
            return checkTime(bin, jedis, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Bin checkTime(Bin bin, Jedis jedis, String Key) {
        OffsetDateTime now = OffsetDateTime.now();
        if (bin.getDeleteTime().isBefore(now)) {
            binRepository.delete(bin);
            jedis.del(Key);
            return null;
        }
        trendingService.incrementScore(bin);
        return bin;
    }

    private Bin getBinFromBD(String url) {
        return binRepository.findByUrl(url).orElseThrow();
    }

    public boolean checkURL(String url) {
        return binRepository.existsBinByUrl(url);
    }

    public Set<Object> getTrendingBins(int topN) {
        return trendingService.getTrendingBins(topN);
    }
}
