package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.JournalReportDTO;
import com.nutrehogar.sistemacontable.exception.ReportException;

public class Journal extends SimpleReport<JournalReportDTO> {

    public Journal() throws ReportException {
        super("Libro Diario",
                "Journal.jrxml",
                ConfigLoader.Props.DIR_JOURNAL_NAME.getPath());
    }
}
