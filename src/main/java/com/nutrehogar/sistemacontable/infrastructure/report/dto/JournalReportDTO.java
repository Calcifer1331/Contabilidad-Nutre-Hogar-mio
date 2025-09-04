package com.nutrehogar.sistemacontable.infrastructure.report.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class JournalReportDTO {
    String entryDate;
    String documentType;
    String accountId;
    String voucher;
    String reference;
    String debit;
    String credit;
}
