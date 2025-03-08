package com.nutrehogar.sistemacontable.application.view;

import com.nutrehogar.sistemacontable.domain.model.User;

import javax.swing.*;
import java.awt.*;

public abstract class AuthView extends JDialog implements View {
    public AuthView() {
        setModal(true);
    }

    public abstract JButton getBtnOk();

    public abstract JButton getBtnCancel();

    public abstract JPasswordField getTxtPing();

    public abstract JList<User> getLstUser();

}
