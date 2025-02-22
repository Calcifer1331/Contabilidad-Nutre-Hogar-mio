package com.nutrehogar.sistemacontable.application.report;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.application.report.dto.JournalReportDTO;
import com.nutrehogar.sistemacontable.exception.ReportException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

public class Journal extends Report<JournalReportDTO> {

    public Journal() throws ReportException {
        super("Libro Diario",
                "Journal.jrxml",
                ConfigLoader.Props.DIR_PAYMENT_VOUCHER_NAME.getPath());
    }

    @Override
    protected void setProps(Map<String, Object> parameters, JournalReportDTO dto) {
        parameters.put("ENTRY_DATE", LocalDate.now());
        parameters.put("START_DATE", dto.getStartDate());
        parameters.put("END_DATE", dto.getEndDate());
        parameters.put("TABLE_DATA_SOURCE", new JRBeanCollectionDataSource(dto.getJournal()));
    }

    @Override
    protected String getDirReportPath(JournalReportDTO dto) {
        var fileName = String.format("%s-%s-%s.pdf", name, dto.getStartDate().format(FILE_DATE_FORMATTER), dto.getEndDate().format(FILE_DATE_FORMATTER));
        return dirPath + File.separator + fileName;
    }

}
