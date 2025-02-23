package com.nutrehogar.sistemacontable.application.dto;

import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.AuditableEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalDTO extends AuditableDTO {
    Integer entryId;
    LocalDate entryDate;
    DocumentType documentType;
    Integer accountId;
    String voucher;
    String reference;
    BigDecimal debit;
    BigDecimal credit;

    public JournalDTO(String createdBy, String updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, Integer entryId, LocalDate entryDate, DocumentType documentType, Integer accountId, String voucher, String reference, BigDecimal debit, BigDecimal credit) {
        super(createdBy, updatedBy, createdAt, updatedAt);
        this.entryId = entryId;
        this.entryDate = entryDate;
        this.documentType = documentType;
        this.accountId = accountId;
        this.voucher = voucher;
        this.reference = reference;
        this.debit = debit;
        this.credit = credit;
    }
}
