package com.nutrehogar.sistemacontable.infrastructure.report.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneralLedgerReportDTO {
    String entryId;
    String entryDate;
    String documentType;
    String accountId;
    String accountType;
    String voucher;
    String reference;
    String debit;
    String credit;
    @Setter
    String balance;
}
