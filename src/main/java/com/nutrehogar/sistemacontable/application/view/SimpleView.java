package com.nutrehogar.sistemacontable.application.view;

import com.nutrehogar.sistemacontable.ui.JComponents.AuditablePanel;

import javax.swing.*;

public abstract class SimpleView extends JPanel implements View {
    public abstract JTable getTblData();

    public abstract JButton getBtnEdit();

    public abstract AuditablePanel getAuditablePanel();

    public abstract JButton getBtnGenerateReport();
}