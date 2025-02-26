package com.nutrehogar.sistemacontable.application;

import com.nutrehogar.sistemacontable.application.controller.AuthController;
import com.nutrehogar.sistemacontable.application.view.AuthView;
import com.nutrehogar.sistemacontable.domain.Permissions;
import com.nutrehogar.sistemacontable.ui.view.service.LoginForm;
import com.nutrehogar.sistemacontable.ui.view.service.DefaultDashboardView;
import com.nutrehogar.sistemacontable.application.config.AppConfig;
import com.nutrehogar.sistemacontable.domain.core.WriteExecutor;
import com.nutrehogar.sistemacontable.domain.model.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.application.repository.UserRepository;
import com.nutrehogar.sistemacontable.domain.repository.UserRepo;
import com.nutrehogar.sistemacontable.infrastructure.persistence.HibernateUtil;
import com.nutrehogar.sistemacontable.application.controller.service.DashboardController;
import com.nutrehogar.sistemacontable.ui.ThemeConfig;
import com.nutrehogar.sistemacontable.application.view.service.DashboardView;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.swing.*;

@Slf4j
public class App {
    private Session session;
    private UserRepository userRepository;
    private AuthController authController;
    private AuthView authView;
    private JFrame frame;
    private User user;

    public App() {
        frame = new JFrame();
        frame.setIconImage(new FlatSVGIcon("svgs/SistemaContableLogo.svg", 250, 250).getImage());
        frame.setTitle("Sistema Contable");
        frame.setSize(1300, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBackground(ThemeConfig.Palette.SOLITUDE_50);
        frame.setVisible(true);
        userRepository = new UserRepo();
        authView = new LoginForm(frame);
        authController = new AuthController(
                authView,
                userRepository,
                User.builder().isEnable(true).username("Root").password("0922").permissions(Permissions.CREATE).build()
        );
        user = authController.getAuthenticatedUser();
        frame.setTitle("Sistema Contable - " + user.getUsername());
        try (var context = AppConfig.init(HibernateUtil.getSession(), user, frame)) {
            context.registerBean(UserRepository.class, userRepository);
            DashboardView dashboardView = new DefaultDashboardView();
            DashboardController dashboardController = new DashboardController(dashboardView, context);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
