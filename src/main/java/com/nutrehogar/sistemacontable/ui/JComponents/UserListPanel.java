package com.nutrehogar.sistemacontable.ui.JComponents;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.ThemeConfig;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Getter
public class UserListPanel extends JPanel {
    private List<User> users;
    private User userSelected;
    private Consumer<User> selectUser;

    public UserListPanel(List<User> users) {
        this.users = users == null ? List.of() : users;
        selectUser = (user) -> {
            userSelected = user;
            userChange();
            updateSelection();
        };
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(ThemeConfig.Spacing.BORDER_MD);
        assert users != null;
        setList(users);
    }

    public UserListPanel() {
        this(List.of());
    }

    public void userChange() {
        log.info("User Selected: {}", userSelected);
    }

    public void setList(@NotNull List<User> users) {
        removeAll();
        if (users.isEmpty()) return;
        boolean first = true;
        for (var user : users) {
            var element = new UserListElement(user, selectUser);
            if (first) {
                element.setSelected(true);
                userSelected = user;
                first = false;
            }
            add(element);
            add(Box.createVerticalStrut(12));
        }
        updateSelection();
    }


    private void updateSelection() {
        for (var comp : getComponents()) {
            if (comp instanceof UserListElement element) {
                element.setSelected(userSelected != null && userSelected.equals(element.getUser()));
                element.revalidate();
                element.repaint();
            }
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Getter
    public static class UserListElement extends JLabel {
        public static final FlatSVGIcon userIcon = new FlatSVGIcon("svgs/user.svg", ThemeConfig.ICON_MD, ThemeConfig.ICON_MD);
        public static final FlatSVGIcon userDisableIcon = new FlatSVGIcon("svgs/user_disable.svg", ThemeConfig.ICON_MD, ThemeConfig.ICON_MD);
        private final User user;
        private boolean isSelected = false;

        public UserListElement(@NotNull User user, Consumer<User> action) {
            this.user = user;
            setOpaque(true);
            setAlignmentX(Component.LEFT_ALIGNMENT); // 🔹 Alinear al lado izquierdo
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 67)); // 🔹 Expandir en X
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalAlignment(SwingConstants.LEADING);
            setText(user.getUsername());
            setIconTextGap(ThemeConfig.Spacing.GAP_MD);
            setFont(ThemeConfig.Typography.FONT_LG);
            setForeground(user.isEnable() ? ThemeConfig.Palette.OFFICE_GREEN : ThemeConfig.Palette.PLATINUM);
            setBackground(user.isEnable() ? Color.WHITE : ThemeConfig.Palette.WHITE_SMOKE);
            setIcon(user.isEnable() ? userIcon : userDisableIcon);
            setBorder(ThemeConfig.Spacing.BORDER_MD);
            if (user.isEnable()) addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.accept(user);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isSelected) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        setBackground(ThemeConfig.Palette.COLUMBIA_BLUE);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isSelected) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        setBackground(Color.WHITE);
                    }
                }
            });

        }

        public void setSelected(boolean selected) {
            if (!user.isEnable()) return;
            this.isSelected = selected;
            if (selected) {
                setBackground(ThemeConfig.Palette.URANIAN_BLUE);
                setForeground(ThemeConfig.Palette.TRUE_BLUE);
            } else {
                setBackground(Color.WHITE);
                setForeground(ThemeConfig.Palette.OFFICE_GREEN);
            }
        }

    }


}
