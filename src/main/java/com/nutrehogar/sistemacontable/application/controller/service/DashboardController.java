package com.nutrehogar.sistemacontable.application.controller.service;

import com.nutrehogar.sistemacontable.application.controller.Controller;
import com.nutrehogar.sistemacontable.application.controller.business.GeneralLedgerController;
import com.nutrehogar.sistemacontable.application.controller.business.JournalController;
import com.nutrehogar.sistemacontable.application.controller.business.TrialBalanceController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountSubtypeController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountingEntryFormController;
import com.nutrehogar.sistemacontable.application.repository.crud.*;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryFactory;
import com.nutrehogar.sistemacontable.domain.model.*;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.ui.view.business.GeneralLedgerView;
import com.nutrehogar.sistemacontable.ui.view.business.JournalView;
import com.nutrehogar.sistemacontable.ui.view.business.TrialBalanceView;
import com.nutrehogar.sistemacontable.ui.view.crud.AccountSubtypeView;
import com.nutrehogar.sistemacontable.ui.view.crud.AccountView;
import com.nutrehogar.sistemacontable.ui.view.crud.AccountingEntryFormView;
import com.nutrehogar.sistemacontable.ui.view.crud.DefaultTrialBalanceView;
import com.nutrehogar.sistemacontable.ui.view.imple.*;
import com.nutrehogar.sistemacontable.ui.view.service.BackupView;
import com.nutrehogar.sistemacontable.ui.view.service.DashboardView;
import com.nutrehogar.sistemacontable.ui.view.business.BusinessView;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class DashboardController extends Controller {

    private final Session session;
    private AccountRepository accountRepository;
    private AccountSubtypeRepository subTipoRepository;
    private JournalEntryRepository journalEntryRepository;
    private LedgerRecordRepository ledgerRecordRepository;
    private final UserRepository userRepository;

    private AccountingEntryFormController accountingEntryFormController;
    private AccountController accountController;
    private AccountSubtypeController accountSubtypeController;
    private JournalController journalController;
    private TrialBalanceController trialBalanceController;
    private GeneralLedgerController generalLedgerController;
    private BackupController backupController;
    private ReportService reportService;

    private AccountingEntryFormView accountingEntryFormView;
    private AccountView accountView;
    private AccountSubtypeView accountSubtypeView;
    private JournalView journalView;
    private TrialBalanceView trialBalanceView;
    private GeneralLedgerView generalLedgerView;
    private BackupView backupView;

    private Consumer<Integer> prepareToEditJournalEntry;
    private final JFrame parent;
    private final User user;

    public DashboardController(DashboardView view, JFrame parent, UserRepository userRepository, Session session, User user) {
        super(view);
        this.userRepository = userRepository;
        this.session = session;
        this.parent = parent;
        this.user = user;
        initialize();
    }

    @Override
    protected void initialize() {
        SwingUtilities.invokeLater(() -> {
            getPnlNav().setVisible(false);
            getPnlContent().setOpaque(false);
        });
        Thread.startVirtualThread(() -> {
            setupViewListeners();
            prepareToEditJournalEntry = (Integer JournalEntryId) -> {
                setContent(getAccountingEntryFormController().getView());
                getAccountingEntryFormController().prepareToEditEntry(JournalEntryId);
            };
        });
    }

    protected void setupViewListeners() {
        getBtnShowFormView().addActionListener(e -> setContent(getAccountingEntryFormController().getView()));
        getBtnShowAccountSubtypeView().addActionListener(e -> setContent(getAccountSubtypeController().getView()));
        getBtnShowAccountView().addActionListener(e -> setContent(getAccountController().getView()));
        getBtnShowJournalView().addActionListener(e -> setContent(getJournalController().getView()));
        getBtnShowTrialBalanceView().addActionListener(e -> setContent(getTrialBalanceController().getView()));
        getBtnShowGeneralLedgerView().addActionListener(e -> setContent(getGeneralLedgerController().getView()));
        getBtnShowBackupView().addActionListener(e -> getBackupController().showView());
        getBtnHome().addActionListener(e -> setContent(getPnlHome()));
    }

    public void setContent(JPanel p) {
        SwingUtilities.invokeLater(() -> {
            if (p != getPnlHome()) {
                getPnlNav().setVisible(true);
            } else {
                getPnlNav().setVisible(false);
                getBtnShowFormView().setBackground(Color.WHITE);
                getBtnShowJournalView().setBackground(Color.WHITE);
                getBtnShowTrialBalanceView().setBackground(Color.WHITE);
                getBtnShowGeneralLedgerView().setBackground(Color.WHITE);
                getBtnShowAccountView().setBackground(Color.WHITE);
                getBtnShowAccountSubtypeView().setBackground(Color.WHITE);
            }
            getPnlContent().removeAll();
            getPnlContent().setLayout(new BorderLayout());
            getPnlContent().add(p, BorderLayout.CENTER);
            getPnlContent().revalidate();
            getPnlContent().repaint();
            if (p instanceof BusinessView view) {
                view.getBtnFilter().doClick();
            }
        });
    }

    private <T> T ensureInitialized(T instance, Supplier<T> creator) {
        return instance != null ? instance : creator.get();
    }

    public AccountingEntryFormView getAccountingEntryFormView() {
        return accountingEntryFormView = ensureInitialized(accountingEntryFormView, DefaultAccountEntryFormView::new);
    }

    public AccountView getAccountView() {
        return accountView = ensureInitialized(accountView, DefaultAccountView::new);
    }

    public AccountSubtypeView getAccountSubtypeView() {
        return accountSubtypeView = ensureInitialized(accountSubtypeView, DefaultAccountSubtypeView::new);
    }

    public JournalView getJournalView() {
        return journalView = ensureInitialized(journalView, DefaultJournalView::new);
    }

    public TrialBalanceView getTrialBalanceView() {
        return trialBalanceView = ensureInitialized(trialBalanceView, DefaultTrialBalanceView::new);
    }

    public GeneralLedgerView getGeneralLedgerView() {
        return generalLedgerView = ensureInitialized(generalLedgerView, DefaultGeneralLedgerView::new);
    }

    public BackupView getBackupView() {
        return backupView = ensureInitialized(backupView, DefaultBackupView::new);
    }

    public AccountRepository getAccountRepository() {
        return accountRepository = ensureInitialized(accountRepository, () -> CRUDRepositoryFactory.createRepository(AccountRepository.class, Account.class, session));
    }

    public AccountSubtypeRepository getAccountSubtypeRepository() {
        return subTipoRepository = ensureInitialized(subTipoRepository, () -> CRUDRepositoryFactory.createRepository(AccountSubtypeRepository.class, AccountSubtype.class, session));
    }

    public JournalEntryRepository getJournalEntryRepository() {
        return journalEntryRepository = ensureInitialized(journalEntryRepository, () -> CRUDRepositoryFactory.createRepository(JournalEntryRepository.class, JournalEntry.class, session));
    }

    public LedgerRecordRepository getLedgerRecordRepository() {
        return ledgerRecordRepository = ensureInitialized(ledgerRecordRepository, () -> CRUDRepositoryFactory.createRepository(LedgerRecordRepository.class, LedgerRecord.class, session));
    }

    public AccountingEntryFormController getAccountingEntryFormController() {
        return accountingEntryFormController = ensureInitialized(accountingEntryFormController, () -> new AccountingEntryFormController(getLedgerRecordRepository(), getAccountingEntryFormView(), getJournalEntryRepository(), getAccountRepository(), getReportController(), user));
    }

    public AccountController getAccountController() {
        return accountController = ensureInitialized(accountController, () -> new AccountController(getAccountRepository(), getAccountView(), getAccountSubtypeRepository(), getReportController(), user));
    }

    public AccountSubtypeController getAccountSubtypeController() {
        return accountSubtypeController = ensureInitialized(accountSubtypeController, () -> new AccountSubtypeController(getAccountSubtypeRepository(), getAccountSubtypeView(), getReportController(), user));
    }

    public JournalController getJournalController() {
        return journalController = ensureInitialized(journalController, () -> new JournalController(getJournalEntryRepository(), getJournalView(), prepareToEditJournalEntry, getReportController(), user));
    }

    public TrialBalanceController getTrialBalanceController() {
        return trialBalanceController = ensureInitialized(trialBalanceController, () -> new TrialBalanceController(getJournalEntryRepository(), getTrialBalanceView(), prepareToEditJournalEntry, getReportController(), user));
    }

    //
    public GeneralLedgerController getGeneralLedgerController() {
        return generalLedgerController = ensureInitialized(generalLedgerController, () -> new GeneralLedgerController(getAccountRepository(), getGeneralLedgerView(), prepareToEditJournalEntry, getAccountSubtypeRepository(), getReportController(), user));
    }

    public BackupController getBackupController() {
        return backupController = ensureInitialized(backupController, () -> new BackupController(getBackupView(), session, parent));
    }

    public ReportService getReportController() {
        return reportService = ensureInitialized(reportService, ReportService::new);
    }

    @Override
    public DashboardView getView() {
        return (DashboardView) super.getView();
    }

    public JButton getBtnShowFormView() {
        return getView().getBtnShowFormView();
    }

    public JButton getBtnShowJournalView() {
        return getView().getBtnShowJournalView();
    }

    public JButton getBtnShowTrialBalanceView() {
        return getView().getBtnShowTrialBalanceView();
    }

    public JButton getBtnShowGeneralLedgerView() {
        return getView().getBtnShowGeneralLedgerView();
    }

    public JButton getBtnShowAccountView() {
        return getView().getBtnShowAccountView();
    }

    public JButton getBtnShowAccountSubtypeView() {
        return getView().getBtnShowAccountSubtypeView();
    }

    public JButton getBtnShowBackupView() {
        return getView().getBtnShowBackupView();
    }

    public JPanel getPnlContent() {
        return getView().getPnlContent();
    }

    public JButton getBtnHome() {
        return getView().getBtnHome();
    }

    public JPanel getPnlHome() {
        return getView().getPnlHome();
    }

    public JPanel getPnlNav() {
        return getView().getPnlNav();
    }
}
