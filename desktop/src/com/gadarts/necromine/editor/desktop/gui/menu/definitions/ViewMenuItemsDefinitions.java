package com.gadarts.necromine.editor.desktop.gui.menu.definitions;

import com.gadarts.necromine.editor.desktop.gui.commands.mode.view.SetPanModeCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.mode.view.SetRotateModeCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.mode.view.SetZoomModeCommand;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;

import static com.gadarts.necromine.editor.desktop.gui.menu.definitions.Menus.Constants.BUTTON_GROUP_MODES;

public enum ViewMenuItemsDefinitions implements MenuItemDefinition {
	CAMERA_PAN(new MenuItemProperties("Pan Camera", SetPanModeCommand.class, "camera_pan", BUTTON_GROUP_MODES)),
	CAMERA_ROTATE(new MenuItemProperties("Rotate Camera", SetRotateModeCommand.class, "camera_rotate", BUTTON_GROUP_MODES)),
	CAMERA_ZOOM(new MenuItemProperties("Zoom Camera", SetZoomModeCommand.class, "camera_zoom", BUTTON_GROUP_MODES));
	private final MenuItemProperties menuItemProperties;

	ViewMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
