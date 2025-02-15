package com.nutrehogar.sistemacontable.application.controller.service;

import com.nutrehogar.sistemacontable.application.controller.Controller;
import com.nutrehogar.sistemacontable.application.controller.business.GeneralLedgerController;
import com.nutrehogar.sistemacontable.application.controller.business.JournalController;
import com.nutrehogar.sistemacontable.application.controller.business.TrialBalanceController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountSubtypeController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountingEntryFormController;
import com.nutrehogar.sistemacontable.ui.view.DashboardView;
import com.nutrehogar.sistemacontable.ui.view.business.BusinessView;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class DashboardController extends Controller {
    private final AccountingEntryFormController accountingEntryFormController;
    private final AccountController accountController;
    private final AccountSubtypeController accountSubtypeController;
    private final JournalController journalController;
    private final TrialBalanceController trialBalanceController;
    private final GeneralLedgerController generalLedgerController;
    private final BackupController backupController;

    public DashboardController(DashboardView view, AccountingEntryFormController accountingEntryFormController, AccountController accountController, AccountSubtypeController accountSubtypeController, JournalController journalController, TrialBalanceController trialBalanceController, GeneralLedgerController generalLedgerController, BackupController backupController) {
        super(view);
        this.accountingEntryFormController = accountingEntryFormController;
        this.accountController = accountController;
        this.accountSubtypeController = accountSubtypeController;
        this.journalController = journalController;
        this.trialBalanceController = trialBalanceController;
        this.generalLedgerController = generalLedgerController;
        this.backupController = backupController;
        initialize();
    }

    @Override
    protected void initialize() {
        getPnlContent().setOpaque(false);
        setupViewListeners();
    }

    protected void setupViewListeners() {
        getBtnShowFormView().addActionListener(e -> setContent(accountingEntryFormController.getView()));
        getBtnShowAccountSubtypeView().addActionListener(e -> setContent(accountSubtypeController.getView()));
        getBtnShowAccountView().addActionListener(e -> setContent(accountController.getView()));
        getBtnShowJournalView().addActionListener(e -> setContent(journalController.getView()));
        getBtnShowTrialBalanceView().addActionListener(e -> setContent(trialBalanceController.getView()));
        getBtnShowGeneralLedgerView().addActionListener(e -> setContent(generalLedgerController.getView()));
        getBtnShowBackupView().addActionListener(e -> backupController.showView());
    }

    public void setContent(JPanel p) {
        getPnlContent().removeAll();
        getPnlContent().setLayout(new BorderLayout());
        getPnlContent().add(p, BorderLayout.CENTER);
        getPnlContent().revalidate();
        getPnlContent().repaint();
        if (p instanceof BusinessView view){
            view.getBtnFilter().doClick();
        }
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
}
