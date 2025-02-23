package com.nutrehogar.sistemacontable.application.controller;

import com.nutrehogar.sistemacontable.ui.view.View;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Getter
@Slf4j
public abstract class Controller {
    private final View view;

    public Controller(View view) {
        this.view = view;
    }

    protected abstract void initialize();

    protected abstract void setupViewListeners();

    public void showMessage(Object message, String title) {
        log.info(title, message);
        JOptionPane.showMessageDialog(view, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showMessage(Object message) {
        showMessage(message, "Message");
    }

    public void showError(Object message, String title) {
        log.error(title, message);
        JOptionPane.showMessageDialog(view, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showError(Object message) {
        showError(message, "Error");
    }

}
