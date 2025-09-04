package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.domain.model.JournalEntryPK;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.controller.business.dto.TrialBalanceTableDTO;
import com.nutrehogar.sistemacontable.application.repository.JournalEntryRepository;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.application.view.business.TrialBalanceView;
import com.nutrehogar.sistemacontable.infrastructure.report.TrialBalance;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.SimpleReportDTO;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.TrialBalanceReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.nutrehogar.sistemacontable.application.config.Util.*;

@Slf4j
public class TrialBalanceController extends BusinessController<TrialBalanceTableDTO, JournalEntry> {

    public TrialBalanceController(JournalEntryRepository repository, TrialBalanceView view, Consumer<JournalEntryPK> editJournalEntry, ReportService reportService, User user) {
        super(repository, view, editJournalEntry, reportService, user);
    }

    @Override
    protected void initialize() {
        setTblModel(new CustomTableModel("Fecha", "Comprobante", "Tipo Documento", "Cuenta", "Referencia", "Debíto", "Crédito", "Saldo") {
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                var dto = getData().get(rowIndex);
                return switch (columnIndex) {
                    case 0 -> dto.getJournalDate();
                    case 1 -> dto.getVoucher();
                    case 2 -> dto.getDocumentType();
                    case 3 -> Account.getCellRenderer(dto.getAccountId());
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
                    case 1 -> Integer.class;
                    case 2 -> DocumentType.class;
                    case 3, 4, 5 -> String.class;
                    case 6, 7, 8 -> BigDecimal.class;
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
                var dtos = new ArrayList<TrialBalanceReportDTO>();
                data.forEach(t -> dtos.add(toDTO(t)));
                var simpleReportDTO = new SimpleReportDTO<>(spnModelStartPeriod.getValue(), spnModelEndPeriod.getValue(), dtos);
                reportService.generateReport(TrialBalance.class, simpleReportDTO);
                showMessage("Reporte generado!");
            } catch (RepositoryException ex) {
                showError("Error al crear el Reporte.", ex);
            }
        });
    }

    public TrialBalanceReportDTO toDTO(TrialBalanceTableDTO t) {
        if (t == null) t = new TrialBalanceTableDTO();
        return new TrialBalanceReportDTO(toStringSafe(t.getJournalDate()), toStringSafe(t.getDocumentType(), DocumentType::getName), toStringSafe(t.getAccountId(), Account::getCellRenderer), toStringSafe(t.getVoucher()), toStringSafe(t.getReference()), formatDecimalSafe(t.getDebit()), formatDecimalSafe(t.getCredit()), formatDecimalSafe(t.getBalance()));
    }


    @Override
    public void loadData() {
        new TrailBalanceDataLoader().execute();
    }

    public class TrailBalanceDataLoader extends DataLoader {

        @Override
        protected List<TrialBalanceTableDTO> doInBackground() {
            var trialBalanceList = getRepository().findAllByDateRange(spnModelStartPeriod.getValue(), spnModelEndPeriod.getValue()).stream().flatMap(journalEntry -> journalEntry.getLedgerRecords().stream().map(ledgerRecord -> new TrialBalanceTableDTO(ledgerRecord.getCreatedBy(), ledgerRecord.getUpdatedBy(), ledgerRecord.getCreatedAt(), ledgerRecord.getUpdatedAt(), journalEntry.getId(), journalEntry.getDate(), journalEntry.getId().getDocumentType(), ledgerRecord.getAccount().getId(), ledgerRecord.getAccount().getName(), ledgerRecord.getAccount().getAccountSubtype().getAccountType(), journalEntry.getId().getDocumentNumber(), ledgerRecord.getReference(), ledgerRecord.getDebit(), ledgerRecord.getCredit(), BigDecimal.ZERO))).toList();

            Map<Integer, List<TrialBalanceTableDTO>> groupedByAccount = trialBalanceList.stream().collect(Collectors.groupingBy(TrialBalanceTableDTO::getAccountId, TreeMap::new, // Usa un TreeMap para que las claves (accountId) estén ordenadas
                    Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(TrialBalanceTableDTO::getJournalDate)) // Ordenar por fecha
                            .toList())));

            var list = new ArrayList<TrialBalanceTableDTO>();

            groupedByAccount.forEach((accountId, balanceList) -> {
                BigDecimal balance = BigDecimal.ZERO;
                BigDecimal debitSum = BigDecimal.ZERO;
                BigDecimal creditSum = BigDecimal.ZERO;
                List<TrialBalanceTableDTO> processedList = new ArrayList<>();

                for (TrialBalanceTableDTO dto : balanceList) {
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
                TrialBalanceTableDTO totalDTO = new TrialBalanceTableDTO("TOTAL", // referencia
                        debitSum, // suma debe
                        creditSum, // suma haber
                        balance // diferencia final
                );
                processedList.add(totalDTO);

                // Agregar una línea en blanco para separación visual
//            processedList.add(new TrialBalanceDTO("", null, null, null));

                // 3️⃣ Agregar la lista procesada al mapa final
                list.addAll(processedList);
            });
            return list;
        }
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
            getBtnEdit().setEnabled(user.isAuthorized());
            setJournalEntryId(selected.getJournalId());
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
