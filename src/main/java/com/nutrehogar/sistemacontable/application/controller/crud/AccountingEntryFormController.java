package com.nutrehogar.sistemacontable.application.controller.crud;

import com.nutrehogar.sistemacontable.application.controller.SimpleController;
import com.nutrehogar.sistemacontable.application.controller.crud.dto.LedgerRecordDTO;
import com.nutrehogar.sistemacontable.domain.model.*;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.JournalEntryReportDTO;
import com.nutrehogar.sistemacontable.infrastructure.report.dto.LedgerRecordReportDTO;
import com.nutrehogar.sistemacontable.infrastructure.report.PaymentVoucher;
import com.nutrehogar.sistemacontable.infrastructure.report.RegistrationForm;
import com.nutrehogar.sistemacontable.application.repository.AccountRepository;
import com.nutrehogar.sistemacontable.application.repository.JournalEntryRepository;
import com.nutrehogar.sistemacontable.application.repository.LedgerRecordRepository;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.exception.RepositoryException;
import com.nutrehogar.sistemacontable.ui.components.*;
import com.nutrehogar.sistemacontable.application.view.crud.AccountingEntryFormView;
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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static com.nutrehogar.sistemacontable.application.config.Util.*;

@Slf4j
public class AccountingEntryFormController extends SimpleController<LedgerRecord, LedgerRecord> {
    private final JournalEntryRepository journalRepository;
    private final AccountRepository accountRepository;
    private Optional<JournalEntry> journalEntry;
    private CustomComboBoxModel<Account> cbxModelAccount;
    private CustomComboBoxModel<DocumentType> cbxModelDocumentType;
    private List<LedgerRecordDTO> tblDataList;
    private boolean isBeingAdded;
    private boolean isBeingEdited;
    public static final BigDecimal ZERO = BigDecimal.valueOf(0, 2);

    public AccountingEntryFormController(LedgerRecordRepository repository, AccountingEntryFormView view, JournalEntryRepository journalRepository, AccountRepository accountRepository, ReportService reportService, User user) {
        super(repository, view, reportService, user);
        this.journalRepository = journalRepository;
        this.accountRepository = accountRepository;
        loadDataAccount();
    }

    @Override
    protected void initialize() {
        setTblModel(new CustomTableModel("Referencia", "Cuenta", "Debíto", "Crédito") {
            @Override
            public int getRowCount() {
                return tblDataList.size();
            }
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                var record = tblDataList.get(rowIndex);
                return switch (columnIndex) {
                    case 0 -> record.getReference();
                    case 1 -> record.getAccountId();
                    case 2 -> record.getDebit();
                    case 3 -> record.getCredit();
                    default -> "que haces?";
                };
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        });
        cbxModelAccount = new CustomComboBoxModel<>(List.of());
        cbxModelDocumentType = new CustomComboBoxModel<>(DocumentType.values());
        journalEntry = Optional.empty();
        tblDataList = new ArrayList<>();
        prepareBtnToAddEntry();
        prepareBtnToAddRecord();
        if (!user.isAuthorized()) {
            getBtnAddRecord().setEnabled(false);
            getBtnDeleteRecord().setEnabled(false);
            getBtnSaveRecord().setEnabled(false);
            getBtnUpdateRecord().setEnabled(false);
            getBtnEdit().setEnabled(false);
            getBtnAddEntry().setEnabled(false);
            getBtnUpdateEntry().setEnabled(false);
            getBtnSaveEntry().setEnabled(false);
        }
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

    private void calcBalance() {
        var debitSum = BigDecimal.ZERO;
        var creditSum = BigDecimal.ZERO;
        if (journalEntry.isEmpty()) {
            showError("Error: journal entry is empty!");
            return;
        }

        if (tblDataList == null) {
            tblDataList = new ArrayList<>();
        } else {
            tblDataList.clear();
        }

        for (LedgerRecord record : getData()) {
            debitSum = debitSum.add(record.getDebit(), MathContext.DECIMAL128);
            creditSum = creditSum.add(record.getCredit(), MathContext.DECIMAL128);
            tblDataList.add(new LedgerRecordDTO(Account.getCellRenderer(record.getAccount().getId()), record.getReference(), formatBigDecimal(record.getDebit()), formatBigDecimal(record.getCredit())));
        }
        tblDataList.add(new LedgerRecordDTO("", "TOTAL", DECIMAL_FORMAT.format(debitSum), DECIMAL_FORMAT.format(creditSum)));
        boolean isBalanced = !getData().isEmpty();
        getBtnSaveEntry().setEnabled(isBalanced && isBeingAdded);
        getBtnUpdateEntry().setEnabled(isBalanced && isBeingEdited);
    }

    private String formatBigDecimal(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) == 0 ? "" : DECIMAL_FORMAT.format(value);
    }

