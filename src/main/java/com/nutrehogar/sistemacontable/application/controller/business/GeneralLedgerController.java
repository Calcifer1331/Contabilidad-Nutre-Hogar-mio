package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.dto.GeneralLedgerDTO;
import com.nutrehogar.sistemacontable.application.repository.AccountRepository;
import com.nutrehogar.sistemacontable.application.repository.AccountSubtypeRepository;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.components.CustomComboBoxModel;
import com.nutrehogar.sistemacontable.ui.components.CustomListCellRenderer;
import com.nutrehogar.sistemacontable.application.view.business.GeneralLedgerView;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class GeneralLedgerController extends BusinessController<GeneralLedgerDTO, Account> {
    private final AccountSubtypeRepository subtypeRepository;
    private CustomComboBoxModel<AccountType> cbxModelAccountType;
    private CustomComboBoxModel<Account> cbxModelAccount;
    private CustomComboBoxModel<AccountSubtype> cbxModelSubtype;

    public GeneralLedgerController(AccountRepository repository, GeneralLedgerView view, Consumer<Integer> editJournalEntry, AccountSubtypeRepository subtypeRepository, ReportService reportService, User user) {
        super(repository, view, editJournalEntry, reportService, user);
        this.subtypeRepository = subtypeRepository;
        loadDataSubtype();
    }

    private void loadDataSubtype() {
        var accountType = cbxModelAccountType.getSelectedItem();
        if (accountType == null) return;
        List<AccountSubtype> list = subtypeRepository.findAllByAccountType(accountType);
        cbxModelSubtype.setData(list);
    }

    private void loadDataAccount() {
        var accountSubtype = cbxModelSubtype.getSelectedItem();
        assert accountSubtype != null;
        List<Account> list = accountSubtype.getAccounts();
        cbxModelAccount.setData(list);
    }

    @Override
    protected void initialize() {
        setTblModel(new GeneralLedgerTableModel());
        cbxModelAccountType = new CustomComboBoxModel<>(AccountType.values());
        cbxModelSubtype = new CustomComboBoxModel<>(List.of());
        cbxModelAccount = new CustomComboBoxModel<>(List.of());
        super.initialize();
    }

    @Override
    protected void loadData() {
        var account = cbxModelAccount.getSelectedItem();
        if (account == null) {
            return;
        }
        Integer accountId = account.getId();
        if (accountId == null) {
            return;
        }
        getData().clear();
        var generalLedgers = new ArrayList<GeneralLedgerDTO>();
        for (var record : account.getLedgerRecords()) {
            generalLedgers.add(new GeneralLedgerDTO(
                    record.getCreatedBy(),
                    record.getUpdatedBy(),
                    record.getCreatedAt(),
                    record.getUpdatedAt(),
                    record.getJournalEntry().getId(),
                    record.getJournalEntry().getDate(),
                    record.getJournalEntry().getName(),
                    record.getDocumentType(),
                    account.getId(),
                    account.getAccountSubtype().getAccountType(),
                    record.getVoucher(),
                    record.getReference(),
                    record.getDebit(),
                    record.getCredit(),
                    BigDecimal.ZERO
            ));
        }

        var balance = BigDecimal.ZERO;
        var debitSum = BigDecimal.ZERO;
        var creditSum = BigDecimal.ZERO;

        for (GeneralLedgerDTO dto : generalLedgers) {
            balance = dto.getAccountType().getBalance(balance, dto.getCredit(), dto.getDebit());
            debitSum = debitSum.add(dto.getDebit(), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
            creditSum = creditSum.add(dto.getCredit(), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
            dto.setBalance(balance);
        }

        var totalDTO = new GeneralLedgerDTO("TOTAL", // referencia
                debitSum, // suma debe
                creditSum, // suma haber
                balance // diferencia
        );
        generalLedgers.add(totalDTO);
        setData(generalLedgers);
        super.loadData();
    }

    @Override
    protected void setupViewListeners() {
        super.setupViewListeners();
        getCbxAccountType().setRenderer(new CustomListCellRenderer());
        getCbxAccountSubtype().setRenderer(new CustomListCellRenderer());
        getCbxAccount().setRenderer(new CustomListCellRenderer());
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
//        getCbxAccountType().addItemListener(e -> {
//            System.out.println("1 - AccountType ,Item: " + cbxModelAccountType.getSelectedItem());
//            loadDataSubtype();
//        });
//        getCbxAccountSubtype().addItemListener(e -> {
//            System.out.println("2 - AccountSubtype ,Item: " + cbxModelSubtype.getSelectedItem());
//            loadDataAccount();
//        });
//        getCbxAccount().addItemListener(e -> {
//            System.out.println("3 - Account ,Item: " + cbxModelAccount.getSelectedItem());
//            loadData();
//        });
    }

    @Override
    protected void setElementSelected(@NotNull MouseEvent e) {
        int row = getTblData().rowAtPoint(e.getPoint());
        if (row != -1) {
            int selectedRow = getTblData().getSelectedRow();
            if (selectedRow >= 0 && selectedRow < getData().size() - 1) {
                setSelected(getData().get(selectedRow));
                setAuditoria();
                getBtnEdit().setEnabled(true);
                setJournalEntryId(getSelected().getEntryId());
            } else {
                getBtnEdit().setEnabled(false);
            }
        }
    }

    public class GeneralLedgerTableModel extends AbstractTableModel {

        private final String[] COLUMN_NAMES =
                {
                        "Fecha", "Nombre de Asiento", "Tipo Documento", "Cuenta", "Comprobante", "Referencia", "Debíto", "Crédito", "Saldo"
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
                case 1 -> dto.getEntryName();
                case 2 -> dto.getDocumentType();
                case 3 -> Account.getCellRenderer(dto.getAccountId());
                case 4 -> dto.getVoucher();
                case 5 -> dto.getReference();
                case 6 -> dto.getDebit();
                case 7 -> dto.getCredit();
                case 8 -> dto.getBalance();
                default -> null;
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> LocalDate.class;
                case 2 -> DocumentType.class;
                case 1, 3, 4, 5 -> String.class;
                case 6, 7, 8 -> BigDecimal.class;
                default -> Object.class;
            };
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
