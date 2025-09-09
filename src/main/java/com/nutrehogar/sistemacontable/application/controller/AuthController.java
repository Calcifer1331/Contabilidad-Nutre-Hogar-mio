package com.nutrehogar.sistemacontable.application.controller;

import com.nutrehogar.sistemacontable.application.repository.UserRepository;
import com.nutrehogar.sistemacontable.application.view.AuthView;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.exception.ApplicationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Slf4j
public final class AuthController extends Controller {
    private final UserRepository userRepository;
    private final DefaultListModel<User> userListModel;
    @Getter
    private User authenticatedUser;
    private User adminUser;

    public AuthController(AuthView view, UserRepository userRepository, User adminUser) {
        super(view);
        this.userRepository = userRepository;
        this.adminUser = adminUser;
        this.userListModel = new DefaultListModel<>();
        initialize();
    }

    @Override
    protected void initialize() {
        getLstUser().setModel(userListModel);
        SwingUtilities.invokeLater(() -> getBtnOk().setEnabled(false));
        new SwingWorker<ArrayList<User>, Void>() {
            @Override
            protected ArrayList<User> doInBackground() {
                return new ArrayList<>(userRepository.findAll()); // Carga en background
            }

            @Override
            protected void done() {
                try {
                    userListModel.addAll(get());
                    userListModel.addElement(adminUser);
                    if (!userListModel.isEmpty()) {
                        getLstUser().setSelectedIndex(0);
                        getBtnOk().setEnabled(true);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    showError("Error al cargar datos de usuario", new ApplicationException("Failure to find all users.", e));
                }
            }
        }.execute();
        setupViewListeners();
    }

    @Override
    protected void setupViewListeners() {
        getBtnOk().addActionListener(e -> {
            if (userListModel.isEmpty()) return;
            User selectedUser = getLstUser().getSelectedValue();
            if (selectedUser != null && String.valueOf(getTxtPing().getPassword()).equals(selectedUser.getPassword())) {
                authenticatedUser = selectedUser;
                authenticatedUser.setUser(authenticatedUser);
                getView().setVisible(false);
                getView().dispose();
            } else {
                showMessage("Contraseña Incorrecta.");
            }
        });
        getBtnCancel().addActionListener(e -> {
            log.info("System.exit");
            System.exit(0);
        });
    }

    @Override
    public AuthView getView() {
        return (AuthView) super.getView();
    }

    public JList<User> getLstUser() {
        return getView().getLstUser();
    }

    public JButton getBtnOk() {
        return getView().getBtnOk();
    }

    public JButton getBtnCancel() {
        return getView().getBtnCancel();
    }

    public JPasswordField getTxtPing() {
        return getView().getTxtPing();
    }

}
