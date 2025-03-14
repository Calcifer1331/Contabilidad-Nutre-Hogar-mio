package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.domain.model.JournalEntryPK;
import com.nutrehogar.sistemacontable.exception.ApplicationException;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.infrastructure.report.Journal;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.controller.business.dto.JournalTableDTO;
import com.nutrehogar.sistemacontable.application.repository.JournalEntryRepository;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.application.view.business.JournalView;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.JournalReportDTO;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.SimpleReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static com.nutrehogar.sistemacontable.application.config.Util.*;

@Slf4j
public class JournalController extends BusinessController<JournalTableDTO, JournalEntry> {
    public JournalController(JournalEntryRepository repository, JournalView view, Consumer<JournalEntryPK> editJournalEntry, ReportService reportService, User user) {
        super(repository, view, editJournalEntry, reportService, user);
    }

    @Override
    protected void initialize() {
        setTblModel(new CustomTableModel("Fecha", "Comprobante", "Tipo Documento", "Cuenta", "Referencia", "Debíto", "Crédito") {
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
                    case 1 -> Integer.class;
                    case 2 -> DocumentType.class;
                    case 3, 4 -> String.class;
                    case 5, 6 -> BigDecimal.class;
                    default -> Object.class;
                };
            }
        });
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
        new JournalDataLoader().execute();
    }
    public class JournalDataLoader extends DataLoader {
        @Override
        protected List<JournalTableDTO> doInBackground() {
            return getRepository().findAllByDateRange(spnModelStartPeriod.getValue(), spnModelEndPeriod.getValue())
                    .stream()
                    .flatMap(journalEntry -> journalEntry.getLedgerRecords().stream()
                            .map(ledgerRecord -> new JournalTableDTO(
                                    ledgerRecord.getCreatedBy(),
                                    ledgerRecord.getUpdatedBy(),
                                    ledgerRecord.getCreatedAt(),
                                    ledgerRecord.getUpdatedAt(),
                                    ledgerRecord.getJournalEntry().getId(),
                                    ledgerRecord.getJournalEntry().getDate(),
                                    ledgerRecord.getJournalEntry().getId().getDocumentType(),
                                    ledgerRecord.getAccount().getId(),
                                    ledgerRecord.getJournalEntry().getId().getDocumentNumber(),
                                    ledgerRecord.getReference(),
                                    ledgerRecord.getDebit(),
                                    ledgerRecord.getCredit()
                            ))
                    )
                    .sorted(Comparator.comparing(JournalTableDTO::getEntryDate))
                    .toList();
        }
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

    @Override
    public JournalView getView() {
        return (JournalView) super.getView();
    }

    @Override
    public JournalEntryRepository getRepository() {
        return (JournalEntryRepository) super.getRepository();
    }
}
