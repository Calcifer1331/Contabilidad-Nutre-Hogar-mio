package com.nutrehogar.sistemacontable.application.config;

import com.nutrehogar.sistemacontable.application.controller.AuthController;
import com.nutrehogar.sistemacontable.application.controller.crud.UserController;
import com.nutrehogar.sistemacontable.application.controller.service.DashboardController;
import com.nutrehogar.sistemacontable.application.view.AuthView;
import com.nutrehogar.sistemacontable.application.view.service.DashboardView;
import com.nutrehogar.sistemacontable.domain.Permissions;
import com.nutrehogar.sistemacontable.ui.view.business.DefaultGeneralLedgerView;
import com.nutrehogar.sistemacontable.ui.view.business.DefaultJournalView;
import com.nutrehogar.sistemacontable.ui.view.crud.DefaultUserView;
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
import com.nutrehogar.sistemacontable.ui.view.service.DefaultDashboardView;
import com.nutrehogar.sistemacontable.ui.view.service.LoginForm;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.function.Consumer;

public class AppConfig {

    private AppConfig() {
        throw new IllegalStateException("Utility class");
    }

    static Consumer<JournalEntryPK> editJournalEntry;

    public static void setup(@NotNull ApplicationContext context) {
        context.registerBean(User.class, User.builder()
                .username("Root")
                .permissions(Permissions.CREATE)
                .isEnable(true)
                .password("0922")
                .build());
        context.registerBean(UserRepository.class, new UserRepo());
        context.registerBean(AuthView.class, new LoginForm());
        context.registerBean(AuthController.class, new AuthController(context.getBean(AuthView.class), context.getBean(UserRepository.class), context.getBean(User.class)));
        context.registerBean(DashboardView.class, new DefaultDashboardView());
        var dashboard = new DashboardController(context.getBean(DashboardView.class), context);
        context.registerBean(DashboardController.class, dashboard);
        editJournalEntry = (JournalEntryPK JournalEntryId) -> {
            dashboard.setContent(context.getBean(AccountingEntryFormController.class).getView());
            context.getBean(AccountingEntryFormController.class).prepareToEditEntry(JournalEntryId);
        };
        ReportService.initializeReports();
    }

    public static void init(@NotNull ApplicationContext context, Session session, User user, JFrame parent) {

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
                editJournalEntry,
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(TrialBalanceController.class, new TrialBalanceController(
                context.getBean(JournalEntryRepository.class),
                new DefaultTrialBalanceView(),
                editJournalEntry,
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(GeneralLedgerController.class, new GeneralLedgerController(
                context.getBean(AccountRepository.class),
                new DefaultGeneralLedgerView(),
                editJournalEntry,
                context.getBean(AccountSubtypeRepository.class),
                context.getBean(LedgerRecordRepository.class),
                context.getBean(ReportService.class),
                user
        ));
        context.registerBean(BackupRepository.class, new BackupRepo());
        context.registerBean(BackupController.class, new BackupController(
                context.getBean(BackupRepository.class),
                new DefaultBackupView(),
                session,
                parent
        ));
        context.registerBean(UserController.class, new UserController(
                context.getBean(UserRepository.class),
                new DefaultUserView(),
                context.getBean(ReportService.class),
                user
        ));
        // Registrar otros controladores de manera similar...

    }
}