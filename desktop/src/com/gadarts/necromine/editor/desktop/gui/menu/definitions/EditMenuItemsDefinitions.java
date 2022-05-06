package com.gadarts.necromine.editor.desktop.gui.menu.definitions;

import com.gadarts.necromine.editor.desktop.gui.commands.OpenAmbientLightDialogCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.OpenMapSizeDialogCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.mode.edit.*;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;

import static com.gadarts.necromine.editor.desktop.gui.menu.definitions.Menus.Constants.BUTTON_GROUP_MODES;

public enum EditMenuItemsDefinitions implements MenuItemDefinition {
	MODE_TILE(new MenuItemProperties("Tiles Mode", SetTilesModeCommand.class, "mode_tile", BUTTON_GROUP_MODES)),
	MODE_CHARACTER(new MenuItemProperties("Characters Mode", SetCharactersModeCommand.class, "mode_character", BUTTON_GROUP_MODES)),
	MODE_ENV(new MenuItemProperties("Environment Mode", SetEnvironmentModeCommand.class, "mode_env", BUTTON_GROUP_MODES)),
	MODE_PICKUPS(new MenuItemProperties("Pick-Ups Mode", SetPickupsModeCommand.class, "mode_pickup", BUTTON_GROUP_MODES)),
	MODE_LIGHTS(new MenuItemProperties("Lights Mode", SetLightsModeCommand.class, "mode_light", BUTTON_GROUP_MODES)),
	SET_AMBIENT_LIGHT(new MenuItemProperties("Set Ambient Light", OpenAmbientLightDialogCommand.class, "ambient_light")),
	SET_MAP_SIZE(new MenuItemProperties("Set Map Size", OpenMapSizeDialogCommand.class, "size"));
	private final MenuItemProperties menuItemProperties;

	EditMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties( ) {
		return menuItemProperties;
	}


}
