package com.nutrehogar.sistemacontable.application.controller.business;

import com.nutrehogar.sistemacontable.application.dto.GeneralLedgerDTO;
import com.nutrehogar.sistemacontable.application.repository.crud.AccountSubtypeRepository;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.helper.OrderDirection;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;
import com.nutrehogar.sistemacontable.domain.repository.GeneralLedgerRepositoryImpl;
import com.nutrehogar.sistemacontable.domain.repository.TrialBalanceRepositoryImpl;
import com.nutrehogar.sistemacontable.ui.components.CustomComboBoxModel;
import com.nutrehogar.sistemacontable.ui.components.CustomListCellRenderer;
import com.nutrehogar.sistemacontable.ui.view.business.GeneralLedgerView;
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
import java.util.List;
import java.util.function.Consumer;

public class GeneralLedgerController extends BusinessController<GeneralLedgerDTO> {
    private final AccountSubtypeRepository subtypeRepository;
    private CustomComboBoxModel<AccountType> cbxModelAccountType;
    private CustomComboBoxModel<Account> cbxModelAccount;
    private CustomComboBoxModel<AccountSubtype> cbxModelSubtype;

    public GeneralLedgerController(GeneralLedgerRepositoryImpl repository, GeneralLedgerView view, Consumer<Integer> editJournalEntry, AccountSubtypeRepository subtypeRepository) {
        super(repository, view, editJournalEntry);
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
        var entity = cbxModelAccount.getSelectedItem();
        if (entity == null) {
            return;
        }
        Integer id = entity.getId();
        if (id == null) {
            return;
        }
        getData().clear();

        var list = getRepository().find(
                GeneralLedgerRepositoryImpl.Field.JOURNAL_ENTRY_DATE,
                OrderDirection.DESCENDING,
                new GeneralLedgerRepositoryImpl.Filter.ByDateRange(
                        getSpnModelStartPeriod().getValue(),
                        getSpnModelEndPeriod().getValue()),
                new GeneralLedgerRepositoryImpl.Filter.ByAccountId(id)
        );

        var balance = BigDecimal.ZERO;
        var debitSum = BigDecimal.ZERO;
        var creditSum = BigDecimal.ZERO;

        for (GeneralLedgerDTO dto : list) {
            balance = dto.getAccountType().getBalance(balance, dto.getLedgerRecordCredit(), dto.getLedgerRecordDebit());
            debitSum = debitSum.add(dto.getLedgerRecordDebit(), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
            creditSum = creditSum.add(dto.getLedgerRecordCredit(), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
            dto.setBalance(balance);
        }

        var totalDTO = new GeneralLedgerDTO("TOTAL", // referencia
                debitSum, // suma debe
                creditSum, // suma haber
                balance // diferencia
        );
        list.add(totalDTO);
        setData(list);
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
                getBtnEdit().setEnabled(true);
                setJournalEntryId(getSelected().getJournalEntryId());
            } else {
                getBtnEdit().setEnabled(false);
            }
        }
    }

    public class GeneralLedgerTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return getData().size();
        }

        @Override
        public int getColumnCount() {
            return TrialBalanceRepositoryImpl.Field.values().length;
        }

        @Override
        public String getColumnName(int column) {
            return TrialBalanceRepositoryImpl.Field.values()[column].getFieldName();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var dto = getData().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> dto.getJournalEntryDate();
                case 1 -> dto.getJournalEntryName();
                case 2 -> dto.getLedgerRecordDocumentType();
                case 3 -> Account.getCellRenderer(dto.getAccountId());
                case 4 -> dto.getLedgerRecordReference();
                case 5 -> dto.getLedgerRecordDebit();
                case 6 -> dto.getLedgerRecordCredit();
                case 7 -> dto.getBalance();
                default -> null;
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> LocalDate.class;
                case 2 -> DocumentType.class;
                case 1, 3, 4 -> String.class;
                case 5, 6, 7 -> BigDecimal.class;
                default -> Object.class;
            };
        }
    }

    @Override
    public GeneralLedgerView getView() {
        return (GeneralLedgerView) super.getView();
    }

    @Override
    public GeneralLedgerRepositoryImpl getRepository() {
        return (GeneralLedgerRepositoryImpl) super.getRepository();
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

}
