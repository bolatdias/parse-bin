package org.example.parsebin.repository;


import org.example.parsebin.model.Bin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface BinRepository extends JpaRepository<Bin, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Bin b WHERE b.deleteTime<:now")
    void deleteExpired(OffsetDateTime now);

    @Query(value = "SELECT nextval('bin_sequence')", nativeQuery = true)
    long getNextId();

    public Optional<Bin> findByUrl(String url);
}
