package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.application.MainClass;
import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.exception.ReportException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
public class ReportService {
    private final Map<Class<? extends Report<?>>, Report<?>> reports;
    private static final String IMG_DIR = ConfigLoader.Props.DIR_REPORTS_TEMPLATE_NAME.getPath().toString() + File.separator;
    private final Map<String, Object> parameters;
    private final User user;

    public ReportService(User user) {
        this.user = user;
        reports = new HashMap<>();
        parameters = new HashMap<>();
        initialize();
    }

    private void initialize() {
        parameters.put("IMG_DIR", IMG_DIR);
        parameters.put("LOCATION", "Finca 12, Changuinola, Bocas del toro, Panamá");
        parameters.put("PHONE", "(+507) 758-6506");
        parameters.put("EMAIL", "nutrehogar@gmail.com");
        parameters.put("MANAGER_NAME", user.getUsername());
        Thread.startVirtualThread(() -> {
            reports.put(PaymentVoucher.class, new PaymentVoucher());
            reports.put(RegistrationForm.class, new RegistrationForm());
            reports.put(Journal.class, new Journal());
            reports.put(TrialBalance.class, new TrialBalance());
            reports.put(GeneralLedgerReport.class, new GeneralLedgerReport());
        });
    }

    public <T> void generateReport(@NotNull Class<? extends Report<T>> reportClass, T dto) throws ReportException {

        Map<String, Object> params = new HashMap<>(this.parameters);

        Report<T> generate = (Report<T>) reports.get(reportClass);

        if (generate == null) {
            throw new ReportException("Type no suport: " + reportClass.getSimpleName());
        }

        generate.generateReport(params, dto);
    }
}