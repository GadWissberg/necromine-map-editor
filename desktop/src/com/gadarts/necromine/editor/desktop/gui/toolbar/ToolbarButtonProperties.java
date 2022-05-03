package com.gadarts.necromine.editor.desktop.gui.toolbar;

import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import lombok.Getter;

@Getter
public class ToolbarButtonProperties {
	private final String icon;
	private final String toolTip;
	private final MenuItemDefinition menuItemDefinition;
	private String buttonGroup;
	private MapperCommand mapperCommand;


	public ToolbarButtonProperties(final String icon, final String toolTip, final MapperCommand mapperCommand) {
		this(icon, toolTip, mapperCommand, null);
	}

	public ToolbarButtonProperties(final String icon,
								   final String toolTip,
								   final MapperCommand mapperCommand,
								   final String buttonGroup) {
		this(icon, toolTip, (MenuItemDefinition) null);
		this.mapperCommand = mapperCommand;
		this.buttonGroup = buttonGroup;
	}

	public ToolbarButtonProperties(final String icon,
								   final String toolTip,
								   final MenuItemDefinition mapperMenuItemDefinition) {
		this.icon = icon;
		this.toolTip = toolTip;
		this.menuItemDefinition = mapperMenuItemDefinition;
		this.buttonGroup = null;
	}

}
