package com.nutrehogar.sistemacontable.application.controller.service;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.application.controller.Controller;
import com.nutrehogar.sistemacontable.application.repository.BackupRepository;
import com.nutrehogar.sistemacontable.exception.ReportException;
import com.nutrehogar.sistemacontable.ui.components.CustomTableCellRenderer;
import com.nutrehogar.sistemacontable.application.view.service.BackupView;
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
    private BackupRepository repository;
    private List<Path> data = new ArrayList<>();
    private Path selected;
    private AbstractTableModel tblModel;
    private final Session session;
    private final JFrame frame;

    public BackupController(BackupRepository repository, BackupView view, Session session, JFrame frame) {
        super(view);
        this.repository = repository;
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
            try {
                repository.backup(createFilePathForBackup(fileName));
            } catch (ReportException e) {
                showError("Error al crear la copia de seguridad.", e);
                return 100;
            }
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

        try {
            repository.restore(getSelected().toAbsolutePath().toString());
        } catch (ReportException e) {
            showError("Error al realizar la copia de  seguridad", e);
            return;
        }
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
