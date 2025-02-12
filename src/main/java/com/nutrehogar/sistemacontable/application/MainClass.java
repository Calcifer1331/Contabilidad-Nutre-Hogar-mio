package com.nutrehogar.sistemacontable.application;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.application.controller.service.ReportController;
import com.nutrehogar.sistemacontable.ui.JComponents.SplashScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainClass {

    public static void main(String[] args) {
        System.setProperty("LOG_DIR", ConfigLoader.getLogsPath());
        Logger log = LoggerFactory.getLogger(MainClass.class);
        System.setProperty("flatlaf.animation", "true");
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        System.setProperty("flatlaf.useWindowDecorations", "true");
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> Thread.startVirtualThread(() -> {
            JPasswordField passwordField = new JPasswordField();
            FlatSVGIcon icon = new FlatSVGIcon("svgs/key.svg");
            boolean pass = false;
            while (!pass) {
                pass = requestPing(log, passwordField, icon);
            }
        }));
        new App();
    }

    private static boolean requestPing(Logger log, JPasswordField passwordField, Icon icon) {
        int option = JOptionPane.showConfirmDialog(null, passwordField, "Ingrese su PING", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
        if (option != JOptionPane.OK_OPTION) {
            log.info("Close program, no ping");
            System.exit(0);
        }
        String pin = new String(passwordField.getPassword());
        log.info("PIN ingresado: {}", pin);
        return pin.equals(ConfigLoader.PING);
    }
}
