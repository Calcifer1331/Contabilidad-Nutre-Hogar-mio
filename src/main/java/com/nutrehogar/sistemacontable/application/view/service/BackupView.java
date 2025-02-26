package com.nutrehogar.sistemacontable.application.view.service;

import com.nutrehogar.sistemacontable.application.view.SimpleView;

import javax.swing.*;

public abstract class BackupView extends SimpleView {
    public abstract JButton getBtnAdd();

    public abstract JButton getBtnRestore();

}
