package com.nutrehogar.sistemacontable.application.controller.business.dto;

/**
 * @author Jayson
 */

import com.nutrehogar.sistemacontable.application.dto.AuditableDTO;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.JournalEntryPK;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TrialBalanceTableDTO extends AuditableDTO {
    JournalEntryPK journalId;
    LocalDate journalDate;
    DocumentType documentType;
    Integer accountId;
    String accountName;
    AccountType accountType;
    Integer voucher;
    String reference;
    BigDecimal debit;
    BigDecimal credit;
    @Setter
    BigDecimal balance;

    public TrialBalanceTableDTO(String createdBy, String updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, JournalEntryPK journalId, LocalDate journalDate, DocumentType documentType, Integer accountId, String accountName, AccountType accountType, Integer voucher, String reference, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
        super(createdBy, updatedBy, createdAt, updatedAt);
        this.journalId = journalId;
        this.journalDate = journalDate;
        this.documentType = documentType;
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.voucher = voucher;
        this.reference = reference;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }

    public TrialBalanceTableDTO(String reference, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
        this.reference = reference;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }
}
