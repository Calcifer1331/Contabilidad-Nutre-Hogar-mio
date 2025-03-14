package com.nutrehogar.sistemacontable.application.controller;


import com.nutrehogar.sistemacontable.application.config.Util;
import com.nutrehogar.sistemacontable.application.controller.business.dto.JournalTableDTO;
import com.nutrehogar.sistemacontable.exception.ApplicationException;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.infrastructure.report.Report;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.repository.SimpleRepository;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.JComponents.AuditablePanel;
import com.nutrehogar.sistemacontable.ui.components.CustomTableCellRenderer;
import com.nutrehogar.sistemacontable.application.view.SimpleView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Getter
@Setter
public abstract class SimpleController<T, R> extends Controller {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
    protected final SimpleRepository<R> repository;
    protected List<T> data = new ArrayList<>();
    protected T selected;
    protected AbstractTableModel tblModel;
    protected ReportService reportService;
    protected final User user;
    protected static final String NA = Util.NA;

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
        getTblData().setDefaultRenderer(BigDecimal.class, new CustomTableCellRenderer());
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

    public abstract class DataLoader extends SwingWorker<List<T>, Void> {

        @Override
        protected void done() {
            try {
                if (get() == null ||get().isEmpty() || get().getFirst() == null) {
                    setData(List.of());
                    return;
                }
                setData(get());
                getTblModel().fireTableDataChanged();
            } catch (Exception e) {
                showError("Error al cargar datos", new ApplicationException("Failure to find", e));
            }
        }
    }

    protected abstract void setElementSelected(@NotNull MouseEvent e);

    protected abstract void setAuditoria();

    public abstract class CustomTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES;

        public CustomTableModel(@NotNull String... COLUMN_NAMES) {
            this.COLUMN_NAMES = COLUMN_NAMES;
        }

        @Override
        public int getRowCount() {
            return getData().size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }
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

    public JButton getBtnGenerateReport() {
        return getView().getBtnGenerateReport();
    }
}