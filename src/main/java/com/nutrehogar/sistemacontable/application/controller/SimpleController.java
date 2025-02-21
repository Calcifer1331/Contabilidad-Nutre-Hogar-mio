package com.nutrehogar.sistemacontable.application.controller;


import com.nutrehogar.sistemacontable.application.repository.SimpleRepository;
import com.nutrehogar.sistemacontable.domain.model.AuditableEntity;
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

            @Override
            public void mouseMoved(MouseEvent e) {
                setToolTipText(e);
            }
        });
    }

    protected void updateView() {
        SwingUtilities.invokeLater(getTblModel()::fireTableDataChanged);
    }

    protected abstract void setElementSelected(@NotNull MouseEvent e);



    protected void setToolTipText(@NotNull MouseEvent e) {
//        int row = getTblData().rowAtPoint(e.getPoint());
//        log.info("setToolTipText: row={}, pos={}", row, e.getPoint());
//        if (row != -1) {
//            int selectedRow = getTblData().getSelectedRow();
//            if (selectedRow < 0) {
//                return;
//            }
//            log.info("setToolTipText: selectedRow={}", selectedRow);
//            if (getData().get(selectedRow) instanceof AuditableEntity entity) {
//                String tooltip = "<html>Creado por: " + entity.getCreatedBy() + "<br>" +
//                        "Fecha creación: " + entity.getCreatedAt() + "<br>" +
//                        "Actualizado por: " + entity.getUpdatedBy() + "<br>" +
//                        "Fecha actualización: " + entity.getUpdatedAt() + "</html>";
//                getTblData().setToolTipText(tooltip);
//            }
//        }
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



}