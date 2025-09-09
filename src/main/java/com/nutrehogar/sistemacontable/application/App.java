package com.nutrehogar.sistemacontable.application;

import com.nutrehogar.sistemacontable.application.config.ApplicationContext;
import com.nutrehogar.sistemacontable.application.controller.AuthController;
import com.nutrehogar.sistemacontable.application.view.AuthView;
import com.nutrehogar.sistemacontable.application.config.AppConfig;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.domain.core.WriteExecutor;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import com.nutrehogar.sistemacontable.ui.ThemeConfig;
import com.nutrehogar.sistemacontable.application.view.service.DashboardView;
import lombok.extern.slf4j.Slf4j;


import javax.swing.*;

@Slf4j
public class App {
    private JFrame frame;

    public App() {
        try {
            var session = HibernateUtil.getSession();
            HibernateUtil.returnSession(session);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread.startVirtualThread(() -> Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            WriteExecutor.shutdown();
            HibernateUtil.shutdown();
            log.info("Application stopped");
        })));

        var context = new ApplicationContext();

        try {
            AppConfig.setup(context);
            frame = new JFrame();
            frame.setIconImage(new FlatSVGIcon("svgs/SistemaContableLogo.svg", 250, 250).getImage());
            frame.setTitle("Sistema Contable");
            frame.setSize(1300, 600);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.getRootPane().setBackground(ThemeConfig.Palette.SOLITUDE_50);
            frame.add(context.getBean(DashboardView.class));
            frame.setVisible(true);
            context.getBean(AuthView.class).setVisible(true);
            var user = context.getBean(AuthController.class).getAuthenticatedUser();
            frame.setTitle("Sistema Contable - " + user.getUsername());
            AppConfig.init(context, HibernateUtil.getSession(), user, frame);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}