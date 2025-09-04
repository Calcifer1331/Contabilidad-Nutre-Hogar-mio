package com.nutrehogar.sistemacontable.infrastructure.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LedgerRecordReportDTO {
    String documentType;
    String voucher;
    String accountId;
    String reference;
    String debit;
    String credit;
}