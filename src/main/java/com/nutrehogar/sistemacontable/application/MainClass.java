package com.nutrehogar.sistemacontable.application;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import com.nutrehogar.sistemacontable.ui.ThemeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClass {
    public static void main(String[] args) {
        ConfigLoader.createDirectories();
        System.setProperty("LOG_DIR", ConfigLoader.Props.DIR_LOG_NAME.getPath().toString());
        Logger log = LoggerFactory.getLogger(MainClass.class);
        ThemeConfig.setup();
        new App();
    }
}
