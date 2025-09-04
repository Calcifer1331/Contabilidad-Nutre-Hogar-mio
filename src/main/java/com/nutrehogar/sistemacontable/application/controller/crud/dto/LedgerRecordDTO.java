package com.nutrehogar.sistemacontable.application.controller.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LedgerRecordDTO {
    String accountId;
    String reference;
    String debit;
    String credit;
}