package com.nutrehogar.sistemacontable.application.view.crud;

import com.nutrehogar.sistemacontable.domain.Permissions;

import javax.swing.*;

public abstract class UserView extends CRUDView {
    public abstract JComboBox<Permissions> getCbxPermissions();

    public abstract JTextField getTxtUsername();

    public abstract JTextField getTxtPassword();

    public abstract JCheckBox getChkIsEnable();
}
