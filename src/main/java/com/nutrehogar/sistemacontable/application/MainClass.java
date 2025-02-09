package com.nutrehogar.sistemacontable.application;

import com.formdev.flatlaf.FlatLightLaf;
import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.application.controller.service.ReportController;
import com.nutrehogar.sistemacontable.ui.JComponents.SplashScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainClass {

    public static void main(String[] args) {
        System.setProperty("LOG_DIR", ConfigLoader.getLogsPath());
        Logger log = LoggerFactory.getLogger(MainClass.class);
        try (InputStream input = ReportController.class.getResourceAsStream("/jasperreports_extension.properties")) {
            if (input == null) {
                System.out.println("No se encontró el archivo de propiedades.");
                return;
            }
            Properties properties = new Properties();
            properties.load(input);

            // Establecer una propiedad específica
            properties.setProperty("net.sf.jasperreports.extension.simple.font.families.roboto", "D:/fonts/roboto.xml");
            log.info("Propertie {}", properties);
            try (FileOutputStream output = new FileOutputStream(ReportController.class.getResource("/jasperreports_extension.properties").getFile())) {
                log.info("output {}", output.toString());
                properties.store(output, "Actualización de propiedades de JasperReports");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);
        System.setProperty("flatlaf.animation", "true");
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        System.setProperty("flatlaf.useWindowDecorations", "true");
        FlatLightLaf.setup();
        new App(splash);
    }
}
