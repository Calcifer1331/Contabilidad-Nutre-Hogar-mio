package com.nutrehogar.sistemacontable.application.controller;

import com.nutrehogar.sistemacontable.application.repository.UserRepository;
import com.nutrehogar.sistemacontable.application.view.AuthView;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.exception.ApplicationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;

@Slf4j
public final class AuthController extends Controller {
    private final UserRepository userRepository;
    private final DefaultListModel<User> userListModel;
    @Getter
    private User authenticatedUser;
    private final User adminUser;

    public AuthController(AuthView view, UserRepository userRepository, User adminUser) {
        super(view);
        this.userRepository = userRepository;
        this.adminUser = adminUser;
        this.userListModel = new DefaultListModel<>();
        getLstUser().setModel(userListModel);
        initialize();
    }

    @Override
    protected void initialize() {
        SwingUtilities.invokeLater(() -> getView().setVisible(true));
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
                } catch (Exception e) {
                    showError("Error al cargar datos de usuario", new ApplicationException("Failure to find all users."));
                }
            }
        }.execute();
        setupViewListeners();
    }

    @Override
    protected void setupViewListeners() {
        getBtnOk().addActionListener(e -> {
            if (String.valueOf(getTxtPing().getPassword()).equals(getLstUser().getSelectedValue().getUsername())) {
                authenticatedUser = getLstUser().getSelectedValue();
                getView().setVisible(false);
                getView().dispose();
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
