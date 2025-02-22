package com.nutrehogar.sistemacontable.application.report;

import com.nutrehogar.sistemacontable.application.MainClass;
import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.exception.ReportException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class ReportController {
    private final String IMG_DIR;
    private final Map<String, Object> parameters;
    private PaymentVoucher paymentVoucher;
    private RegistrationForm  registrationForm;

    public ReportController() {
        parameters = new HashMap<>();
        IMG_DIR = ConfigLoader.Props.DIR_REPORTS_TEMPLATE_NAME.getPath().toString() + File.separator;
        log.info(IMG_DIR);
        initialize();
    }

    private void initialize() {
        parameters.put("IMG_DIR", IMG_DIR);
        parameters.put("MANAGER_NAME", MainClass.USER.getUsername());
        parameters.put("LOCATION", "Finca 12, Changuinola, Bocas del toro, Panamá");
        parameters.put("PHONE", "(+507) 758-6506");
        parameters.put("EMAIL", "nutrehogar@gmail.com");
    }

    public <T> void generateReport(Class<? extends Report<T>> reportClass, T dto) throws ReportException {
        Map<String, Object> params = new HashMap<>(this.parameters);

        Report<T> generate = null;

        if (reportClass.equals(PaymentVoucher.class)) {
            generate = (Report<T>) getPaymentVoucherReport();
        } else if (reportClass.equals(RegistrationForm.class)) {
            generate = (Report<T>) getRegistrationForm();
        }

        if (generate == null) {
            throw new ReportException("Type no suport: " + reportClass.getSimpleName());
        }

        generate.generateReport(params, dto);
    }

    public PaymentVoucher getPaymentVoucherReport() {
        if (paymentVoucher == null) paymentVoucher = new PaymentVoucher();
        return paymentVoucher;
    }
public RegistrationForm getRegistrationForm() {
        if (registrationForm == null) registrationForm = new RegistrationForm();
        return registrationForm;
}
}