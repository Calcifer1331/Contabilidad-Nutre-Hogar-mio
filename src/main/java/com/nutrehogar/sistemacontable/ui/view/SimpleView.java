package com.nutrehogar.sistemacontable.ui.view;

import com.nutrehogar.sistemacontable.ui.JComponents.AuditablePanel;

import javax.swing.*;

public abstract class SimpleView extends View {
    public abstract JTable getTblData();

    public abstract JButton getBtnEdit();

    public abstract AuditablePanel getAuditablePanel();
}