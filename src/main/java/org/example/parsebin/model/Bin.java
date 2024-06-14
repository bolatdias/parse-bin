package org.example.parsebin.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Data
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bin_sequence")
    @SequenceGenerator(name = "bin_sequence", sequenceName = "bin_sequence", allocationSize = 1)
    private long id;
    private String text;
    private String url;

    @CreationTimestamp
    private OffsetDateTime created;
    private OffsetDateTime deleteTime;
}
