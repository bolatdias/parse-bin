package org.example.parsebin.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrendingService {
    private static final String TRENDING_KEY = "trending:bins";

    private final ZSetOperations<String, String> zSetOperations;
    private final RedisTemplate<String, String> redisTemplate;

    public void incrementScore(String bin) {
        zSetOperations.incrementScore(TRENDING_KEY, bin, 1);
    }

    public Set<String> getTrendingBins(int topN) {
        return zSetOperations.reverseRange(TRENDING_KEY, 0, topN - 1);
    }

    public void resetTrendingBins() {
        zSetOperations.getOperations().delete(TRENDING_KEY);
    }
}
