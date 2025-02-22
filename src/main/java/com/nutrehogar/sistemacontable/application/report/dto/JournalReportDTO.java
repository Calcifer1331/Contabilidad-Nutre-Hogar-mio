package com.nutrehogar.sistemacontable.application.report.dto;

import com.nutrehogar.sistemacontable.application.dto.JournalDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalReportDTO {
    LocalDate startDate;
    LocalDate endDate ;
    List<JournalDTO> journal;
}
