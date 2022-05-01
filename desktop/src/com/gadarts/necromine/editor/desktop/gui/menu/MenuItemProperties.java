package com.gadarts.necromine.editor.desktop.gui.menu;

import lombok.Getter;

import java.awt.event.ActionListener;

@Getter
public class MenuItemProperties {
    private final String label;
    private final ActionListener action;
    private final boolean disabledOnStart;
    private final String icon;
    private final String buttonGroup;

    public MenuItemProperties(final String label, final ActionListener action, final String icon) {
        this(label, action, icon, false, null);
    }

    public MenuItemProperties(final String label, final ActionListener action, final String icon, final String buttonGroup) {
        this(label, action, icon, false, buttonGroup);
    }

    public MenuItemProperties(final String label,
                              final ActionListener action,
                              final String icon,
                              final boolean disabledOnStart,
                              final String buttonGroup) {
        this.label = label;
        this.action = action;
        this.disabledOnStart = disabledOnStart;
        this.icon = icon;
        this.buttonGroup = buttonGroup;
    }


}
