package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.dto.TrialBalanceDTO;
import com.nutrehogar.sistemacontable.application.repository.JournalEntryRepository;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.application.view.business.TrialBalanceView;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class TrialBalanceController extends BusinessController<TrialBalanceDTO, JournalEntry> {

    public TrialBalanceController(JournalEntryRepository repository, TrialBalanceView view, Consumer<Integer> editJournalEntry, ReportService reportService, User user) {
        super(repository, view, editJournalEntry, reportService, user);
    }

    @Override
    protected void initialize() {
        setTblModel(new TrialBalanceTableModel());
        super.initialize();
    }

    @Override
    protected void loadData() {
        var trialBalanceList = getRepository().findAllByDateRange(spnModelStartPeriod.getValue(), spnModelEndPeriod.getValue())
                .stream()
                .flatMap(journalEntry -> journalEntry.getLedgerRecords().stream()
                        .map(ledgerRecord -> new TrialBalanceDTO(
                                ledgerRecord.getCreatedBy(),
                                ledgerRecord.getUpdatedBy(),
                                ledgerRecord.getCreatedAt(),
                                ledgerRecord.getUpdatedAt(),
                                journalEntry.getId(),
                                journalEntry.getDate(),
                                ledgerRecord.getDocumentType(),
                                ledgerRecord.getAccount().getId(),
                                ledgerRecord.getAccount().getName(),
                                ledgerRecord.getAccount().getAccountSubtype().getAccountType(),
                                ledgerRecord.getVoucher(),
                                ledgerRecord.getReference(),
                                ledgerRecord.getDebit(),
                                ledgerRecord.getCredit(),
                                BigDecimal.ZERO
                        ))
                )
                .toList();

        Map<Integer, List<TrialBalanceDTO>> groupedByAccount = trialBalanceList.stream()
                .collect(Collectors.groupingBy(
                        TrialBalanceDTO::getAccountId,
                        TreeMap::new, // Usa un TreeMap para que las claves (accountId) estén ordenadas
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(TrialBalanceDTO::getJournalDate)) // Ordenar por fecha
                                        .toList()
                        )
                ));

        var list = new ArrayList<TrialBalanceDTO>();

        groupedByAccount.forEach((accountId, balanceList) -> {
            BigDecimal balance = BigDecimal.ZERO;
            BigDecimal debitSum = BigDecimal.ZERO;
            BigDecimal creditSum = BigDecimal.ZERO;
            List<TrialBalanceDTO> processedList = new ArrayList<>();

            for (TrialBalanceDTO dto : balanceList) {
                // Calcular balance
                balance = dto.getAccountType().getBalance(balance, dto.getCredit(), dto.getDebit());

                // Acumular débitos y créditos
                debitSum = debitSum.add(dto.getDebit(), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
                creditSum = creditSum.add(dto.getCredit(), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);

                // Actualizar balance en el DTO y agregar a la lista procesada
                dto.setBalance(balance);
                processedList.add(dto);
            }

            // Agregar el total de la cuenta
            TrialBalanceDTO totalDTO = new TrialBalanceDTO(
                    "TOTAL", // referencia
                    debitSum, // suma debe
                    creditSum, // suma haber
                    balance // diferencia final
            );
            processedList.add(totalDTO);

            // Agregar una línea en blanco para separación visual
            processedList.add(new TrialBalanceDTO("", null, null, null));

            // 3️⃣ Agregar la lista procesada al mapa final
            list.addAll(processedList);
        });

        setData(list);
        super.loadData();
    }

    @Override
    protected void setElementSelected(@NotNull MouseEvent e) {
        int row = getTblData().rowAtPoint(e.getPoint());
        if (row != -1) {
            int selectedRow = getTblData().getSelectedRow();
            if (selectedRow < 0) {
                getBtnEdit().setEnabled(false);
                return;
            }
            var selected = getData().get(selectedRow);
            if (selected.getJournalDate() == null) {
                getBtnEdit().setEnabled(false);
                return;
            }
            setSelected(selected);
            setAuditoria();
            getBtnEdit().setEnabled(true);
            setJournalEntryId(selected.getJournalId());
        }
    }


    public class TrialBalanceTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES =
                {
                        "Fecha", "Tipo Documento", "Cuenta", "Comprobante", "Referencia", "Debíto", "Crédito", "Saldo"
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
                case 0 -> dto.getJournalDate();
                case 1 -> dto.getDocumentType();
                case 2 -> Account.getCellRenderer(dto.getAccountId());
                case 3 -> dto.getVoucher();
                case 4 -> dto.getAccountName();
                case 5 -> dto.getReference();
                case 6 -> dto.getDebit();
                case 7 -> dto.getCredit();
                case 8 -> dto.getBalance();
                default -> "Element not found";
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> LocalDate.class;
                case 1 -> DocumentType.class;
                case 2, 3, 4, 5 -> String.class;
                case 6, 7, 8 -> BigDecimal.class;
                default -> Object.class;
            };
        }
    }

    @Override
    public TrialBalanceView getView() {
        return (TrialBalanceView) super.getView();
    }

    @Override
    public JournalEntryRepository getRepository() {
        return (JournalEntryRepository) super.getRepository();
    }

}
