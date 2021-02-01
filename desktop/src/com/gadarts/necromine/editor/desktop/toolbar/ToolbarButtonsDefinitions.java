package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.MenuItemsDefinitions;

public enum ToolbarButtonsDefinitions implements ToolbarButtonDefinition {
    MODE_TILE(new ToolbarButtonOfMenuItem("mode_tile", "Tiles Mode", MenuItemsDefinitions.MODE_TILE, Constants.BUTTON_GROUP_MODE)),
    MODE_CHARACTER(new ToolbarButtonOfMenuItem("mode_character", "Characters Mode", MenuItemsDefinitions.MODE_CHARACTER, Constants.BUTTON_GROUP_MODE)),
    MODE_ENV(new ToolbarButtonOfMenuItem("mode_env", "Environment Objects Mode", MenuItemsDefinitions.MODE_ENV, Constants.BUTTON_GROUP_MODE)),
    MODE_PICKUP(new ToolbarButtonOfMenuItem("mode_pickup", "Pick-Ups Mode", MenuItemsDefinitions.MODE_PICKUPS, Constants.BUTTON_GROUP_MODE)),
    MODE_LIGHTS(new ToolbarButtonOfMenuItem("mode_pickup", "Lights Mode", MenuItemsDefinitions.MODE_LIGHTS, Constants.BUTTON_GROUP_MODE));

    private final ToolbarButtonOfMenuItem toolbarButtonOfMenuItem;

    ToolbarButtonsDefinitions(final ToolbarButtonOfMenuItem toolbarButtonOfMenuItem) {
        this.toolbarButtonOfMenuItem = toolbarButtonOfMenuItem;
    }

    @Override
    public ToolbarButtonOfMenuItem getButtonProperties() {
        return toolbarButtonOfMenuItem;
    }

    public static final class Constants {
        public static final String BUTTON_GROUP_MODE = "modes";

        private Constants() {
        }
    }
}
