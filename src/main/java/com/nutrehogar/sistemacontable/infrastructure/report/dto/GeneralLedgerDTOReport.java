package com.nutrehogar.sistemacontable.infrastructure.report.dto;

import java.time.LocalDate;
import java.util.List;

public record GeneralLedgerDTOReport(LocalDate startDate,
                                     LocalDate endDate,
                                     String account,
                                     List<GeneralLedgerReportDTO> dto) {
}
