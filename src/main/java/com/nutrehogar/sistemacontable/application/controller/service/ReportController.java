package com.nutrehogar.sistemacontable.application.controller.service;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.application.dto.JournalEntryDTO;
import com.nutrehogar.sistemacontable.exception.ReportException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Slf4j
public class ReportController {
    public static final Locale LOCALE = Locale.of("es", "PA");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", LOCALE);
    public static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", LOCALE);
    private static final String TEMPLATE_PATH = "/template/";
    private final String IMG_DIR;
    private final Map<String, Object> parameters;

    public ReportController() {
        parameters = new HashMap<>();
        IMG_DIR = ConfigLoader.Props.DIR_REPORTS_TEMPLATE_NAME.getPath().toString() + File.separator;
        log.info(IMG_DIR);
        initialize();
    }

    private void initialize() {
        parameters.put("IMG_DIR", IMG_DIR);
        parameters.put("MANAGER_NAME", "Licdo. Julío C. Guerra.");
        parameters.put("LOCATION", "Finca 12, Changuinola, Bocas del toro, Panamá");
        parameters.put("PHONE", "(+507) 758-6506");
        parameters.put("EMAIL", "nutrehogar@gmail.com");
    }

    public void generateReport(@NotNull ReportType reportType, JournalEntryDTO journal) throws ReportException {
        Map<String, Object> params = new HashMap<>(this.parameters);
        reportType.setProps(params, journal);
        params.put("TABLE_DATA_SOURCE", new JRBeanCollectionDataSource(journal.ledgerRecords()));

        InputStream templateStream = ReportController.class.getResourceAsStream(TEMPLATE_PATH + reportType.getTemplateName());

        if (templateStream == null) {
            log.error("templateStream is null");
            throw new ReportException("Failed to load file " + TEMPLATE_PATH + reportType.getTemplateName());
        }

        log.info("templateName {}", reportType.getTemplateName());
        log.info("templateStream {}", templateStream);

        JasperReport reportTemplate;

        try {
            reportTemplate = JasperCompileManager.compileReport(templateStream);
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw new ReportException("Error compiling report: " + e.getMessage(), e);
        }

        log.info("Loading template object  {}", reportTemplate);
        log.info("Loading template CompilerClass {}", reportTemplate.getCompilerClass());
        log.info("Loading template name {}", reportTemplate.getName());

        try {
            log.info("checkedStream {}", templateStream);
            log.info("params {}", params);

            JasperPrint print = JasperFillManager.fillReport(reportTemplate, params, new JREmptyDataSource());

            log.info("Report generated successfully {}", print.getName());

            JasperExportManager.exportReportToPdfFile(print, getDirReportPath(reportType, journal));

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ReportException(e.getMessage(), e);
        }
    }

    private @NotNull String getDirReportPath(@NotNull ReportType reportType, @NotNull JournalEntryDTO journal) {
        String fileName = String.format("%s_%s_%s.pdf", reportType.getName(), journal.id(), journal.date().format(FILE_DATE_FORMATTER));
        log.info("filename {}", fileName);
        log.info("file path {}", reportType.getDirPath() + File.separator + fileName);
        return reportType.getDirPath() + File.separator + fileName;
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum ReportType {
        PAYMENT_VOUCHER(
                "Comprobante de Pago",
                "PaymentVoucher.jrxml",
                ConfigLoader.Props.DIR_PAYMENT_VOUCHER_NAME.getPath()
        ) {
            @Override
            protected void setProps(Map<String, Object> parameters, JournalEntryDTO journal) {
                parameters.put("ENTRY_ID", String.valueOf(journal.id()));
                parameters.put("ENTRY_DATE", journal.date().format(DATE_FORMATTER));
                parameters.put("ENTRY_NAME", journal.name());
                parameters.put("ENTRY_CONCEPT", journal.concept());
                parameters.put("ENTRY_AMOUNT", journal.amount());
            }
        }, REGISTRATION_FORM(
                "Formulario de Registro",
                "RegistrationForm.jrxml",
                ConfigLoader.Props.DIR_REGISTRATION_FORM_NAME.getPath()
        ) {
            @Override
            protected void setProps(Map<String, Object> parameters, JournalEntryDTO journal) {
                parameters.put("ENTRY_ID", String.valueOf(journal.id()));
                parameters.put("ENTRY_DATE", journal.date().format(DATE_FORMATTER));
                parameters.put("ENTRY_NAME", journal.name());
                parameters.put("ENTRY_CONCEPT", journal.concept());
                parameters.put("ENTRY_AMOUNT", journal.amount());
            }
        };

        String name;
        String templateName;
        Path dirPath;

        protected abstract void setProps(Map<String, Object> parameters, JournalEntryDTO journal);
    }
}
