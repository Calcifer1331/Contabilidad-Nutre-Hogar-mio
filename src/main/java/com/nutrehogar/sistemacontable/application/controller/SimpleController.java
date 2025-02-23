package com.nutrehogar.sistemacontable.application.controller;


import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.repository.SimpleRepository;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.JComponents.AuditablePanel;
import com.nutrehogar.sistemacontable.ui.components.CustomTableCellRenderer;
import com.nutrehogar.sistemacontable.ui.view.SimpleView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public abstract class SimpleController<T, R> extends Controller {
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
    protected final SimpleRepository<R> repository;
    protected List<T> data = new ArrayList<>();
    protected T selected;
    protected AbstractTableModel tblModel;
    protected ReportService reportService;
    protected final User user;

    public SimpleController(SimpleRepository<R> repository, SimpleView view, ReportService reportService, User user) {
        super(view);
        this.repository = repository;
        this.reportService = reportService;
        this.user = user;
        initialize();
    }

    @Override
    protected void initialize() {
        getTblData().setModel(getTblModel());
        getTblData().setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        getTblData().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadData();
        setupViewListeners();
    }

    protected void loadData() {
        updateView();
    }

    @Override
    protected void setupViewListeners() {
        getTblData().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setElementSelected(e);
            }
        });
    }

    protected void updateView() {
        SwingUtilities.invokeLater(getTblModel()::fireTableDataChanged);
    }

    protected abstract void setElementSelected(@NotNull MouseEvent e);

    protected abstract void setAuditoria();

    @Override
    public SimpleView getView() {
        return (SimpleView) super.getView();
    }

    public JTable getTblData() {
        return getView().getTblData();
    }

    public JButton getBtnEdit() {
        return getView().getBtnEdit();
    }

    public AuditablePanel getAuditablePanel() {
        return getView().getAuditablePanel();
    }
}