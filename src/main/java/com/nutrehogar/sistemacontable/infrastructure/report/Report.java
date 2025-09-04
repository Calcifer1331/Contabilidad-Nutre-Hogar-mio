package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.exception.ReportException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
public abstract class Report<T> {
    public static final Locale LOCALE = Locale.of("es", "PA");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", LOCALE);
    public static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", LOCALE);
    protected static final String TEMPLATE_PATH = "/template/";

    protected String name;
    protected String templateName;
    protected Path dirPath;
    protected JasperReport jasperReport;

    public Report(String name, String templateName, Path dirPath) throws ReportException {
        this.name = name;
        this.templateName = templateName;
        this.dirPath = dirPath;
        initializeJasperReport();
    }

    private void initializeJasperReport() throws ReportException {
        try {
            InputStream templateStream = Report.class.getResourceAsStream(TEMPLATE_PATH + getTemplateName());
            if (templateStream == null) {
                throw new ReportException("Template not found: " + TEMPLATE_PATH + getTemplateName());
            }
            this.jasperReport = JasperCompileManager.compileReport(templateStream);
        } catch (JRException e) {
            throw new ReportException("Failed to compile report template: " + e.getMessage(), e);
        }
    }

    public void generateReport(Map<String, Object> parameters, T dto) throws ReportException {
        setProps(parameters, dto);
        try {
            JasperPrint print = generate(parameters);
            JasperExportManager.exportReportToPdfFile(print, getDirReportPath(dto));
        } catch (Exception e) {
            throw new ReportException(e.getMessage(), e);
        }
    }

    protected abstract void setProps(Map<String, Object> parameters, T dto);

    protected JasperPrint generate(Map<String, Object> parameters) throws JRException {
        return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
    }

    protected abstract String getDirReportPath(T dto);

}