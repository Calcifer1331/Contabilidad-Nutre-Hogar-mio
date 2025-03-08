package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.exception.ReportException;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.TrialBalanceReportDTO;

import java.nio.file.Path;

public class TrialBalance extends SimpleReport<TrialBalanceReportDTO> {

    public TrialBalance() throws ReportException {
        super("Balance General",
                "TrialBalance.jrxml",
                ConfigLoader.Props.DIR_TRIAL_BALANCE_NAME.getPath());
    }

}