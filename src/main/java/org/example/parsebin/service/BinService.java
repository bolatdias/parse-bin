package org.example.parsebin.service;


import lombok.RequiredArgsConstructor;
import org.example.parsebin.model.Bin;
import org.example.parsebin.payload.BinAddRequest;
import org.example.parsebin.repository.BinRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class BinService {
    private final BinRepository binRepository;
    private final HashGeneratorService hashGeneratorService;
    private final Base64.Encoder encoder = Base64.getEncoder();

    public void saveBin(BinAddRequest request) {
        Bin bin = new Bin();
        bin.setText(request.getText());
        bin.setDeleteTime(request.getDeleteTime());
        bin.setUrl(encoder.encodeToString(String.valueOf(binRepository.getNextId()).getBytes()));
        binRepository.save(bin);
    }

    public Bin getBinByURL(String url) {
        Bin bin = binRepository.findByUrl(url).orElseThrow();
        return bin;
    }

}
