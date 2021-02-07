package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.menu.MenuItemDefinition;
import lombok.Getter;

@Getter
public class ToolbarButtonOfMenuItem {
    private final String icon;
    private final String toolTip;
    private final MenuItemDefinition menuItemDefinition;
    private MapperCommand mapperCommand;


    public ToolbarButtonOfMenuItem(final String icon, final String toolTip, final MapperCommand mapperCommand) {
        this(icon, toolTip, (MenuItemDefinition) null);
        this.mapperCommand = mapperCommand;
    }

    public ToolbarButtonOfMenuItem(final String icon,
                                   final String toolTip,
                                   final MenuItemDefinition mapperMenuItemDefinition) {
        this.icon = icon;
        this.toolTip = toolTip;
        this.menuItemDefinition = mapperMenuItemDefinition;
    }

}
