package org.example.parsebin.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Data
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bin_sequence")
    private long id;
    private String text;
    private String url;

    @CreationTimestamp
    private OffsetDateTime created;
    private OffsetDateTime deleteTime;
}
