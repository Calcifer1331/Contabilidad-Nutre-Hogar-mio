package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.exception.ReportException;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.GeneralLedgerDTOReport;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.GeneralLedgerReportDTO;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.SimpleReportDTO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class GeneralLedgerReport extends Report<GeneralLedgerDTOReport> {

    public GeneralLedgerReport() throws ReportException {
        super("Mayor General",
                "GeneralLedger.jrxml",
                ConfigLoader.Props.DIR_GENERAL_LEDGER_NAME.getPath());
    }

    @Override
    protected void setProps(Map<String, Object> parameters, GeneralLedgerDTOReport dto) {
        parameters.put("ENTRY_DATE", LocalDate.now().format(DATE_FORMATTER));
        parameters.put("START_DATE", dto.startDate().format(DATE_FORMATTER));
        parameters.put("END_DATE", dto.endDate().format(DATE_FORMATTER));
        parameters.put("ENTRY_ACCOUNT", dto.account());
        parameters.put("TABLE_DATA_SOURCE", new JRBeanCollectionDataSource(dto.dto()));
    }

    @Override
    protected String getDirReportPath(GeneralLedgerDTOReport dto) {
        var fileName = String.format("%s-%s-%s.pdf", dto.account(), dto.startDate().format(FILE_DATE_FORMATTER), dto.endDate().format(FILE_DATE_FORMATTER));
        return dirPath + File.separator + fileName;
    }
}