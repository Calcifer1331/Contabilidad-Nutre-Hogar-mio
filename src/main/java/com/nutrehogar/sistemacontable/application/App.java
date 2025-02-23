package com.nutrehogar.sistemacontable.application;

import com.nutrehogar.sistemacontable.application.repository.crud.*;
import com.nutrehogar.sistemacontable.domain.model.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import com.nutrehogar.sistemacontable.application.controller.service.DashboardController;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryFactory;
import com.nutrehogar.sistemacontable.ui.ThemeConfig;
import com.nutrehogar.sistemacontable.ui.view.imple.*;
import com.nutrehogar.sistemacontable.ui.view.service.DashboardView;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.swing.*;

@Slf4j
public class App {
    private Session session;
    private UserRepository userRepository;
    private LoginForm loginForm;
    private JFrame frame;
    private DashboardController dashboardController;
    private DashboardView dashboardView;
    private User user;

    public App() {
        Thread.startVirtualThread(() -> {
            Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
        });
        session = HibernateUtil.getSession();
        userRepository = CRUDRepositoryFactory.createRepository(UserRepository.class, User.class, session);
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame();
            frame.setIconImage(new FlatSVGIcon("svgs/SistemaContableLogo.svg", 250, 250).getImage());
            frame.setTitle("Sistema Contable");
            frame.setSize(1300, 600);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.getRootPane().setBackground(ThemeConfig.Palette.SOLITUDE_50);
            frame.add(getDashboardView());
            frame.setVisible(true);
            loginForm = new LoginForm(frame, userRepository);
            loginForm.setVisible(true);
            user = loginForm.getUser();
            if (user == null) {
                log.error("User is null");
                System.exit(0);
            }
            frame.setTitle("Sistema Contable - " + user.getUsername());
        });
        dashboardController = new DashboardController(getDashboardView(), frame, userRepository, session, user);
    }

    private DashboardView getDashboardView() {
        if (dashboardView == null) {
            log.info("DashboardView created");
            dashboardView = new DefaultDashboardView();
        }
        return dashboardView;
    }

}
