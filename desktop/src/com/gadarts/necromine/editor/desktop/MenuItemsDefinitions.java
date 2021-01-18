package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.editor.desktop.actions.SetModeAction;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemProperties;
import com.necromine.editor.EditorModes;

public enum MenuItemsDefinitions implements MenuItemDefinition {
	MODE_TILE(new MenuItemProperties("Tiles Mode", new SetModeAction(EditorModes.TILES), "tile_mode")),
	MODE_OBJECT(new MenuItemProperties("Objects Mode", new SetModeAction(EditorModes.OBJECTS), "object_mode"));
	private final MenuItemProperties menuItemProperties;

	MenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
