package com.nutrehogar.sistemacontable.application;

import com.nutrehogar.sistemacontable.application.repository.crud.*;
import com.nutrehogar.sistemacontable.domain.model.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.application.config.HibernateUtil;
import com.nutrehogar.sistemacontable.application.controller.service.DashboardController;
import com.nutrehogar.sistemacontable.domain.core.CRUDRepositoryFactory;
import com.nutrehogar.sistemacontable.ui.view.imple.*;
import com.nutrehogar.sistemacontable.ui.view.service.DashboardView;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class App {
    private Session session;
    private UserRepository userRepository;
    private LoginForm loginForm;
    private JFrame frame;
    private DashboardController dashboardController;
    private DashboardView dashboardView;

    public App() {
        Thread.startVirtualThread(() -> {
            Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
        });
        session = HibernateUtil.getSession();
        userRepository = CRUDRepositoryFactory.createRepository(UserRepository.class, User.class, session);
        SwingUtilities.invokeLater(() -> {
            loginForm = new LoginForm(frame, userRepository);
            loginForm.setVisible(true);
            MainClass.USER = loginForm.getUser();
            if (MainClass.USER == null) {
                log.warn("User is null");
                System.exit(0);
            }
            createFrame();
        });
        dashboardController = new DashboardController(getDashboardView(), frame, userRepository, session);
    }

    private DashboardView getDashboardView() {
        if (dashboardView == null) {
            log.info("DashboardView created");
            dashboardView = new DefaultDashboardView();
        }
        return dashboardView;
    }

    private void createFrame(){
        frame = new JFrame();
        frame.setIconImage(new FlatSVGIcon("svgs/SistemaContableLogo.svg", 250, 250).getImage());
        frame.setTitle("Sistema Contable - " + MainClass.USER.getFirstName() + " " + MainClass.USER.getLastName());
        frame.setSize(1300, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        log.info("Frame created");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(getDashboardView());
        frame.getRootPane().setBackground(Color.WHITE);
        frame.setVisible(true);
    }


}
