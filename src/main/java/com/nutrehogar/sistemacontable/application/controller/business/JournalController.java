package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.dto.JournalDTO;
import com.nutrehogar.sistemacontable.application.repository.crud.JournalEntryRepository;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.view.business.JournalView;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Consumer;

public class JournalController extends BusinessController<JournalDTO, JournalEntry> {
    public JournalController(JournalEntryRepository repository, JournalView view, Consumer<Integer> editJournalEntry, ReportService reportService, User user) {
        super(repository, view, editJournalEntry, reportService, user);
    }

    @Override
    protected void initialize() {
        setTblModel(new JournalTableModel());
        super.initialize();
    }

    @Override
    protected void loadData() {
        var list = getRepository().findAllByDateRange(spnModelStartPeriod.getValue(), spnModelEndPeriod.getValue())
                .stream()
                .flatMap(journalEntry -> journalEntry.getLedgerRecords().stream()
                        .map(ledgerRecord -> new JournalDTO(
                                ledgerRecord.getCreatedBy(),
                                ledgerRecord.getUpdatedBy(),
                                ledgerRecord.getCreatedAt(),
                                ledgerRecord.getUpdatedAt(),
                                journalEntry.getId(),
                                journalEntry.getDate(),
                                ledgerRecord.getDocumentType(),
                                ledgerRecord.getAccount().getId(),
                                ledgerRecord.getVoucher(),
                                ledgerRecord.getReference(),
                                ledgerRecord.getDebit(),
                                ledgerRecord.getCredit()
                        ))
                )
                .toList(); // Java 17+, usa `collect(Collectors.toList())` en Java 8-16
        setData(list);
        super.loadData();
    }

    @Override
    protected void setElementSelected(@NotNull MouseEvent e) {
        int row = getTblData().rowAtPoint(e.getPoint());
        if (row != -1) {
            int selectedRow = getTblData().getSelectedRow();
            if (selectedRow >= 0 && selectedRow < getData().size()) {
                setSelected(getData().get(selectedRow));
                setAuditoria();
                getBtnEdit().setEnabled(true);
                setJournalEntryId(getSelected().getEntryId());
            } else {
                getBtnEdit().setEnabled(false);
            }
        }
    }

    public class JournalTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES =
                {
                        "Fecha", "Tipo Documento", "Cuenta", "Comprobante", "Referencia", "Debíto", "Crédito"
                };

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

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var dto = getData().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> dto.getEntryDate();
                case 1 -> dto.getDocumentType();
                case 2 -> Account.getCellRenderer(dto.getAccountId());
                case 3 -> dto.getVoucher();
                case 4 -> dto.getReference();
                case 5 -> dto.getDebit();
                case 6 -> dto.getCredit();
                default -> "Element not found";
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> LocalDate.class;
                case 1 -> DocumentType.class;
                case 2, 3, 4 -> String.class;
                case 5, 6 -> BigDecimal.class;
                default -> Object.class;
            };
        }
    }

    @Override
    public JournalView getView() {

        return (JournalView) super.getView();
    }

    @Override
    public JournalEntryRepository getRepository() {
        return (JournalEntryRepository) super.getRepository();
    }
}
