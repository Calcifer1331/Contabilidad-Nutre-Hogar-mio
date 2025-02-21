package com.nutrehogar.sistemacontable.ui.view;

import javax.swing.*;

public abstract class SimpleView extends View {
    public abstract JTable getTblData();

    public abstract JButton getBtnEdit();
}