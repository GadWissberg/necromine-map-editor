package com.gadarts.necromine.editor.desktop.menu.definitions;

import com.gadarts.necromine.editor.desktop.commands.SetModeCommand;
import com.gadarts.necromine.editor.desktop.menu.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.menu.definitions.Menus.Constants;
import com.necromine.editor.EditModes;

public enum EditMenuItemsDefinitions implements MenuItemDefinition {
    MODE_TILE(new MenuItemProperties("Tiles Mode", new SetModeCommand(EditModes.TILES), "mode_tile", Constants.BUTTON_GROUP_MODES)),
    MODE_CHARACTER(new MenuItemProperties("Characters Mode", new SetModeCommand(EditModes.CHARACTERS), "mode_character", Constants.BUTTON_GROUP_MODES)),
    MODE_ENV(new MenuItemProperties("Environment Mode", new SetModeCommand(EditModes.ENVIRONMENT), "mode_env", Constants.BUTTON_GROUP_MODES)),
    MODE_PICKUPS(new MenuItemProperties("Pick-Ups Mode", new SetModeCommand(EditModes.PICKUPS), "mode_pickup", Constants.BUTTON_GROUP_MODES)),
    MODE_LIGHTS(new MenuItemProperties("Lights Mode", new SetModeCommand(EditModes.LIGHTS), "mode_pickup", Constants.BUTTON_GROUP_MODES));
    private final MenuItemProperties menuItemProperties;

    EditMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
        this.menuItemProperties = menuItemProperties;
    }

    @Override
    public MenuItemProperties getMenuItemProperties() {
        return menuItemProperties;
    }


}