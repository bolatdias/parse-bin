package org.example.parsebin.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.parsebin.model.Bin;
import org.example.parsebin.payload.BinAddRequest;
import org.example.parsebin.repository.BinRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BinService {
    private final BinRepository binRepository;
    private final HashGeneratorService hashGeneratorService;
    private final TrendingService trendingService;
    private final ObjectMapper objectMapper;

    public void saveBin(BinAddRequest request) {
        Bin bin = new Bin();
        bin.setText(request.getText());
        bin.setDeleteTime(request.getDeleteTime());
        bin = binRepository.save(bin);
        bin.setUrl(hashGeneratorService.getCachedHash(bin.getId()));
        binRepository.save(bin);
    }

    public Bin getBinByURL(String url) {
        Bin bin = binRepository.findByUrl(url).orElseThrow(

        );

        OffsetDateTime now = OffsetDateTime.now();

        if (bin.getDeleteTime().isBefore(now)) {
            binRepository.delete(bin);
            return null;
        }

        trendingService.incrementScore(bin);


        return bin;
    }

    public Set<Object> getTrendingBins(int topN) {
        return trendingService.getTrendingBins(topN);
    }
}
