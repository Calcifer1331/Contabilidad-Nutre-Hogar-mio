package com.nutrehogar.sistemacontable.application.controller.crud;

import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.application.repository.AccountRepository;
import com.nutrehogar.sistemacontable.application.repository.AccountSubtypeRepository;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.components.CustomComboBoxModel;
import com.nutrehogar.sistemacontable.ui.components.CustomListCellRenderer;
import com.nutrehogar.sistemacontable.ui.components.DocumentSizeFilter;
import com.nutrehogar.sistemacontable.application.view.crud.AccountView;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountController extends CRUDController<Account, Integer> {
    private final AccountSubtypeRepository subtypeRepository;
    private CustomComboBoxModel<AccountType> cbxModelAccountType;
    private CustomComboBoxModel<AccountSubtype> cbxModelSubtype;
    private DocumentSizeFilter documentSizeFilter;

    public AccountController(AccountRepository repository, AccountView view, AccountSubtypeRepository subtypeRepository, ReportService reportService, User user) {
        super(repository, view, reportService, user);
        this.subtypeRepository = subtypeRepository;
        loadDataSubtype();
        setTextToLbAccountTypeId();
        setTextToLbAccountSubtypeId();
        prepareToAdd();
    }

    private void loadDataSubtype() {
        var accountType = cbxModelAccountType.getSelectedItem();
        List<AccountSubtype> list = subtypeRepository.findAllByAccountType(accountType != null ? accountType : AccountType.ASSETS);
        cbxModelSubtype.setData(list);
    }

    @Override
    protected void initialize() {
        setTblModel(new AccountTableModel());
        cbxModelAccountType = new CustomComboBoxModel<>(AccountType.values());
        cbxModelSubtype = new CustomComboBoxModel<>(List.of());
        documentSizeFilter = new DocumentSizeFilter(Account.MAX_CANONICAL_ID_LENGTH);
        super.initialize();
    }


    @Override
    protected void setupViewListeners() {
        super.setupViewListeners();
        getCbxAccountType().setRenderer(new CustomListCellRenderer());
        getCbxAccountSubtype().setRenderer(new CustomListCellRenderer());
        getCbxAccountType().setModel(cbxModelAccountType);
        getCbxAccountSubtype().setModel(cbxModelSubtype);
        getCbxAccountType().addItemListener(e -> {
            loadDataSubtype();
            setTextToLbAccountTypeId();
        });
        getCbxAccountSubtype().addItemListener(e -> {
            setTextToLbAccountSubtypeId();
        });
        ((PlainDocument) getTxtAccountId().getDocument()).setDocumentFilter(documentSizeFilter);
    }

    private void setTextToLbAccountTypeId() {
        if (cbxModelAccountType.getSelectedItem() == null) return;
        var id = cbxModelAccountType.getSelectedItem().getId();
        getView().getLblAccountTypeId().setText(id + ".");
    }

    private void setTextToLbAccountSubtypeId() {
        if (cbxModelSubtype.getSelectedItem() == null) return;
        var id = cbxModelSubtype.getSelectedItem().getCanonicalId();
        getView().getLblAccountSubtypeId().setText(id);
        int size = Account.MAX_ID_LENGTH - cbxModelSubtype.getSelectedItem().getId().toString().length();
        documentSizeFilter.setMaxCaracteres(size);
        getTxtAccountId().setText(getTxtAccountId().getText().length() <= size ? getTxtAccountId().getText() : "");
    }

    @Override
    protected void prepareToEdit() {
        super.prepareToEdit();
        getTxtAccountName().setText(getSelected().getName());
        getTxtAccountId().setText(getSelected().getCanonicalId());
        getTxtAccountId().setEnabled(false);
        getCbxAccountType().setEnabled(false);
        getCbxAccountSubtype().setEnabled(false);
        AccountType accountType = getSelected().getAccountSubtype().getAccountType();
        getCbxAccountType().setSelectedItem(accountType != null ? accountType : AccountType.ASSETS);
        cbxModelSubtype.setData(subtypeRepository.findAllByAccountType(accountType != null ? accountType : AccountType.ASSETS));
        getCbxAccountSubtype().setSelectedItem(getSelected().getAccountSubtype());
    }

    @Override
    protected void prepareToAdd() {
        super.prepareToAdd();
        getTxtAccountId().setEnabled(true);
        getCbxAccountType().setEnabled(true);
        getCbxAccountSubtype().setEnabled(true);
        getTxtAccountName().setText("");
        getTxtAccountId().setText("");
        getCbxAccountType().setSelectedItem(AccountType.ASSETS);
        loadDataSubtype();
    }

    @Override
    protected Integer prepareToDelete() {
        return getSelected().getId();
    }


    @Override
    protected Account prepareToSave() {
        int id;
        try {
            id = Integer.parseInt(getTxtAccountId().getText());
        } catch (NumberFormatException e) {
            showMessage("El Código tiene que ser un numero.");
            return null;
        }
        if (cbxModelAccountType.getSelectedItem() == null || cbxModelSubtype.getSelectedItem() == null || getTxtAccountName().getText().isBlank()) {
            showMessage("Ningun campo puede estar vacio.");
            return null;
        }

        var account = new Account(user);
        account.setAccountSubtype(cbxModelSubtype.getSelectedItem());
        try {
            account.setId(id);
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage());
            return null;
        }

        if (getRepository().existsById(account.getId())) {
            showMessage("Ya existe una cuenta con el Código: " + account.getId());
            return null;
        }

        account.setName(getTxtAccountName().getText());
        return account;
    }

    @Override
    protected Account prepareToUpdate() {
        if (getTxtAccountName().getText().isBlank()) {
            showMessage("Ningun campo puede estar vacio.");
            return null;
        }
        getSelected().setName(getTxtAccountName().getText());
        getSelected().setUser(user);
        return getSelected();
    }

    public class AccountTableModel extends AbstractTableModel {

        private final String[] COLUMN_NAMES =
                {
                        "Código", "Nombre", "Tipo de Cuenta", "Subtipo de Cuenta"
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
                case 0 -> dto.getFormattedId();
                case 1 -> dto.getName();
                case 2 -> dto.getAccountSubtype().getAccountType();
                case 3 -> dto.getAccountSubtype().getName();
                default -> "que haces?";
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 2 -> AccountType.class;
                default -> String.class;
            };
        }
    }


    @Override
    public AccountView getView() {
        return (AccountView) super.getView();
    }

    @Override
    public AccountRepository getRepository() {
        return (AccountRepository) super.getRepository();
    }

    public JComboBox<AccountSubtype> getCbxAccountSubtype() {
        return getView().getCbxAccountSubtype();
    }

    public JComboBox<AccountType> getCbxAccountType() {
        return getView().getCbxAccountType();
    }

    public JTextField getTxtAccountId() {
        return getView().getTxtAccountId();
    }

    public JTextField getTxtAccountName() {
        return getView().getTxtAccountName();
    }
}
