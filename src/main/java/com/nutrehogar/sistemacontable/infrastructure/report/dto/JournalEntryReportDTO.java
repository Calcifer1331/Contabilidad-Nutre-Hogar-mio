package com.nutrehogar.sistemacontable.infrastructure.report.dto;

import java.time.LocalDate;
import java.util.List;

public record JournalEntryReportDTO(
        int id,
        String checkNumber,
        LocalDate date,
        String name,
        String concept,
        String amount,
        List<LedgerRecordReportDTO> ledgerRecords) {
}
