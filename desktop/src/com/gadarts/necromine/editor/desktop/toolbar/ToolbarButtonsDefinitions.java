package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.MenuItemsDefinitions;

public enum ToolbarButtonsDefinitions implements ToolbarButtonDefinition {
	MODE_TILE(new ToolbarButtonOfMenuItem("tile_mode", "Tiles Mode", MenuItemsDefinitions.MODE_TILE, Constants.BUTTON_GROUP_MODE)),
	MODE_OBJECT(new ToolbarButtonOfMenuItem("object_mode", "Objects Mode", MenuItemsDefinitions.MODE_OBJECT, Constants.BUTTON_GROUP_MODE));

	private final ToolbarButtonOfMenuItem toolbarButtonOfMenuItem;

	ToolbarButtonsDefinitions(final ToolbarButtonOfMenuItem toolbarButtonOfMenuItem) {
		this.toolbarButtonOfMenuItem = toolbarButtonOfMenuItem;
	}

	@Override
	public ToolbarButtonOfMenuItem getButtonProperties() {
		return toolbarButtonOfMenuItem;
	}

	public static class Constants {
		public static final String BUTTON_GROUP_MODE = "modes";
	}
}
