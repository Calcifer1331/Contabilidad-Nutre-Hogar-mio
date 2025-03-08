package com.nutrehogar.sistemacontable.ui.components;

import com.nutrehogar.sistemacontable.domain.AccountType;
import com.nutrehogar.sistemacontable.domain.DocumentType;
import com.nutrehogar.sistemacontable.domain.Permissions;
import com.nutrehogar.sistemacontable.domain.model.Account;
import com.nutrehogar.sistemacontable.domain.model.AccountSubtype;

import javax.swing.*;
import java.awt.*;

public class CustomListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        var label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setText(switch (value) {
                    case AccountType accountType -> AccountType.getCellRenderer(accountType);
                    case AccountSubtype tipoCuenta ->
                            tipoCuenta.getAccountType().getId() + "." + tipoCuenta.getCanonicalId() + " " + tipoCuenta.getName();
                    case Account account -> account.getFormattedId();
                    case DocumentType documentType -> documentType.getName();
                    case Permissions permissions -> permissions.getName();
                    case null -> "";
                    default -> value.toString();
                }
        );
        return label;
    }
}