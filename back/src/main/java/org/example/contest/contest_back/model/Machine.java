package org.example.contest.contest_back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "machines")
public class Machine {
    @Id
    @Column(name = "machine_id", nullable = false)
    private Long id;

    @Column(name = "dosage_created_at", nullable = false)
    private LocalDateTime dosageCreatedAt;

    @Column(name = "dosage_closed_at", nullable = false)
    private LocalDateTime dosageClosedAt;

}