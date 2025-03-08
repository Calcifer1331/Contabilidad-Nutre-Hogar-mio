package com.nutrehogar.sistemacontable.infrastructure.report.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrialBalanceReportDTO {
    String date;
    String type;
    String accountId;
    String voucher;
    String reference;
    String debit;
    String credit;
    @Setter
    String balance;
}
