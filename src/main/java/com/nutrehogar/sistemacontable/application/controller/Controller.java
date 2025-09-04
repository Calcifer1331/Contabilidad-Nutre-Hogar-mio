package com.nutrehogar.sistemacontable.application.controller;

import com.nutrehogar.sistemacontable.application.view.View;
import com.nutrehogar.sistemacontable.exception.AppException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.desktop.AppEvent;

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
        JOptionPane.showMessageDialog(isComponent(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showMessage(Object message) {
        showMessage(message, "Message");
    }

    public void showError(String message, AppException cause) {
        if (cause != null)
            log.error(cause.getMessage(), cause);
        JOptionPane.showMessageDialog(isComponent(), message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showError(String message) {
        showError(message, null);
    }

    @Contract(pure = true)
    private @Nullable Component isComponent() {
        return view instanceof Component v ? v : null;
    }
}
