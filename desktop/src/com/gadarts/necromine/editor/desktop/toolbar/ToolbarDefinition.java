package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.menu.definitions.EditMenuItemsDefinitions;
import com.gadarts.necromine.editor.desktop.menu.definitions.FileMenuItemsDefinitions;
import com.gadarts.necromine.editor.desktop.menu.definitions.ViewMenuItemsDefinitions;

public enum ToolbarDefinition implements ToolbarButtonDefinition {
    SAVE(new ToolbarButtonOfMenuItem(
            "file_save",
            "Save Map",
            FileMenuItemsDefinitions.SAVE)),

    LOAD(new ToolbarButtonOfMenuItem(
            "file_load",
            "Load Map",
            FileMenuItemsDefinitions.LOAD)),

    SEPARATOR_1(),

    MODE_TILE(new ToolbarButtonOfMenuItem(
            "mode_tile",
            "Tiles Mode",
            EditMenuItemsDefinitions.MODE_TILE)),

    MODE_CHARACTER(new ToolbarButtonOfMenuItem(
            "mode_character",
            "Characters Mode",
            EditMenuItemsDefinitions.MODE_CHARACTER)),

    MODE_ENV(new ToolbarButtonOfMenuItem(
            "mode_env",
            "Environment Objects Mode",
            EditMenuItemsDefinitions.MODE_ENV)),

    MODE_PICKUP(new ToolbarButtonOfMenuItem(
            "mode_pickup",
            "Pick-Ups Mode",
            EditMenuItemsDefinitions.MODE_PICKUPS)),

    MODE_LIGHTS(new ToolbarButtonOfMenuItem(
            "mode_light",
            "Lights Mode",
            EditMenuItemsDefinitions.MODE_LIGHTS)),

    SEPARATOR_2(),

    CAMERA_PAN(new ToolbarButtonOfMenuItem(
            "camera_pan",
            "Pan Camera",
            ViewMenuItemsDefinitions.CAMERA_PAN)),

    CAMERA_ROTATE(new ToolbarButtonOfMenuItem(
            "camera_rotate",
            "Rotate Camera",
            ViewMenuItemsDefinitions.CAMERA_ROTATE)),

    CAMERA_ZOOM(new ToolbarButtonOfMenuItem(
            "camera_zoom",
            "Zoom Camera",
            ViewMenuItemsDefinitions.CAMERA_ZOOM)),

    SEPARATOR_3();

    private final ToolbarButtonOfMenuItem toolbarButtonOfMenuItem;

    ToolbarDefinition() {
        this(null);
    }

    ToolbarDefinition(final ToolbarButtonOfMenuItem toolbarButtonOfMenuItem) {
        this.toolbarButtonOfMenuItem = toolbarButtonOfMenuItem;
    }

    @Override
    public ToolbarButtonOfMenuItem getButtonProperties() {
        return toolbarButtonOfMenuItem;
    }


}
