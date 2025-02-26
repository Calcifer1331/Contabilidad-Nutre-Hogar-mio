package com.nutrehogar.sistemacontable.application.config;

import com.nutrehogar.sistemacontable.ui.view.business.DefaultGeneralLedgerView;
import com.nutrehogar.sistemacontable.ui.view.business.DefaultJournalView;
import com.nutrehogar.sistemacontable.ui.view.service.DefaultBackupView;
import com.nutrehogar.sistemacontable.ui.view.crud.DefaultAccountView;
import com.nutrehogar.sistemacontable.ui.view.crud.DefaultAccountEntryFormView;
import com.nutrehogar.sistemacontable.ui.view.crud.DefaultAccountSubtypeView;
import com.nutrehogar.sistemacontable.application.controller.business.GeneralLedgerController;
import com.nutrehogar.sistemacontable.application.controller.business.JournalController;
import com.nutrehogar.sistemacontable.application.controller.business.TrialBalanceController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountSubtypeController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountingEntryFormController;
import com.nutrehogar.sistemacontable.application.controller.service.BackupController;
import com.nutrehogar.sistemacontable.application.repository.*;
import com.nutrehogar.sistemacontable.domain.repository.*;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.domain.model.*;
import com.nutrehogar.sistemacontable.ui.view.business.DefaultTrialBalanceView;
import org.hibernate.Session;

import javax.swing.*;

public class AppConfig {
    public static ApplicationContext init(Session session, User user, JFrame parent) {
        ApplicationContext context = new ApplicationContext();

        // Registro de repositorios
        context.registerBean(AccountRepository.class, new AccountRepo());
        context.registerBean(AccountSubtypeRepository.class, new AccountSubtypeRepo());
        context.registerBean(JournalEntryRepository.class, new JournalEntryRepo());
        context.registerBean(LedgerRecordRepository.class, new LedgerRecordRepo());
        context.registerBean(UserRepository.class, new UserRepo());

        // Registro de servicios
        context.registerBean(ReportService.class, new ReportService(user));

        // Registro de controladores
        context.registerBean(AccountingEntryFormController.class, new AccountingEntryFormController(
                context.getBean(LedgerRecordRepository.class),
                new DefaultAccountEntryFormView(),
                context.getBean(JournalEntryRepository.class),
                context.getBean(AccountRepository.class),
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(AccountController.class, new AccountController(
                context.getBean(AccountRepository.class),
                new DefaultAccountView(),
                context.getBean(AccountSubtypeRepository.class),
                context.getBean(ReportService.class),
                user
        ));

        context.registerBean(AccountSubtypeController.class, new AccountSubtypeController(
                context.getBean(AccountSubtypeRepository.class),
                new DefaultAccountSubtypeView(),
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(JournalController.class, new JournalController(
                context.getBean(JournalEntryRepository.class),
                new DefaultJournalView(),
                (integer) -> {
                },
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(TrialBalanceController.class, new TrialBalanceController(
                context.getBean(JournalEntryRepository.class),
                new DefaultTrialBalanceView(),
                (integer -> {
                }),
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(GeneralLedgerController.class, new GeneralLedgerController(
                context.getBean(AccountRepository.class),
                new DefaultGeneralLedgerView(),
                (integer -> {
                }),
                context.getBean(AccountSubtypeRepository.class),
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(BackupController.class, new BackupController(
                new DefaultBackupView(),
                session,
                parent
        ));
        // Registrar otros controladores de manera similar...

        return context;
    }
}