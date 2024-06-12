package org.example.parsebin.payload;


import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BinAddRequest {
    private String text;
    private OffsetDateTime deleteTime;
}
