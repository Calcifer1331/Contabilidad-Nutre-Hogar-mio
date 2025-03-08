package com.nutrehogar.sistemacontable.application.controller.crud;

import com.nutrehogar.sistemacontable.application.repository.UserRepository;
import com.nutrehogar.sistemacontable.application.view.crud.UserView;
import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.Permissions;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.infrastructure.report.ReportService;
import com.nutrehogar.sistemacontable.ui.components.CustomComboBoxModel;
import com.nutrehogar.sistemacontable.ui.components.CustomListCellRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class UserController extends CRUDController<User, Integer> {
    private CustomComboBoxModel<Permissions> cbxModelPermissions;

    public UserController(UserRepository repository, UserView view, ReportService reportService, User user) {
        super(repository, view, reportService, user);
        prepareToAdd();
    }

    @Override
    protected void initialize() {
        setTblModel(new CustomTableModel("Nombre", "Contraseña", "Habilitado", "Permiso") {
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                var user = getData().get(rowIndex);
                return switch (columnIndex) {
                    case 0 -> user.getUsername();
                    case 1 -> user.getPassword();
                    case 2 -> user.isEnable();
                    case 3 -> user.getPermissions();
                    default -> "que haces?";
                };
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 2 -> Boolean.class;
                    case 3 -> Permissions.class;
                    default -> String.class;
                };
            }
        });
        cbxModelPermissions = new CustomComboBoxModel<>(Permissions.values());
        super.initialize();
    }


    @Override
    protected void setupViewListeners() {
        super.setupViewListeners();
        getCbxPermissions().setRenderer(new CustomListCellRenderer());
        getCbxPermissions().setModel(cbxModelPermissions);
    }

    @Override
    protected void prepareToEdit() {
        super.prepareToEdit();
        getTxtUsername().setText(getSelected().getUsername());
        getTxtPassword().setText(getSelected().getPassword());
        getChkIsEnable().setSelected(getSelected().isEnable());
        getCbxPermissions().setSelectedItem(getSelected().getPermissions());
    }

    @Override
    protected void prepareToAdd() {
        super.prepareToAdd();
        getCbxPermissions().setSelectedIndex(0);
        getChkIsEnable().setSelected(true);
        getTxtUsername().setText("");
        getTxtPassword().setText("");
    }

    @Override
    protected Integer prepareToDelete() {
        return getSelected().getId();
    }


    @Override
    protected User prepareToSave() {
        if (!checkFields()) return null;
        return getByForm(new User());
    }

    @Override
    protected User prepareToUpdate() {
        if (!checkFields()) return null;
        getByForm(getSelected());
        return getSelected();
    }

    private @NotNull User getByForm(User user) {
        if (user == null) user = new User();
        user.setUser(this.user);
        user.setPermissions(cbxModelPermissions.getSelectedItem());
        user.setUsername(getTxtUsername().getText());
        user.setPassword(getTxtPassword().getText());
        user.setEnable(getChkIsEnable().isSelected());
        return user;
    }


    public boolean checkFields() {
        if (cbxModelPermissions.getSelectedItem() == null || getTxtUsername().getText().isBlank() || getTxtPassword().getText().isBlank()) {
            showMessage("Ningun campo puede estar vació.");
            return false;
        }
        if (getTxtUsername().getText().length() < User.MIN_LENGTH) {
            showMessage("El nombre de usuario no puede ser menor a: " + User.MIN_LENGTH);
            return false;
        }
        if (getTxtPassword().getText().length() < User.MIN_LENGTH) {
            showMessage("El contraseña no puede ser menor a: " + User.MIN_LENGTH);
            return false;
        }
        if (getTxtUsername().getText().length() > User.MAX_LENGTH) {
            showMessage("El nombre de usuario no puede ser mayor a: " + User.MAX_LENGTH);
            return false;
        }
        if (getTxtPassword().getText().length() > User.MAX_LENGTH) {
            showMessage("La contraseña no puede ser mayor a: " + User.MAX_LENGTH);
            return false;
        }
        return true;
    }


    @Override
    public UserView getView() {
        return (UserView) super.getView();
    }

    @Override
    public UserRepository getRepository() {
        return (UserRepository) super.getRepository();
    }

    public JComboBox<Permissions> getCbxPermissions() {
        return getView().getCbxPermissions();
    }

    public JTextField getTxtUsername() {
        return getView().getTxtUsername();
    }

    public JTextField getTxtPassword() {
        return getView().getTxtPassword();
    }

    public JCheckBox getChkIsEnable() {
        return getView().getChkIsEnable();
    }
}