    @Override
    protected void setupViewListeners() {
        super.setupViewListeners();
        getCbxRecordAccount().setModel(cbxModelAccount);
        getCbxEntryDocumentType().setModel(cbxModelDocumentType);
        getCbxEntryDocumentType().setRenderer(new CustomListCellRenderer());
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
                reportService.generateReport(PaymentVoucher.class, getJournalEntryDTO());
                showMessage("Reporte generado!");
            } catch (RepositoryException ex) {
                showError("Error al crear el Reporte.", ex);
            }
        });
        getBtnGenerateRegistrationForm().addActionListener(e -> {
            try {
                reportService.generateReport(RegistrationForm.class, getJournalEntryDTO());
                showMessage("Reporte generado!");
            } catch (RepositoryException ex) {
                showError("Error al crear el Reporte.", ex);
            }
        });
        ((AbstractDocument) getTxtRecordAmount().getDocument()).setDocumentFilter(new CustomDocumentFilter(CustomDocumentFilter.Type.DECIMAL));
        ((AbstractDocument) getTxtEntryDocumentNumber().getDocument()).setDocumentFilter(new CustomDocumentFilter(CustomDocumentFilter.Type.INTEGER));
    }

    private @Nullable JournalEntryReportDTO getJournalEntryDTO() {
        AtomicReference<JournalEntryReportDTO> journalEntryDTO = new AtomicReference<>();
        journalEntry.ifPresentOrElse(entry -> {

            var records = getLedgerRecordDTO();

            journalEntryDTO.set(new JournalEntryReportDTO(
                    entry.getId().getDocumentNumber(),
                    entry.getCheckNumber(),
                    entry.getDate(),
                    entry.getName(),
                    entry.getConcept(),
                    records.getLast().getDebit(),
                    records));
        }, () -> log.error("getJournalEntryDTO() error"));
        return journalEntryDTO.get();
    }

    private List<LedgerRecordReportDTO> getLedgerRecordDTO() {
        var recordList = new ArrayList<LedgerRecordReportDTO>();
        var debitSum = BigDecimal.ZERO;
        var creditSum = BigDecimal.ZERO;
        for (var record : getData()) {
            debitSum = debitSum.add(record.getDebit(), MathContext.DECIMAL128);
            creditSum = creditSum.add(record.getCredit(), MathContext.DECIMAL128);
            recordList.add(
                    new LedgerRecordReportDTO(
                            toStringSafe(record.getJournalEntry().getId().getDocumentType(), DocumentType::getName),
                            toStringSafe(record.getJournalEntry().getId().getDocumentNumber()),
                            toStringSafe(record.getAccount().getId(), Account::getCellRenderer),
                            toStringSafe(record.getReference()),
                            formatDecimalSafe(record.getDebit()),
                            formatBigDecimal(record.getCredit())
                    )
            );
        }
        recordList.add(new LedgerRecordReportDTO("", "", "", "TOTAL", DECIMAL_FORMAT.format(debitSum), DECIMAL_FORMAT.format(creditSum)));
        return recordList;
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
            setAuditoria();
            getBtnDeleteRecord().setEnabled(true);
            getBtnEdit().setEnabled(true);
        } else {
            getBtnDeleteRecord().setEnabled(false);
            getBtnEdit().setEnabled(false);
        }
    }

    @Override
    protected void setAuditoria() {
        SwingUtilities.invokeLater(() -> {
            getAuditablePanel().getLblCreateAt().setText(getSelected().getCreatedAt() == null ? NA : getSelected().getCreatedAt().format(DATE_FORMATTER));
            getAuditablePanel().getLblCreateBy().setText(getSelected().getCreatedBy() == null ? NA : getSelected().getCreatedBy());
            getAuditablePanel().getLblUpdateAt().setText(getSelected().getUpdatedAt() == null ? NA : getSelected().getUpdatedAt().format(DATE_FORMATTER));
            getAuditablePanel().getLblUpdateBy().setText(getSelected().getUpdatedBy() == null ? NA : getSelected().getUpdatedBy());
            getAuditablePanel().revalidate();
            getAuditablePanel().repaint();
        });
    }

    private void saveRecord() {
        if (journalEntry.isEmpty()) {
            showError("La Entrada esta vaciá.");
            return;
        }
        if (cbxModelDocumentType.getSelectedItem() == null) {
            showError("Selecciona un tipo de documento");
            return;
        }
        if (getTxtEntryDocumentNumber().getText().isEmpty()) {
            showError("El numero de documento no puede estar vacio.");
            return;
        }

        var record = getLedgerRecordByForm(new LedgerRecord(user));
        if (record.isEmpty()) {
            return;
        }
        journalEntry.get().getLedgerRecords().add(record.get());
        loadData();
    }

    private Optional<LedgerRecord> getLedgerRecordByForm(LedgerRecord lr) {
        if (journalEntry.isEmpty()) {
            showError("La Entrada esta vaciá.");
            return Optional.empty();
        }
        Optional<Account> account = Optional.ofNullable(cbxModelAccount.getSelectedItem());
        if (account.isEmpty()) {
            showError("la Cuenta esta vaciá.");
            return Optional.empty();
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(getTxtRecordAmount().getText()).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            showMessage("El monto debe ser un numero y no puede estar vació.");
            return Optional.empty();
        }
        if (lr == null) lr = new LedgerRecord();
        lr.setJournalEntry(journalEntry.get());
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
            showMessage("El nombre de la Entrada no puede esta vaciá.");
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
        je.setUser(user);

        for (var record : getData()) {
            record.setJournalEntry(je);
            record.setUser(user);
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
        record.get().setUser(user);
        loadData();
        prepareToAddRecord();
    }

    private void prepareToEditRecord() {
        if (getSelected() == null) {
            showError("El Registro no puede estar vacia.");
            return;
        }

        prepareBtnToEditRecord();

        getTxtRecordReference().setText(getSelected().getReference());
        cbxModelAccount.setSelectedItem(getSelected().getAccount());

        if (getSelected().getCredit() != null && !(getSelected().getCredit().compareTo(BigDecimal.ZERO)==0)) {
            log.info("es credito");
            getRbtRecordCredit().setSelected(true);
            getTxtRecordAmount().setText(getSelected().getCredit().toString());
        }
        if (getSelected().getDebit() != null && !(getSelected().getDebit().compareTo(BigDecimal.ZERO) == 0)){
            log.info("es debito");
            getRbtRecordDebit().setSelected(true);
            getTxtRecordAmount().setText(getSelected().getDebit().toString());
        }
    }

    private void prepareToAddRecord() {
        prepareBtnToAddRecord();
        setSelected(null);
        if (cbxModelAccount.getSize() > 0) {
            getCbxRecordAccount().setSelectedIndex(0);
        }
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
        try {
            if (journalRepository == null) {
                showError("Error: journal repository is null!");
                return;
            }
            JournalEntry entry = new JournalEntry(user);
            String documentNo = getTxtEntryDocumentNumber().getText();
            if (!documentNo.isBlank()) {
                int id;
                try {
                    id = Integer.parseInt(documentNo);
                } catch (NumberFormatException e) {
                    showMessage("El Documento No. debe ser un numero.");
                    return;
                }

                var journalId = new JournalEntryPK(id, cbxModelDocumentType.getSelectedItem());

                if (journalRepository.existsById(journalId)) {
                    showMessage("Ya existe una Entrada con el numero de documento: " + id + " y tipo de documento: " + cbxModelDocumentType.getSelectedItem().getName() + ".");
                    return;
                }
                entry.setId(journalId);
            } else {
                showMessage("La Documento no puede estar vacia.");
            }

            var optional = getJournalEntryByForm(entry);

            if (optional.isEmpty()) {
                showError("Optioanl null");
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
                    case ConstraintViolationException c -> "Código de cuenta duplicado";
                    case null, default -> e.getMessage();
                };
                showError("Error al guardar: " + fullMessage);
            }
        } catch (Exception e) {
            showError("Error al guardar: " + e.getMessage());
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
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepareToEditEntry(@NotNull JournalEntry je) {
        journalEntry = Optional.of(je);
        getTxtEntryDocumentNumber().setEnabled(false);
        getTxtEntryDocumentNumber().setText(je.getId().getDocumentNumber().toString());
        getCbxEntryDocumentType().setEnabled(false);
        getCbxEntryDocumentType().setSelectedItem(je.getId().getDocumentType());
        getTxtEntryName().setText(je.getName());
        getTaEntryConcept().setText(je.getConcept());
        getSpnEntryDate().getModel().setValue(je.getDate());
        getTxtEntryCheckNumber().setText(je.getCheckNumber());
        getLblCreateBy().setText(je.getCreatedBy() == null ? NA : je.getCreatedBy());
        getLblCreateAt().setText(je.getCreatedAt() == null ? NA : je.getCreatedAt().format(DATE_FORMATTER));
        getLblUpdateBy().setText(je.getUpdatedBy() == null ? NA : je.getUpdatedBy());
        getLblUpdateAt().setText(je.getUpdatedAt() == null ? NA : je.getUpdatedAt().format(DATE_FORMATTER));
        prepareBtnToEditEntry();
        prepareToAddRecord();
        loadData();
    }

    public void prepareToEditEntry(JournalEntryPK jeId) {
        if (journalRepository == null) {
            showError("Error: journal repository is null!");
            return;
        }
        journalRepository.findById(jeId).ifPresentOrElse(this::prepareToEditEntry, () -> showMessage("No journal entry found: " + jeId));
    }

    private void prepareToAddEntry() {
        journalEntry = Optional.of(new JournalEntry());
        getTxtEntryDocumentNumber().setEnabled(true);
        getCbxEntryDocumentType().setEnabled(true);
        getCbxEntryDocumentType().setSelectedItem(DocumentType.INCOME);
        getTxtEntryName().setText("");
        getTaEntryConcept().setText("");
        getTxtEntryDocumentNumber().setText("");
        getSpnEntryDate().getModel().resetValue();
        getTxtEntryCheckNumber().setText("");
        getLblCreateBy().setText(NA);
        getLblCreateAt().setText(NA);
        getLblUpdateBy().setText(NA);
        getLblUpdateAt().setText(NA);
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

    public JComboBox<DocumentType> getCbxEntryDocumentType() {
        return getView().getCbxEntryDocumentType();
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

    public JLabel getLblCreateAt() {
        return getView().getLblCreateAt();
    }

    public JLabel getLblCreateBy() {
        return getView().getLblCreateBy();
    }

    public JLabel getLblUpdateAt() {
        return getView().getLblUpdateAt();
    }

    public JLabel getLblUpdateBy() {
        return getView().getLblUpdateBy();
    }

}
