package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.exception.ReportException;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.SimpleReportDTO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

public abstract class SimpleReport<D> extends Report<SimpleReportDTO<D>> {

    public SimpleReport(String name, String templateName, Path dirPath) throws ReportException {
        super(name, templateName, dirPath);
    }

    @Override
    protected void setProps(Map<String, Object> parameters, SimpleReportDTO<D> dto) {
        parameters.put("ENTRY_DATE", LocalDate.now().format(DATE_FORMATTER));
        parameters.put("START_DATE", dto.startDate().format(DATE_FORMATTER));
        parameters.put("END_DATE", dto.endDate().format(DATE_FORMATTER));
        parameters.put("TABLE_DATA_SOURCE", new JRBeanCollectionDataSource(dto.dto()));
    }

    @Override
    protected String getDirReportPath(SimpleReportDTO<D> dto) {
        var fileName = String.format("%s-%s-%s.pdf", name, dto.startDate().format(FILE_DATE_FORMATTER), dto.endDate().format(FILE_DATE_FORMATTER));
        return dirPath + File.separator + fileName;
    }
}
