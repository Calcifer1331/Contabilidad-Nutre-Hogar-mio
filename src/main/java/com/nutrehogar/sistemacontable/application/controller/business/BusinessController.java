package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.application.controller.SimpleController;
import com.nutrehogar.sistemacontable.application.repository.business.BusinessRepository;
import com.nutrehogar.sistemacontable.ui.components.LocalDateSpinner;
import com.nutrehogar.sistemacontable.ui.components.LocalDateSpinnerModel;
import com.nutrehogar.sistemacontable.ui.view.business.BusinessView;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.time.LocalDate;
import java.util.function.Consumer;

import static com.nutrehogar.sistemacontable.ui.components.LocalDateSpinnerModel.CURRENT_DATE;

@Getter
@Setter
public abstract class BusinessController<T> extends SimpleController<T> {
    public static final LocalDate END_PERIOD = LocalDate.of(CURRENT_DATE.getYear(), 12, 31);
    public static final LocalDate START_PERIOD = LocalDate.of(CURRENT_DATE.getYear(), 1, 1);

    private LocalDateSpinnerModel spnModelStartPeriod;
    private LocalDateSpinnerModel spnModelEndPeriod;
    private final Consumer<Integer> editJournalEntry;
    private Integer journalEntryId;


    public BusinessController(BusinessRepository<T> repository, BusinessView view, Consumer<Integer> editJournalEntry) {
        super(repository, view);
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
        getBtnEdit().addActionListener(e-> editJournalEntry.accept(journalEntryId));
        getBtnFilter().addActionListener(e -> {
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

    public void clearView() {
        setSelected(null);
        getBtnEdit().setEnabled(false);
        getData().clear();
        loadData();
    }


    @Override
    public BusinessView getView() {
        return (BusinessView) super.getView();
    }

    @Override
    public BusinessRepository<T> getRepository() {
        return (BusinessRepository<T>) super.getRepository();
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