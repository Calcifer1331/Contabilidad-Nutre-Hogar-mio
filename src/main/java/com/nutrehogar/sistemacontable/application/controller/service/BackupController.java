package com.nutrehogar.sistemacontable.application.controller.service;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.application.controller.Controller;
import com.nutrehogar.sistemacontable.ui.components.CustomTableCellRenderer;
import com.nutrehogar.sistemacontable.ui.view.service.BackupView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Slf4j
public class BackupController extends Controller {
    private static final FlatSVGIcon ICON = new FlatSVGIcon("svgs/backup.svg");
    public static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    public static final SimpleDateFormat TABLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private List<Path> data = new ArrayList<>();
    private Path selected;
    private AbstractTableModel tblModel;
    private final Session session;
    private final JFrame frame;

    public BackupController(BackupView view, Session session, JFrame frame) {
        super(view);
        this.session = session;
        this.frame = frame;
        initialize();
    }

    @Override
    protected void initialize() {
        setTblModel(new BackupTableModel());
        getTblData().setModel(getTblModel());
        getTblData().setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        getTblData().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadData();
        setupViewListeners();
    }

    protected void loadData() {
        try (var stream = Files.list(ConfigLoader.Props.DIR_BACKUP_NAME.getPath())) {
            data = stream
                    .filter(Files::isRegularFile)
                    .sorted((p1, p2) -> {
                        try {
                            long time1 = Files.getLastModifiedTime(p1).toMillis();
                            long time2 = Files.getLastModifiedTime(p2).toMillis();
                            return Long.compare(time2, time1); // Orden descendente (más reciente primero)
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .toList();
        } catch (IOException e) {
            showError("Error al cargar los datos de las copias de seguridad");
        }
        updateView();

    }

    @Override
    protected void setupViewListeners() {
        getBtnAdd().addActionListener(e -> createBackup(""));
        getBtnRestore().addActionListener(e -> restoreBackup());
        getTblData().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setElementSelected(e);
            }
        });
    }

    public void showView() {
        var dialog = new JDialog(frame, "Copias de Seguridad", true);
        dialog.setIconImage(ICON.getImage());
        dialog.getRootPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(frame);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        dialog.setSize(430, 460);
        dialog.getContentPane().removeAll();
        dialog.setContentPane(getView());
        dialog.revalidate();
        dialog.repaint();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private int createBackup(String fileName) {
        var inputFileName = new JTextField(fileName == null ? "" : fileName + createNameByDate());
        var contentPanel = new JPanel();
        contentPanel.add(new Label("Nombre:"));
        contentPanel.add(inputFileName);
        int response = JOptionPane.showConfirmDialog(
                getView(),
                contentPanel,
                "Nueva Copia de Seguridad",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (response == JOptionPane.OK_OPTION) {
            if (!inputFileName.getText().isBlank()) {
                fileName = inputFileName.getText();
            } else {
                showMessage("Se usara como nombre: " + fileName);
            }
            backup(createFilePathForBackup(fileName));
            loadData();
        }
        return response;
    }

    private void restoreBackup() {
        var response = JOptionPane.showConfirmDialog(
                getView(),
                "Debe crear una copia de seguridad con los datos actuales.",
                "Restablecer copia",
                JOptionPane.OK_CANCEL_OPTION);

        if (response != JOptionPane.OK_OPTION) return;

        int result = createBackup("security_");

        if (result != JOptionPane.OK_OPTION) return;

        restartBackup(getSelected().toAbsolutePath().toString());
        showMessage("Para hacer efectivo los cambios se cerrara el programa.", "Se cerrara el programa.");
        System.exit(1);//terminar proceso
    }

    protected void updateView() {
        SwingUtilities.invokeLater(getTblModel()::fireTableDataChanged);
    }

    protected void setElementSelected(@NotNull MouseEvent e) {
        int row = getTblData().rowAtPoint(e.getPoint());
        if (row != -1) {
            int selectedRow = getTblData().getSelectedRow();
            if (selectedRow >= 0 && selectedRow < getData().size()) {
                setSelected(getData().get(selectedRow));
                getBtnRestore().setEnabled(true);
            } else {
                getBtnRestore().setEnabled(false);
            }
        }
    }

    public void backup(String fileName) {
        session.doWork(connection -> {
            try (Statement stmt = connection.createStatement()) {
                connection.setAutoCommit(true);
                stmt.execute("VACUUM INTO '" + fileName + "';");
                connection.setAutoCommit(false);
            } catch (Exception e) {
                log.error("Error while backup", e);
                showError("Error al realizar copia de seguridad.");
            }
        });
    }

    public void restartBackup(String filePath) {
        session.doWork(connection -> {
            try (Statement stmt = connection.createStatement()) {
                // Habilita el autocommit para ejecutar los comandos
                connection.setAutoCommit(true);

                // Elimina los datos actuales de las tablas
                stmt.execute("DELETE FROM main.account_subtype;");
                stmt.execute("DELETE FROM main.account;");
                stmt.execute("DELETE FROM main.ledger_record;");
                stmt.execute("DELETE FROM main.journal_entry;");

                // Adjunta la base de datos de respaldo
                stmt.execute("ATTACH DATABASE '" + filePath + "' AS BACKUP;");

                // Copia los datos del respaldo a las tablas principales
                stmt.execute("INSERT INTO main.account_subtype SELECT * FROM BACKUP.account_subtype;");
                stmt.execute("INSERT INTO main.account SELECT * FROM BACKUP.account;");
                stmt.execute("INSERT INTO main.ledger_record SELECT * FROM BACKUP.ledger_record;");
                stmt.execute("INSERT INTO main.journal_entry SELECT * FROM BACKUP.journal_entry;");

                // Restaurar el autocommit a su estado inicial
                connection.setAutoCommit(false);
            } catch (Exception e) {
                log.error("Error al realizar copia de seguridad.", e);
                showError("Error al realizar copia de seguridad");
            }
        });
    }

    private @NotNull String createFilePathForBackup(String fileName) {
        return ConfigLoader.Props.DIR_BACKUP_NAME.getPath().toString() + File.separator + fileName + ".sqlite";
    }

    private @NotNull String createNameByDate() {
        return LocalDateTime.now().format(FILE_DATE_FORMATTER);
    }

    private String getModificationDate(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            long millis = attrs.lastModifiedTime().toMillis();
            return TABLE_DATE_FORMAT.format(new Date(millis));
        } catch (Exception e) {
            return "Desconocido";
        }
    }

    public class BackupTableModel extends AbstractTableModel {

        private final String[] COLUMN_NAMES = {"Nombre", "Última Modificación"};

        @Override
        public int getRowCount() {
            return data.size();
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
            Path file = data.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> file.getFileName().toString();
                case 1 -> getModificationDate(file);
                default -> "";
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
    }

    @Override
    public BackupView getView() {
        return (BackupView) super.getView();
    }

    public JTable getTblData() {
        return getView().getTblData();
    }

    public JButton getBtnEdit() {
        return getView().getBtnEdit();
    }

    public JButton getBtnAdd() {
        return getView().getBtnAdd();
    }

    public JButton getBtnRestore() {
        return getView().getBtnRestore();
    }
}
