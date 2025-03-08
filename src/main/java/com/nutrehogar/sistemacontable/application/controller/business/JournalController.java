package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.infrastructure.report.Journal;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.controller.business.dto.JournalDTO;
import com.nutrehogar.sistemacontable.application.repository.JournalEntryRepository;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.application.view.business.JournalView;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.JournalReportDTO;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.SimpleReportDTO;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;

import static com.nutrehogar.sistemacontable.application.config.Util.*;


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
    protected void setupViewListeners() {
        super.setupViewListeners();
        getBtnGenerateReport().addActionListener(e -> {
            try {
                var journalReportDTOs = new ArrayList<JournalReportDTO>();
                data.forEach(j -> journalReportDTOs.add(
                        new JournalReportDTO(
                                toStringSafe(j.getEntryDate()),
                                toStringSafe(j.getDocumentType(), DocumentType::getName),
                                toStringSafe(j.getAccountId(), Account::getCellRenderer),
                                toStringSafe(j.getVoucher()),
                                toStringSafe(j.getReference()),
                                formatDecimalSafe(j.getDebit()),
                                formatDecimalSafe(j.getCredit()))));
                var simpleReportDTO = new SimpleReportDTO<>(
                        spnModelStartPeriod.getValue(),
                        spnModelEndPeriod.getValue(),
                        journalReportDTOs
                );
                reportService.generateReport(Journal.class, simpleReportDTO);
                showMessage("Reporte generado!");
            } catch (RepositoryException ex) {
                showError("Error al crear el Reporte.", ex);
            }
        });
    }

    @Override
    public void loadData() {
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
                .sorted(Comparator.comparing(JournalDTO::getEntryDate))
                .toList();
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
                getBtnEdit().setEnabled(user.isAuthorized());
                setJournalEntryId(getSelected().getEntryId());
            } else {
                getBtnEdit().setEnabled(false);
            }
        }
    }

    public class JournalTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES =
                {
                        "Fecha", "Comprobante", "Tipo Documento", "Cuenta", "Referencia", "Debíto", "Crédito"
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
                case 1 -> dto.getVoucher();
                case 2 -> dto.getDocumentType();
                case 3 -> Account.getCellRenderer(dto.getAccountId());
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
                case 2 -> DocumentType.class;
                case 1, 3, 4 -> String.class;
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
