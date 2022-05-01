package com.gadarts.necromine.editor.desktop.gui.menu.definitions;

import com.gadarts.necromine.editor.desktop.commands.LoadMapCommand;
import com.gadarts.necromine.editor.desktop.commands.NewMapCommand;
import com.gadarts.necromine.editor.desktop.commands.SaveMapCommand;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;

public enum FileMenuItemsDefinitions implements MenuItemDefinition {
	NEW(new MenuItemProperties("New", new NewMapCommand(), "file_new")),
	SAVE(new MenuItemProperties("Save", new SaveMapCommand(), "file_save")),
	LOAD(new MenuItemProperties("Load", new LoadMapCommand(), "file_load"));

	private final MenuItemProperties menuItemProperties;

	FileMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
