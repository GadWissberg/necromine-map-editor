package com.necromine.editor.desktop.toolbar;

import com.necromine.editor.desktop.menuitems.MenuItemDefinition;
import lombok.Getter;

@Getter
public class ToolbarButtonOfMenuItem {
	private final String icon;
	private final String toolTip;
	private final MenuItemDefinition menuItemDefinition;
	private final String buttonGroup;

	public ToolbarButtonOfMenuItem(final String icon,
								   final String toolTip,
								   final MenuItemDefinition mapperMenuItemDefinition,
								   final String buttonGroup) {
		this.icon = icon;
		this.toolTip = toolTip;
		this.menuItemDefinition = mapperMenuItemDefinition;
		this.buttonGroup = buttonGroup;
	}


}
