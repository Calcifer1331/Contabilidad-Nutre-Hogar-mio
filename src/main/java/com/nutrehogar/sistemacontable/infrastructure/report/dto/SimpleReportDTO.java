package com.nutrehogar.sistemacontable.infrastructure.report.dto;

import java.time.LocalDate;
import java.util.List;

public record SimpleReportDTO<T>(
        LocalDate startDate,
        LocalDate endDate,
        List<T> dto) {
}
