package com.nutrehogar.sistemacontable.ui.JComponents;

import com.nutrehogar.sistemacontable.domain.model.User;

import javax.swing.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.ThemeConfig;
import com.nutrehogar.sistemacontable.ui.components.UserListCellRenderer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Getter
public class JLIstUser extends JPanel {
    public static void main(String[] args) {
        var jl = new JLIstUser(List.of(
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().build(),
                User.builder().build(),
                User.builder().build(),
                User.builder().build(),
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().username("Yospeh").isEnable(true).build(),
                User.builder().username("Yospeh").isEnable(true).build()
        ));
        var frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(jl, BorderLayout.CENTER);
        frame.add(new JButton("Hola"), BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setResizable(false);
    }
    private List<User> users;
    private User userSelected;
    private Consumer<User> selectUser;
    private JList<User> userJList;

    public JLIstUser(List<User> users) {
        this.users = users == null ? List.of() : users;
        selectUser = (user) -> {
            userSelected = user;
            userChange();
            updateSelection();
        };
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(ThemeConfig.Spacing.BORDER_MD);
        assert users != null;
        setList(users);
    }

    public JLIstUser() {
        this(List.of());
    }

    public void userChange() {
        log.info("User Selected: {}", userSelected);
    }

    public void setList(@NotNull List<User> users) {
        removeAll();
        if (users.isEmpty()) return;

        DefaultListModel<User> listModel = new DefaultListModel<>();
        for (User user : users) {
            listModel.addElement(user);
        }

        userJList = new JList<>(listModel);
        userJList.setCellRenderer(new UserListCellRenderer());
        userJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                User selectedUser = userJList.getSelectedValue();
                if (selectedUser != null) {
                    selectUser.accept(selectedUser);
                }
            }
        });

        add(new JScrollPane(userJList), BorderLayout.CENTER);
        updateSelection();
    }

    private void updateSelection() {
        if (userSelected != null) {
            userJList.setSelectedValue(userSelected, true);
        }
    }
}