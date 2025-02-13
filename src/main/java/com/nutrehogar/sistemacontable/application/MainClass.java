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
        new App();
    }
}
