package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.application.repository.LedgerRecordRepository;
import com.nutrehogar.sistemacontable.domain.model.JournalEntryPK;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.infrastructure.report.GeneralLedgerReport;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.controller.business.dto.GeneralLedgerTableDTO;
import com.nutrehogar.sistemacontable.application.repository.AccountRepository;
import com.nutrehogar.sistemacontable.application.repository.AccountSubtypeRepository;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.GeneralLedgerDTOReport;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.GeneralLedgerReportDTO;
import com.nutrehogar.sistemacontable.ui.components.AccountListCellRenderer;
import com.nutrehogar.sistemacontable.ui.components.CustomComboBoxModel;
import com.nutrehogar.sistemacontable.ui.components.CustomListCellRenderer;
import com.nutrehogar.sistemacontable.application.view.business.GeneralLedgerView;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.nutrehogar.sistemacontable.application.config.Util.*;

@Slf4j
public class GeneralLedgerController extends BusinessController<GeneralLedgerTableDTO, Account> {
    private final AccountSubtypeRepository subtypeRepository;
    private final LedgerRecordRepository ledgerRecordRepository;
    private CustomComboBoxModel<AccountType> cbxModelAccountType;
    private CustomComboBoxModel<Account> cbxModelAccount;
    private CustomComboBoxModel<AccountSubtype> cbxModelSubtype;

    public GeneralLedgerController(AccountRepository repository, GeneralLedgerView view, Consumer<JournalEntryPK> editJournalEntry, AccountSubtypeRepository subtypeRepository, LedgerRecordRepository ledgerRecordRepository, ReportService reportService, User user) {
        super(repository, view, editJournalEntry, reportService, user);
        this.ledgerRecordRepository = ledgerRecordRepository;
        this.subtypeRepository = subtypeRepository;
        loadDataSubtype();
    }


    public void loadDataSubtype() {
        var accountType = cbxModelAccountType.getSelectedItem();
        if (accountType == null) return;
        List<AccountSubtype> list = subtypeRepository.findAllByAccountType(accountType);
        cbxModelSubtype.setData(list);
    }

    public void loadDataAccount() {
        var accountSubtype = cbxModelSubtype.getSelectedItem();
        assert accountSubtype != null;
        Hibernate.initialize(accountSubtype.getAccounts());
        List<Account> list = accountSubtype.getAccounts();
        cbxModelAccount.setData(list);
    }

