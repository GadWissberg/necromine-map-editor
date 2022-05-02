package com.gadarts.necromine.editor.desktop.gui.menu.definitions;

import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;

public enum ViewMenuItemsDefinitions implements MenuItemDefinition {
//    CAMERA_PAN(new MenuItemProperties("Pan Camera", new SetModeCommand(CameraModes.PAN), "camera_pan", Menus.Constants.BUTTON_GROUP_MODES)),
//    CAMERA_ROTATE(new MenuItemProperties("Rotate Camera", new SetModeCommand(CameraModes.ROTATE), "camera_rotate", Menus.Constants.BUTTON_GROUP_MODES)),
//    CAMERA_ZOOM(new MenuItemProperties("Zoom Camera", new SetModeCommand(CameraModes.ZOOM), "camera_zoom", Menus.Constants.BUTTON_GROUP_MODES));
	;
	private final MenuItemProperties menuItemProperties;

	ViewMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
