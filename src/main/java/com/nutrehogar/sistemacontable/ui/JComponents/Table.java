package com.nutrehogar.sistemacontable.ui.JComponents;

import javax.swing.*;

public class Table extends JTable {
    public Table() {
        super();
        CustomTableCellRenderer renderer = new CustomTableCellRenderer();
        setDefaultRenderer(Object.class, renderer);
    }
}
