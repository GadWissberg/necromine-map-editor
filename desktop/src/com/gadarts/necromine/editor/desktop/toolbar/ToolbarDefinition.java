package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.MenuItemsDefinitions;

public enum ToolbarDefinition implements ToolbarButtonDefinition {
	SAVE(new ToolbarButtonOfMenuItem(
			"save",
			"Save Map",
			MenuItemsDefinitions.SAVE)),

	SEPARATOR_1(),

	MODE_TILE(new ToolbarButtonOfMenuItem(
			"mode_tile",
			"Tiles Mode",
			MenuItemsDefinitions.MODE_TILE,
			Constants.BUTTON_GROUP_MODES)),

	MODE_CHARACTER(new ToolbarButtonOfMenuItem(
			"mode_character",
			"Characters Mode",
			MenuItemsDefinitions.MODE_CHARACTER,
			Constants.BUTTON_GROUP_MODES)),

	MODE_ENV(new ToolbarButtonOfMenuItem(
			"mode_env",
			"Environment Objects Mode",
			MenuItemsDefinitions.MODE_ENV,
			Constants.BUTTON_GROUP_MODES)),

	MODE_PICKUP(new ToolbarButtonOfMenuItem(
			"mode_pickup",
			"Pick-Ups Mode",
			MenuItemsDefinitions.MODE_PICKUPS,
			Constants.BUTTON_GROUP_MODES)),

	MODE_LIGHTS(new ToolbarButtonOfMenuItem(
			"mode_light",
			"Lights Mode",
			MenuItemsDefinitions.MODE_LIGHTS,
			Constants.BUTTON_GROUP_MODES)),

	SEPARATOR_2(),

	CAMERA_PAN(new ToolbarButtonOfMenuItem(
			"camera_pan",
			"Pan Camera",
			MenuItemsDefinitions.CAMERA_PAN,
			Constants.BUTTON_GROUP_MODES)),

	CAMERA_ROTATE(new ToolbarButtonOfMenuItem(
			"camera_rotate",
			"Rotate Camera",
			MenuItemsDefinitions.CAMERA_ROTATE,
			Constants.BUTTON_GROUP_MODES)),

	CAMERA_ZOOM(new ToolbarButtonOfMenuItem(
			"camera_zoom",
			"Zoom Camera",
			MenuItemsDefinitions.CAMERA_ZOOM,
			Constants.BUTTON_GROUP_MODES)),

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

	public static final class Constants {
		public static final String BUTTON_GROUP_MODES = "modes";

		private Constants() {
		}
	}
}
