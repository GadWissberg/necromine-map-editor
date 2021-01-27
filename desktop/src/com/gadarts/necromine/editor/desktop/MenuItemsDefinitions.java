package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.editor.desktop.actions.SetModeCommand;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemProperties;
import com.necromine.editor.EditorModes;

public enum MenuItemsDefinitions implements MenuItemDefinition {
	MODE_TILE(new MenuItemProperties("Tiles Mode", new SetModeCommand(EditorModes.TILES), "mode_tile")),
	MODE_CHARACTER(new MenuItemProperties("Characters Mode", new SetModeCommand(EditorModes.CHARACTERS), "mode_character")),
	MODE_ENV(new MenuItemProperties("Environment Mode", new SetModeCommand(EditorModes.ENVIRONMENT), "mode_env"));

	private final MenuItemProperties menuItemProperties;

	MenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
