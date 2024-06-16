package org.example.parsebin.service;


import lombok.RequiredArgsConstructor;
import org.example.parsebin.model.Bin;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrendingService {
    private static final String TRENDING_KEY = "trending:bins";
    private static final int MAX_SIZE = 2000;

    private final ZSetOperations<String, Object> zSetOperations;

    public void incrementScore(Bin bin) {
        double timestamp = bin.getCreated().toInstant().getEpochSecond();
        boolean added = zSetOperations.addIfAbsent(TRENDING_KEY, bin, timestamp);

        if (!added) {
            zSetOperations.incrementScore(TRENDING_KEY, bin, 600);
        }
    }


    public Set<Object> getTrendingBins(int topN) {
        return zSetOperations.reverseRange(TRENDING_KEY, 0, topN - 1);
    }

    @Scheduled(fixedRate = 1000 * 60 * 10)
    private void trimToMaxSize() {
        Long size = zSetOperations.zCard(TRENDING_KEY);
        if (size != null && size > MAX_SIZE) {
            zSetOperations.removeRange(TRENDING_KEY, 0, size - MAX_SIZE - 1);
        }
    }

    public void resetTrendingBins() {
        zSetOperations.getOperations().delete(TRENDING_KEY);
    }
}
