package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.editor.desktop.actions.SetModeCommand;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemProperties;
import com.necromine.editor.EditorModes;

public enum MenuItemsDefinitions implements MenuItemDefinition {
	MODE_TILE(new MenuItemProperties("Tiles Mode", new SetModeCommand(EditorModes.TILES), "tile_mode")),
	MODE_OBJECT(new MenuItemProperties("Objects Mode", new SetModeCommand(EditorModes.CHARACTERS), "object_mode"));
	private final MenuItemProperties menuItemProperties;

	MenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