    @Override
    protected void initialize() {
        setTblModel(new CustomTableModel("Fecha", "Comprobante", "Tipo Documento", "Cuenta", "Referencia", "Debíto", "Crédito", "Saldo") {
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
                    case 7 -> dto.getBalance();
                    default -> null;
                };
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> LocalDate.class;
                    case 1 -> Integer.class;
                    case 2 -> DocumentType.class;
                    case 3, 4 -> String.class;
                    case 5, 6, 7 -> BigDecimal.class;
                    default -> Object.class;
                };
            }
        });
        cbxModelAccountType = new CustomComboBoxModel<>(AccountType.values());
        cbxModelSubtype = new CustomComboBoxModel<>(List.of());
        cbxModelAccount = new CustomComboBoxModel<>(List.of());
        super.initialize();
    }

    @Override
    protected void setupViewListeners() {
        super.setupViewListeners();
        getCbxAccountType().setRenderer(new CustomListCellRenderer());
        getCbxAccountSubtype().setRenderer(new CustomListCellRenderer());
        getCbxAccount().setRenderer(new AccountListCellRenderer());
        getCbxAccountType().setModel(cbxModelAccountType);
        getCbxAccountSubtype().setModel(cbxModelSubtype);
        getCbxAccount().setModel(cbxModelAccount);
        cbxModelAccountType.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                loadDataSubtype();
            }
        });
        cbxModelSubtype.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                loadDataAccount();
            }
        });
        cbxModelAccount.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                loadData();
            }
        });
        getBtnGenerateReport().addActionListener(e -> {
            try {
                var dtos = new ArrayList<GeneralLedgerReportDTO>();
                data.forEach(t -> dtos.add(new GeneralLedgerReportDTO(
                        toStringSafe(t.getEntryId()), toStringSafe(t.getEntryDate()), toStringSafe(t.getDocumentType(), DocumentType::getName), toStringSafe(t.getAccountId()), toStringSafe(t.getAccountType(), AccountType::getName), toStringSafe(t.getVoucher()), toStringSafe(t.getReference()), formatDecimalSafe(t.getDebit()), formatDecimalSafe(t.getCredit()), formatDecimalSafe(t.getBalance())
                )));
                var dto = new GeneralLedgerDTOReport(spnModelStartPeriod.getValue(),
                        spnModelEndPeriod.getValue(),
                        Account.getCellRenderer(cbxModelAccount.getSelectedItem().getId()) + " " + cbxModelAccount.getSelectedItem().getName(),
                        dtos);
                reportService.generateReport(GeneralLedgerReport.class, dto);
                showMessage("Reporte generado!");
            } catch (RepositoryException ex) {
                showError("Error al crear el Reporte.", ex);
            }
        });
    }

    @Override
    public void loadData() {
        new GeneralLedgerDataLoader().execute();
    }

    public class GeneralLedgerDataLoader extends DataLoader {

        @Override
        protected List<GeneralLedgerTableDTO> doInBackground() {
            //        log.info("Load data");
            var accountSelectedItem = cbxModelAccount.getSelectedItem();
            if (accountSelectedItem == null) return null;
//        log.info("account: {}", accountSelectedItem);

            var ledgerRecords = ledgerRecordRepository.findByDateRangeAndAccount(accountSelectedItem, spnModelStartPeriod.getValue(), spnModelEndPeriod.getValue());

            // 🔹 Usar Stream para mapear, ordenar y calcular totales
            List<GeneralLedgerTableDTO> generalLedgers = ledgerRecords.stream()
                    .map(record -> new GeneralLedgerTableDTO(
                            record.getCreatedBy(),
                            record.getUpdatedBy(),
                            record.getCreatedAt(),
                            record.getUpdatedAt(),
                            record.getJournalEntry().getId(),
                            record.getJournalEntry().getDate(),
                            record.getJournalEntry().getId().getDocumentType(),
                            record.getAccount().getId(),
                            record.getAccount().getAccountSubtype().getAccountType(),
                            record.getJournalEntry().getId().getDocumentNumber(),
                            record.getReference(),
                            record.getDebit(),
                            record.getCredit(),
                            BigDecimal.ZERO
                    ))
                    .sorted(Comparator.comparing(GeneralLedgerTableDTO::getEntryDate)) // Ordenar por fecha
                    .toList();
//        log.info("GeneralLedgerDTOs: {}", generalLedgers);
            // 🔹 Calcular totales usando reduce()
            var debitSum = generalLedgers.stream()
                    .map(GeneralLedgerTableDTO::getDebit)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            var creditSum = generalLedgers.stream()
                    .map(GeneralLedgerTableDTO::getCredit)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            // 🔹 Calcular balances
            var balance = BigDecimal.ZERO;
            for (GeneralLedgerTableDTO dto : generalLedgers) {
                balance = dto.getAccountType().getBalance(balance, dto.getCredit(), dto.getDebit());
                dto.setBalance(balance);
            }

            // 🔹 Agregar total al final de la lista
            var totalDTO = new GeneralLedgerTableDTO("TOTAL", debitSum, creditSum, balance);
            generalLedgers = new ArrayList<>(generalLedgers);
            generalLedgers.add(totalDTO);
            return generalLedgers;
        }
    }


    @Override
    protected void setElementSelected(@NotNull MouseEvent e) {
        int row = getTblData().rowAtPoint(e.getPoint());
        if (row != -1) {
            int selectedRow = getTblData().getSelectedRow();
            if (selectedRow >= 0 && selectedRow < getData().size() - 1) {
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
    public GeneralLedgerView getView() {
        return (GeneralLedgerView) super.getView();
    }

    private JComboBox<AccountType> getCbxAccountType() {
        return getView().getCbxAccountType();
    }

    private JComboBox<AccountSubtype> getCbxAccountSubtype() {
        return getView().getCbxAccountSubtype();
    }

    private JComboBox<Account> getCbxAccount() {
        return getView().getCbxAccount();
    }

    @Override
    public AccountRepository getRepository() {
        return (AccountRepository) super.getRepository();
    }
}
