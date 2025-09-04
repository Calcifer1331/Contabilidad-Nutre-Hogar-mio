package com.nutrehogar.sistemacontable.ui.components;

import com.nutrehogar.sistemacontable.domain.model.Account;

import javax.swing.*;
import java.awt.*;

public class AccountListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        var label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setText(switch (value) {
                    case Account account -> Account.getCellRenderer(account.getId()) + " " + account.getName();
                    case null -> "";
                    default -> value.toString();
                }
        );
        return label;
    }
}