package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.application.controller.SimpleController;
import com.nutrehogar.sistemacontable.domain.model.JournalEntryPK;
import com.nutrehogar.sistemacontable.exception.ApplicationException;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.dto.AuditableDTO;
import com.nutrehogar.sistemacontable.application.repository.SimpleRepository;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.components.LocalDateSpinner;
import com.nutrehogar.sistemacontable.ui.components.LocalDateSpinnerModel;
import com.nutrehogar.sistemacontable.application.view.business.BusinessView;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import static com.nutrehogar.sistemacontable.application.config.Util.CURRENT_DATE;

@Getter
@Setter
public abstract class BusinessController<T extends AuditableDTO, R> extends SimpleController<T, R> {
    public static final LocalDate END_PERIOD = LocalDate.of(CURRENT_DATE.getYear(), 12, 31);
    public static final LocalDate START_PERIOD = LocalDate.of(CURRENT_DATE.getYear(), 1, 1);
    protected LocalDateSpinnerModel spnModelStartPeriod;
    protected LocalDateSpinnerModel spnModelEndPeriod;
    protected final Consumer<JournalEntryPK> editJournalEntry;
    protected JournalEntryPK journalEntryId;


    public BusinessController(SimpleRepository<R> repository, BusinessView view, Consumer<JournalEntryPK> editJournalEntry, ReportService reportService, User user) {
        super(repository, view, reportService, user);
        this.editJournalEntry = editJournalEntry;
    }

    @Override
    protected void initialize() {
        startSpnModel();
        super.initialize();
    }

    private void startSpnModel() {
        spnModelStartPeriod = getSpnStart().getModel();
        if (spnModelStartPeriod == null) {
            spnModelStartPeriod = new LocalDateSpinnerModel(START_PERIOD);
        } else {
            setStartPeriod();
        }
        spnModelEndPeriod = getSpnEnd().getModel();
        if (spnModelEndPeriod == null) {
            spnModelEndPeriod = new LocalDateSpinnerModel(END_PERIOD);
        } else {
            setEndPeriod();
        }
    }

    private void setStartPeriod() {
        spnModelStartPeriod.setValue(START_PERIOD);
    }

    private void setEndPeriod() {
        spnModelEndPeriod.setValue(END_PERIOD);
    }

    @Override
    protected void setupViewListeners() {
        getBtnEdit().setEnabled(false);
        getBtnEdit().addActionListener(e -> editJournalEntry.accept(journalEntryId));
        getBtnFilter().addActionListener(e -> {
            if (!user.isAuthorized()) return;
            loadData();
            setSelected(null);
            getBtnEdit().setEnabled(false);
        });
        getBtnResetStart().addActionListener(e -> {
            setStartPeriod();
            clearView();
        });
        getBtnResetEnd().addActionListener(e -> {
            setEndPeriod();
            clearView();
        });
        super.setupViewListeners();
    }

    @Override
    protected void setAuditoria() {
        SwingUtilities.invokeLater(() -> {
            getAuditablePanel().getLblCreateAt().setText(getSelected().getCreatedAt() == null ? NA : getSelected().getCreatedAt().format(DATE_FORMATTER));
            getAuditablePanel().getLblCreateBy().setText(getSelected().getCreatedBy() == null ? NA : getSelected().getCreatedBy());
            getAuditablePanel().getLblUpdateAt().setText(getSelected().getUpdatedAt() == null ? NA : getSelected().getUpdatedAt().format(DATE_FORMATTER));
            getAuditablePanel().getLblUpdateBy().setText(getSelected().getUpdatedBy() == null ? NA : getSelected().getUpdatedBy());
            getAuditablePanel().revalidate();
            getAuditablePanel().repaint();
        });
    }

    public void clearView() {
        setSelected(null);
        getBtnEdit().setEnabled(false);
        setData(List.of());
        loadData();
    }

    @Override
    public BusinessView getView() {
        return (BusinessView) super.getView();
    }

    public LocalDateSpinner getSpnStart() {
        return getView().getSpnStart();
    }

    public LocalDateSpinner getSpnEnd() {
        return getView().getSpnEnd();
    }

    public JButton getBtnFilter() {
        return getView().getBtnFilter();
    }

    public JButton getBtnResetStart() {
        return getView().getBtnResetStart();
    }

    public JButton getBtnResetEnd() {
        return getView().getBtnResetEnd();
    }
}