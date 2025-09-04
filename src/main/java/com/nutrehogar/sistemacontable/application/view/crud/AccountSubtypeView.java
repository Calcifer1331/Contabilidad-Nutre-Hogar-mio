package com.nutrehogar.sistemacontable.application.view.crud;

import com.nutrehogar.sistemacontable.domain.AccountType;

import javax.swing.*;

public abstract class AccountSubtypeView extends CRUDView {
    public abstract JComboBox<AccountType> getCbxAccountType();

    public abstract JTextField getTxtAccountSubtypeId();

    public abstract JTextField getTxtAccountSubtypeName();

    public abstract JLabel getLblAccountTypeId();
}
