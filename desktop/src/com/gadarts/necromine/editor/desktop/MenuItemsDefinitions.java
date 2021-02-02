package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.editor.desktop.actions.SetModeCommand;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemProperties;
import com.necromine.editor.CameraModes;
import com.necromine.editor.EditModes;

public enum MenuItemsDefinitions implements MenuItemDefinition {
    MODE_TILE(new MenuItemProperties("Tiles Mode", new SetModeCommand(EditModes.TILES), "mode_tile")),
    MODE_CHARACTER(new MenuItemProperties("Characters Mode", new SetModeCommand(EditModes.CHARACTERS), "mode_character")),
    MODE_ENV(new MenuItemProperties("Environment Mode", new SetModeCommand(EditModes.ENVIRONMENT), "mode_env")),
    MODE_PICKUPS(new MenuItemProperties("Pick-Ups Mode", new SetModeCommand(EditModes.PICKUPS), "mode_pickup")),
    MODE_LIGHTS(new MenuItemProperties("Lights Mode", new SetModeCommand(EditModes.LIGHTS), "mode_pickup")),
    CAMERA_PAN(new MenuItemProperties("Pan Camera", new SetModeCommand(CameraModes.PAN), "camera_pan")),
    CAMERA_ROTATE(new MenuItemProperties("Rotate Camera", new SetModeCommand(CameraModes.ROTATE), "camera_rotate")),
    CAMERA_ZOOM(new MenuItemProperties("Zoom Camera", new SetModeCommand(CameraModes.ZOOM), "camera_zoom"));

    private final MenuItemProperties menuItemProperties;

    MenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
        this.menuItemProperties = menuItemProperties;
    }

    @Override
    public MenuItemProperties getMenuItemProperties() {
        return menuItemProperties;
    }
}
