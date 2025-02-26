package com.nutrehogar.sistemacontable.application.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditableDTO {
    String createdBy;

    String updatedBy;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
