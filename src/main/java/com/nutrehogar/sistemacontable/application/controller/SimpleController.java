package com.nutrehogar.sistemacontable.application.controller;


import com.nutrehogar.sistemacontable.application.controller.service.BackupController;
import com.nutrehogar.sistemacontable.application.repository.SimpleRepository;
import com.nutrehogar.sistemacontable.domain.model.AuditableEntity;
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
public abstract class SimpleController<T> extends Controller {
    private final SimpleRepository<T> repository;
    private List<T> data = new ArrayList<>();
    private T selected;
    private AbstractTableModel tblModel;

    public SimpleController(SimpleRepository<T> repository, SimpleView view) {
        super(view);
        this.repository = repository;
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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    protected <K extends AuditableEntity> void setAuditoria(K auditoria) {
        SwingUtilities.invokeLater(() -> {
            getAuditablePanel().getLblCreateAt().setText(auditoria.getCreatedAt() == null ? "" : auditoria.getCreatedAt().format(DATE_FORMATTER));
            getAuditablePanel().getLblCreateBy().setText(auditoria.getCreatedBy() == null ? "" : auditoria.getCreatedBy());
            getAuditablePanel().getLblUpdateAt().setText(auditoria.getUpdatedAt() == null ? "" : auditoria.getUpdatedAt().format(DATE_FORMATTER));
            getAuditablePanel().getLblUpdateBy().setText(auditoria.getUpdatedBy() == null ? "" : auditoria.getUpdatedBy());
            getAuditablePanel().revalidate();
            getAuditablePanel().repaint();
        });
    }

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