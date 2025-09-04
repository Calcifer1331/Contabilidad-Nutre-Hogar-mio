package com.nutrehogar.sistemacontable.ui.components;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.ui.ThemeConfig;

import javax.swing.*;
import java.awt.*;

public class UserListCellRenderer extends DefaultListCellRenderer {
    private static final FlatSVGIcon userIcon = new FlatSVGIcon("svgs/user.svg", ThemeConfig.ICON_MD, ThemeConfig.ICON_MD);
    private static final FlatSVGIcon userDisableIcon = new FlatSVGIcon("svgs/user_disable.svg", ThemeConfig.ICON_MD, ThemeConfig.ICON_MD);

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof User user) {
            setText(user.getUsername());
            setIcon(user.isEnable() ? userIcon : userDisableIcon);
            setForeground(user.isEnable() ? ThemeConfig.Palette.OFFICE_GREEN : ThemeConfig.Palette.PLATINUM);
            setBackground(isSelected ? ThemeConfig.Palette.URANIAN_BLUE : user.isEnable() ? Color.WHITE : ThemeConfig.Palette.WHITE_SMOKE);
            setFont(ThemeConfig.Typography.FONT_LG);
            setIconTextGap(ThemeConfig.Spacing.GAP_MD);
            setBorder(ThemeConfig.Spacing.BORDER_MD);
        }
        return this;
    }
}