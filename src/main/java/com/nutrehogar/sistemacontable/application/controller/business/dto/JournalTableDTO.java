package com.nutrehogar.sistemacontable.application.controller.business.dto;

import com.nutrehogar.sistemacontable.application.dto.AuditableDTO;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.JournalEntryPK;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalTableDTO extends AuditableDTO {
    JournalEntryPK entryId;
    LocalDate entryDate;
    DocumentType documentType;
    Integer accountId;
    Integer voucher;
    String reference;
    BigDecimal debit;
    BigDecimal credit;

    public JournalTableDTO(String createdBy, String updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, JournalEntryPK entryId, LocalDate entryDate, DocumentType documentType, Integer accountId, Integer voucher, String reference, BigDecimal debit, BigDecimal credit) {
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
