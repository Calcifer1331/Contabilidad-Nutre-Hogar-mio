package com.nutrehogar.sistemacontable.application.controller.service;

import com.nutrehogar.sistemacontable.application.config.ApplicationContext;
import com.nutrehogar.sistemacontable.application.controller.Controller;
import com.nutrehogar.sistemacontable.application.controller.business.GeneralLedgerController;
import com.nutrehogar.sistemacontable.application.controller.business.JournalController;
import com.nutrehogar.sistemacontable.application.controller.business.TrialBalanceController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountSubtypeController;
import com.nutrehogar.sistemacontable.application.controller.crud.AccountingEntryFormController;
import com.nutrehogar.sistemacontable.application.controller.crud.UserController;
import com.nutrehogar.sistemacontable.application.view.business.BusinessView;
import com.nutrehogar.sistemacontable.application.view.service.DashboardView;

import javax.swing.*;
import java.awt.*;


public class DashboardController extends Controller {
    private final ApplicationContext context;

    public DashboardController(DashboardView view, ApplicationContext context) {
        super(view);
        this.context = context;
        initialize();
    }

    @Override
    protected void initialize() {
        SwingUtilities.invokeLater(() -> {
            getPnlNav().setVisible(false);
            getPnlContent().setOpaque(false);
        });
        Thread.startVirtualThread(this::setupViewListeners);
        //            prepareToEditJournalEntry = (Integer JournalEntryId) -> {
//                setContent(getAccountingEntryFormController().getView());
//                getAccountingEntryFormController().prepareToEditEntry(JournalEntryId);
//            };
    }

    protected void setupViewListeners() {
        getBtnShowFormView().addActionListener(e ->
                setContent(context.getBean(AccountingEntryFormController.class).getView()));
        getBtnShowAccountSubtypeView().addActionListener(e -> setContent(context.getBean(AccountSubtypeController.class).getView()));
        getBtnShowAccountView().addActionListener(e -> {
            setContent(context.getBean(AccountController.class).getView());
            context.getBean(AccountController.class).loadData();
        });
        getBtnShowJournalView().addActionListener(e -> {
            setContent(context.getBean(JournalController.class).getView());
            context.getBean(JournalController.class).loadData();
        });
        getBtnShowTrialBalanceView().addActionListener(e -> {
            setContent(context.getBean(TrialBalanceController.class).getView());
            context.getBean(TrialBalanceController.class).loadData();
        });
        getBtnShowGeneralLedgerView().addActionListener(e -> {
            setContent(context.getBean(GeneralLedgerController.class).getView());
            context.getBean(GeneralLedgerController.class).loadDataSubtype();
            context.getBean(GeneralLedgerController.class).loadDataAccount();
        });
        getBtnShowBackupView().addActionListener(e -> context.getBean(BackupController.class).showView());
        getBtnHome().addActionListener(e -> setContent(getPnlHome()));
        getBtnShowUserView().addActionListener(e -> setContent(context.getBean(UserController.class).getView()));
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

    public JButton getBtnShowUserView() {
        return getView().getBtnShowUserView();
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
