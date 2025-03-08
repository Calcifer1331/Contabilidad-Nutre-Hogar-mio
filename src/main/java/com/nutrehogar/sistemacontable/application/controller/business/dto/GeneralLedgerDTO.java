package com.nutrehogar.sistemacontable.application.controller.business.dto;


import com.nutrehogar.sistemacontable.application.dto.AuditableDTO;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GeneralLedgerDTO extends AuditableDTO {
    Integer entryId;
    LocalDate entryDate;
    DocumentType documentType;
    Integer accountId;
    AccountType accountType;
    String voucher;
    String reference;
    BigDecimal debit;
    BigDecimal credit;
    @Setter
    BigDecimal balance;

    public GeneralLedgerDTO(String createdBy, String updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, Integer entryId, LocalDate entryDate, DocumentType documentType, Integer accountId, AccountType accountType, String voucher, String reference, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
        super(createdBy, updatedBy, createdAt, updatedAt);
        this.entryId = entryId;
        this.entryDate = entryDate;
        this.documentType = documentType;
        this.accountId = accountId;
        this.accountType = accountType;
        this.voucher = voucher;
        this.reference = reference;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }

    public GeneralLedgerDTO(String reference, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
        this.reference = reference;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }
}