package org.example.parsebin.controller;


import lombok.RequiredArgsConstructor;
import org.example.parsebin.model.Bin;
import org.example.parsebin.payload.BinAddRequest;
import org.example.parsebin.service.BinService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/bins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BinController {
    private final BinService binService;

    @PostMapping
    public String addBin(@RequestBody BinAddRequest request) {
        binService.saveBin(request);
        return "ok";
    }

    @GetMapping("/{binUrl}")
    public Bin getBin(@PathVariable String binUrl) {
        return binService.getBinByURL(binUrl);
    }

    @GetMapping("/trending")
        public Set<Object> getBinTrending() {
        return binService.getTrendingBins(10);
    }
}
