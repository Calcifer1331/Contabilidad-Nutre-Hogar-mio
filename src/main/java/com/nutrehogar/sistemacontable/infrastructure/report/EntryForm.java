package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.infrastructure.report.dto.JournalEntryReportDTO;
import com.nutrehogar.sistemacontable.exception.ReportException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
public sealed class EntryForm extends Report<JournalEntryReportDTO> permits PaymentVoucher, RegistrationForm{
    public EntryForm(String name, String templateName, Path dirPath) throws ReportException {
        super(name, templateName, dirPath);
    }

    @Override
    protected void setProps(Map<String, Object> parameters, JournalEntryReportDTO dto) {
        parameters.put("ENTRY_ID", String.valueOf(dto.id()));
        parameters.put("ENTRY_DATE", dto.date().format(DATE_FORMATTER));
        parameters.put("ENTRY_NAME", dto.name());
        parameters.put("ENTRY_CONCEPT", dto.concept());
        parameters.put("ENTRY_AMOUNT", dto.amount());
        parameters.put("ENTRY_CHECK_NUMBER", dto.checkNumber());
        parameters.put("TABLE_DATA_SOURCE", new JRBeanCollectionDataSource(dto.ledgerRecords()));
    }

    @Override
    protected String getDirReportPath(JournalEntryReportDTO dto) {
        var fileName = String.format("%s#%s_%s.pdf", name, dto.id(), dto.date().format(FILE_DATE_FORMATTER));
        return dirPath + File.separator + fileName;
    }
}