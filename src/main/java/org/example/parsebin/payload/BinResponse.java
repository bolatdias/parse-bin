package org.example.parsebin.payload;

import lombok.Data;

@Data
public class BinResponse {
    private Long id;
    private String text;
    private String url;
}
