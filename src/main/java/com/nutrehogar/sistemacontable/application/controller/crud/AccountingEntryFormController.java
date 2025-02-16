package com.nutrehogar.sistemacontable.application.controller.crud;

import com.nutrehogar.sistemacontable.application.controller.SimpleController;
import com.nutrehogar.sistemacontable.application.controller.service.ReportController;
import com.nutrehogar.sistemacontable.application.dto.JournalEntryDTO;
import com.nutrehogar.sistemacontable.application.dto.LedgerRecordDTO;
import com.nutrehogar.sistemacontable.application.repository.crud.AccountRepository;
import com.nutrehogar.sistemacontable.application.repository.crud.JournalEntryRepository;
import com.nutrehogar.sistemacontable.application.repository.crud.LedgerRecordRepository;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.JournalEntry;
import com.nutrehogar.sistemacontable.domain.model.LedgerRecord;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.ui.components.*;
import com.nutrehogar.sistemacontable.ui.view.crud.AccountingEntryFormView;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectDeletedException;
import org.hibernate.exception.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class AccountingEntryFormController extends SimpleController<LedgerRecord> {
    private final JournalEntryRepository journalRepository;
    private final AccountRepository accountRepository;
    private Optional<JournalEntry> journalEntry;
    private CustomComboBoxModel<Account> cbxModelAccount;
    private CustomComboBoxModel<DocumentType> cbxModelDocumentType;
    private List<LedgerRecordDTO> tblDataList;
    private boolean isBeingAdded;
    private boolean isBeingEdited;
    public static final BigDecimal ZERO = BigDecimal.valueOf(0, 2);
    private ReportController reportController;


    public AccountingEntryFormController(LedgerRecordRepository repository, AccountingEntryFormView view, JournalEntryRepository journalRepository, AccountRepository accountRepository) {
        super(repository, view);
        this.journalRepository = journalRepository;
        this.accountRepository = accountRepository;
        reportController = new ReportController();
        loadDataAccount();
    }

    @Override
    protected void initialize() {
        setTblModel(new LedgerRecordTableModel());
        cbxModelAccount = new CustomComboBoxModel<>(List.of());
        cbxModelDocumentType = new CustomComboBoxModel<>(DocumentType.values());
        journalEntry = Optional.empty();
        tblDataList = new ArrayList<>();
        prepareBtnToAddEntry();
        prepareBtnToAddRecord();
        super.initialize();
    }

    @Override
    protected void loadData() {
        if (journalEntry.isEmpty()) {
            journalEntry = Optional.of(new JournalEntry());
        }
        loadDataAccount();
        setData(journalEntry.get().getLedgerRecords());
        calcBalance();
        super.loadData();
    }

    @Override
    protected void setupViewListeners() {
        super.setupViewListeners();
        getCbxRecordAccount().setModel(cbxModelAccount);
        getCbxRecordDocumentType().setModel(cbxModelDocumentType);
        getCbxRecordDocumentType().setRenderer(new CustomListCellRenderer());
        getCbxRecordAccount().setRenderer(new AccountListCellRenderer());
        getBtnSaveRecord().addActionListener(e -> saveRecord());
        getBtnDeleteRecord().addActionListener(e -> deleteRecord());
        getBtnUpdateRecord().addActionListener(e -> updateRecord());
        getBtnEdit().addActionListener(e -> prepareToEditRecord());
        getBtnAddRecord().addActionListener(e -> prepareToAddRecord());
        getBtnAddEntry().addActionListener(e -> prepareToAddEntry());
        getBtnSaveEntry().addActionListener(e -> saveEntry());
        getBtnDeleteEntry().addActionListener(e -> deleteEntry());
        getBtnUpdateEntry().addActionListener(e -> updateEntry());
        getBtnGeneratePaymentVoucher().addActionListener(e -> {
            try {
                reportController.generateReport(ReportController.ReportType.PAYMENT_VOUCHER, getJournalEntryDTO());
            } catch (Exception ex) {
                log.info(ex.getMessage());
                throw new RuntimeException(ex);
            }
        });
        getBtnGenerateRegistrationForm().addActionListener(e -> {
            try {
                reportController.generateReport(ReportController.ReportType.REGISTRATION_FORM, getJournalEntryDTO());
            } catch (Exception ex) {
                log.info(ex.getMessage());
                throw new RuntimeException(ex);
            }
        });
        ((AbstractDocument) getTxtRecordAmount().getDocument()).setDocumentFilter(new CustomDocumentFilter(CustomDocumentFilter.Type.DECIMAL));
        ((AbstractDocument) getTxtEntryDocumentNumber().getDocument()).setDocumentFilter(new CustomDocumentFilter(CustomDocumentFilter.Type.INTEGER));
    }

    private @Nullable JournalEntryDTO getJournalEntryDTO() {
        AtomicReference<JournalEntryDTO> journalEntryDTO = new AtomicReference<>();
        journalEntry.ifPresentOrElse(entry -> {
            journalEntryDTO.set(new JournalEntryDTO(
                    entry.getId(),
                    entry.getCheckNumber(),
                    entry.getDate(),
                    entry.getName(),
                    entry.getConcept(),
                    tblDataList.getLast().getDebit(),
                    tblDataList));
        }, () -> {

        });
        return journalEntryDTO.get();
    }


    @Override
    protected void updateView() {
        super.updateView();
    }

    @Override
    protected void setElementSelected(@NotNull MouseEvent e) {
        int row = getTblData().rowAtPoint(e.getPoint());
        if (row != -1) {
            int selectedRow = getTblData().getSelectedRow();
            if (selectedRow < 0 || selectedRow + 1 > getData().size()) {
                getBtnDeleteRecord().setEnabled(false);
                getBtnEdit().setEnabled(false);
                return;
            }
            setSelected(getData().get(selectedRow));
            getBtnDeleteRecord().setEnabled(true);
            getBtnEdit().setEnabled(true);
        } else {
            getBtnDeleteRecord().setEnabled(false);
            getBtnEdit().setEnabled(false);
        }
    }

    private void saveRecord() {
        if (journalEntry.isEmpty()) {
            showError("La Entrada esta vacia.");
            return;
        }
        var record = getLedgerRecordByForm(new LedgerRecord());
        if (record.isEmpty()) {
            return;
        }
        journalEntry.get().getLedgerRecords().add(record.get());
        loadData();
    }

    private Optional<LedgerRecord> getLedgerRecordByForm(LedgerRecord lr) {
        if (journalEntry.isEmpty()) {
            showError("La Entrada esta vacia.");
            return Optional.empty();
        }
        Optional<Account> account = Optional.ofNullable(cbxModelAccount.getSelectedItem());
        if (account.isEmpty()) {
            showError("la Cuenta esta vacia.");
            return Optional.empty();
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(getTxtRecordAmount().getText()).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            showMessage("El monto debe ser un numero y no puede estar vacio.");
            return Optional.empty();
        }
        if (lr == null) lr = new LedgerRecord();
        lr.setJournalEntry(journalEntry.get());
        lr.setVoucher(getTxtRecordVoucher().getText());
        lr.setDocumentType(cbxModelDocumentType.getSelectedItem());
        lr.setReference(getTxtRecordReference().getText());
        lr.setAccount(account.get());
        if (getRbtRecordCredit().isSelected()) {
            lr.setCredit(amount);
            lr.setDebit(ZERO);
        } else if (getRbtRecordDebit().isSelected()) {
            lr.setDebit(amount);
            lr.setCredit(ZERO);
        } else {
            showMessage("Debe seleccionar credito o debito.");
            return Optional.empty();
        }
        return Optional.of(lr);
    }

    private @NotNull Optional<JournalEntry> getJournalEntryByForm(@NotNull JournalEntry je) {
        String name = getTxtEntryName().getText();
        if (name.isEmpty()) {
            showMessage("El nombre de la Entrada no puede esta vacia.");
            return Optional.empty();
        }
        if (getData().isEmpty()) {
            showMessage("La Entrada tiene que tener al menos dos registros.");
            return Optional.empty();
        }

        je.setName(name);
        je.setConcept(getTaEntryConcept().getText());
        je.setCheckNumber(getTxtEntryCheckNumber().getText());
        je.setDate(getSpnEntryDate().getValue());
        je.setLedgerRecords(getData());
        for (var record : getData()) {
            record.setJournalEntry(je);
        }
        return Optional.of(je);
    }

    private void deleteRecord() {
        if (journalEntry.isEmpty()) {
            showError("la Entrada esta vacia.");
            return;
        }
        try {
            journalEntry.get().getLedgerRecords().remove(getSelected());
        } catch (Exception e) {
            showError("Error al eliminar el registro.");
            return;
        }
        setSelected(null);
        prepareToAddRecord();
        loadData();
    }

    private void updateRecord() {
        if (journalEntry.isEmpty()) {
            showError("La Entrada esta vacia.");
            return;
        }
        var record = getLedgerRecordByForm(getSelected());
        if (record.isEmpty()) {
            return;
        }
        loadData();
        prepareToAddRecord();
    }

    private void prepareToEditRecord() {
        if (getSelected() == null) {
            showError("El Registro no puede estar vacia.");
            return;
        }

        prepareBtnToEditRecord();

        cbxModelDocumentType.setSelectedItem(getSelected().getDocumentType());
        getTxtRecordVoucher().setText(getSelected().getVoucher());
        getTxtRecordReference().setText(getSelected().getReference());
        cbxModelAccount.setSelectedItem(getSelected().getAccount());

        if (getSelected().getCredit() != null && !getSelected().getCredit().equals(BigDecimal.valueOf(0, 2))) {
            getRbtRecordCredit().setSelected(true);
            getTxtRecordAmount().setText(getSelected().getCredit().toString());
        } else if (getSelected().getDebit() != null && !getSelected().getDebit().equals(BigDecimal.valueOf(0, 2))) {
            getRbtRecordDebit().setSelected(true);
            getTxtRecordAmount().setText(getSelected().getDebit().toString());
        } else {
            showMessage("Debe seleccionar credito o debito.");
        }
    }

    private void prepareToAddRecord() {
        prepareBtnToAddRecord();
        setSelected(null);
        getCbxRecordDocumentType().setSelectedIndex(0);
        if (cbxModelAccount.getSize() > 0) {
            getCbxRecordAccount().setSelectedIndex(0);
        }
        getTxtRecordVoucher().setText("");
        getTxtRecordReference().setText("");
        getRbtRecordDebit().setSelected(true);
        getTxtRecordAmount().setText("");
    }

    private void prepareBtnToEditRecord() {
        getBtnSaveRecord().setEnabled(false);
        getBtnDeleteRecord().setEnabled(true);
        getBtnEdit().setEnabled(true);
        getBtnAddRecord().setEnabled(true);
        getBtnUpdateRecord().setEnabled(true);
    }

    private void prepareBtnToAddRecord() {
        getBtnAddRecord().setEnabled(true);
        getBtnDeleteRecord().setEnabled(false);
        getBtnEdit().setEnabled(false);
        getBtnSaveRecord().setEnabled(true);
        getBtnUpdateRecord().setEnabled(false);
        getRbtRecordDebit().setSelected(true);
    }

    private void saveEntry() {
        if (journalRepository == null) {
            showError("Error: journal repository is null!");
            return;
        }
        JournalEntry entry = new JournalEntry();
        String documentNo = getTxtEntryDocumentNumber().getText();
        if (!documentNo.isBlank()) {
            int id;
            try {
                id = Integer.parseInt(documentNo);
            } catch (NumberFormatException e) {
                showMessage("El Documento No. debe ser un numero.");
                return;
            }
            if (journalRepository.existsById(id)) {
                showMessage("Ya existe una Entrada con el numero de documento: " + id);
                return;
            }
            entry.setId(id);
        }
        var optional = getJournalEntryByForm(entry);
        if (optional.isEmpty()) {
            return;
        }
        try {
            journalRepository.save(entry);
            showMessage("El Registro actualizado exitosamente.");
            prepareToEditEntry(entry);
        } catch (RepositoryException e) {
            String fullMessage = switch (e.getCause()) {
                case EntityExistsException c -> "Ya existe esa Cuenta";
                case IllegalArgumentException c -> "Los datos no puede ser nulo";
                case ConstraintViolationException c -> "Codigo de cuenta duplicado";
                case null, default -> e.getMessage();
            };
            showError("Error al guardar: " + fullMessage);
        }
    }

    private void deleteEntry() {
        if (journalRepository == null) {
            showError("Error: journal repository is null!");
            return;
        }
        var response = JOptionPane.showConfirmDialog(
                getView(),
                "Desea eliminar? El cambio sera permanente.",
                "Elimination",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response != JOptionPane.OK_OPTION) return;

        journalEntry.ifPresentOrElse(entry -> {
            try {
                journalRepository.delete(entry);
                prepareToAddEntry();
            } catch (RepositoryException e) {
                showError("Error al eliminar el registro.");
            }
        }, () -> showError("El Registro no puede estar vacia."));
    }

    private void updateEntry() {
        if (journalRepository == null) {
            showError("Error: journal repository is null!");
            return;
        }
        if (journalEntry.isEmpty()) {
            showError("El Registro no puede estar vacia.");
            return;
        }
        JournalEntry entry;
        var optional = getJournalEntryByForm(journalEntry.get());
        if (optional.isEmpty()) {
            return;
        }
        entry = optional.get();
        try {
            entry = journalRepository.update(entry);
            showMessage("El Registro actualizado exitosamente.");
            prepareToEditEntry(entry);
        } catch (RepositoryException e) {
            String fullMessage = switch (e.getCause()) {
                case IllegalArgumentException c -> "Los datos no son validos";
                case ObjectDeletedException c -> "No se puede editar un cuenta eliminado";
                case ConstraintViolationException c -> "Operacion no valido";
                case null, default -> e.getMessage();
            };
            showError("Error al guardar: " + fullMessage);
        }
    }

    public void prepareToEditEntry(@NotNull JournalEntry je) {
        journalEntry = Optional.of(je);
        getTxtEntryDocumentNumber().setEnabled(false);
        getTxtEntryDocumentNumber().setText(je.getId().toString());
        getTxtEntryName().setText(je.getName());
        getTaEntryConcept().setText(je.getConcept());
        getSpnEntryDate().getModel().setValue(je.getDate());
        getTxtEntryCheckNumber().setText(je.getCheckNumber());
        prepareBtnToEditEntry();
        prepareToAddRecord();
        loadData();
    }

    public void prepareToEditEntry(int jeId) {
        if (journalRepository == null) {
            showError("Error: journal repository is null!");
            return;
        }
        journalRepository.findById(jeId).ifPresentOrElse(this::prepareToEditEntry, () -> showMessage("No journal entry found: " + jeId));
    }

    private void prepareToAddEntry() {
        journalEntry = Optional.of(new JournalEntry());
        getTxtEntryDocumentNumber().setEnabled(true);
        getTxtEntryName().setText("");
        getTaEntryConcept().setText("");
        getTxtEntryDocumentNumber().setText("");
        getSpnEntryDate().getModel().resetValue();
        getTxtEntryCheckNumber().setText("");
        prepareBtnToAddEntry();
        prepareToAddRecord();
        loadData();
    }

    private void prepareBtnToAddEntry() {
        getBtnAddEntry().setEnabled(true);
        getBtnSaveEntry().setEnabled(false);
        getBtnDeleteEntry().setEnabled(false);
        getBtnUpdateEntry().setEnabled(false);
        getBtnGeneratePaymentVoucher().setEnabled(false);
        getBtnGenerateRegistrationForm().setEnabled(false);
        isBeingAdded = true;
        isBeingEdited = false;
    }

    private void prepareBtnToEditEntry() {
        getBtnAddEntry().setEnabled(true);
        getBtnSaveEntry().setEnabled(false);
        getBtnDeleteEntry().setEnabled(true);
        getBtnUpdateEntry().setEnabled(false);
        getBtnGeneratePaymentVoucher().setEnabled(true);
        getBtnGenerateRegistrationForm().setEnabled(true);
        isBeingEdited = true;
        isBeingAdded = false;
    }

    private void loadDataAccount() {
        if (accountRepository == null) {
            return;
        }
        var list = accountRepository.findAll();
        cbxModelAccount.setData(list);
    }

    private void calcBalance() {
        var balance = BigDecimal.ZERO;
        var debitSum = BigDecimal.ZERO;
        var creditSum = BigDecimal.ZERO;

        if (tblDataList == null) {
            tblDataList = new ArrayList<>();
        } else {
            tblDataList.clear();
        }
        for (LedgerRecord record : getData()) {
            debitSum = debitSum.add(record.getDebit(), MathContext.DECIMAL128);
            creditSum = creditSum.add(record.getCredit(), MathContext.DECIMAL128);
            balance = creditSum.subtract(debitSum, MathContext.DECIMAL128);
            tblDataList.add(new LedgerRecordDTO(record.getDocumentType().getName(), record.getVoucher(), Account.getCellRenderer(record.getAccount().getId()), record.getReference(), record.getDebit().toString(), record.getCredit().toString()));
        }
        tblDataList.add(new LedgerRecordDTO("", "", "TOTAL", "", debitSum.toString(), creditSum.toString()));
        boolean isBalanced = !getData().isEmpty();
        getBtnSaveEntry().setEnabled(isBalanced && isBeingAdded);
        getBtnUpdateEntry().setEnabled(isBalanced && isBeingEdited);
    }

    public class LedgerRecordTableModel extends AbstractTableModel {

        private final String[] COLUMN_NAMES = {"Tipo de Documento", "Comprobante", "Referencia", "Código", "Debíto", "Crédito"};

        @Override
        public int getRowCount() {
            return tblDataList.size();
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
            LedgerRecordDTO record = tblDataList.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> record.getDocumentType();
                case 1 -> record.getVoucher();
                case 2 -> record.getReference();
                case 3 -> record.getAccountId();
                case 4 -> record.getDebit();
                case 5 -> record.getCredit();
                default -> "que haces?";
            };

        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 4, 5 -> BigDecimal.class;
                default -> String.class;
            };
        }
    }


    public LedgerRecordRepository getLedgerRecordRepository() {
        return (LedgerRecordRepository) super.getRepository();
    }

    @Override
    public AccountingEntryFormView getView() {
        return (AccountingEntryFormView) super.getView();
    }

    public JTextField getTxtEntryCheckNumber() {
        return getView().getTxtEntryCheckNumber();
    }

    public LocalDateSpinner getSpnEntryDate() {
        return getView().getSpnEntryDate();
    }

    public JTextField getTxtEntryDocumentNumber() {
        return getView().getTxtEntryDocumentNumber();
    }

    public JTextField getTxtEntryName() {
        return getView().getTxtEntryName();
    }

    public JTextField getTxtRecordAmount() {
        return getView().getTxtRecordAmount();
    }

    public JTextField getTxtRecordReference() {
        return getView().getTxtRecordReference();
    }

    public JTextField getTxtRecordVoucher() {
        return getView().getTxtRecordVoucher();
    }

    public JTextArea getTaEntryConcept() {
        return getView().getTaEntryConcept();
    }

    public JRadioButton getRbtRecordCredit() {
        return getView().getRbtRecordCredit();
    }

    public JRadioButton getRbtRecordDebit() {
        return getView().getRbtRecordDebit();
    }

    public JButton getBtnSaveEntry() {
        return getView().getBtnSaveEntry();
    }

    public JButton getBtnUpdateEntry() {
        return getView().getBtnUpdateEntry();
    }

    public JButton getBtnDeleteEntry() {
        return getView().getBtnDeleteEntry();
    }

    public JButton getBtnAddEntry() {
        return getView().getBtnAddEntry();
    }

    public JComboBox<Account> getCbxRecordAccount() {
        return getView().getCbxRecordAccount();
    }

    public JComboBox<DocumentType> getCbxRecordDocumentType() {
        return getView().getCbxRecordDocumentType();
    }

    public ButtonGroup getBgRecordType() {
        return getView().getBgRecordType();
    }

    public JButton getBtnAddRecord() {
        return getView().getBtnAdd();
    }

    public JButton getBtnUpdateRecord() {
        return getView().getBtnUpdate();
    }

    public JButton getBtnEdit() {
        return getView().getBtnEdit();
    }

    public JButton getBtnDeleteRecord() {
        return getView().getBtnDelete();
    }

    public JButton getBtnSaveRecord() {
        return getView().getBtnSave();
    }

    public JButton getBtnGeneratePaymentVoucher() {
        return getView().getBtnGeneratePaymentVoucher();
    }

    public JButton getBtnGenerateRegistrationForm() {
        return getView().getBtnGenerateRegistrationForm();
    }

}
