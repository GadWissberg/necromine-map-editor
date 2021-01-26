package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.editor.desktop.toolbar.ToolbarButtonDefinition;
import com.gadarts.necromine.editor.desktop.toolbar.ToolbarButtonOfMenuItem;

public class SubToolbarButtonDefinition implements ToolbarButtonDefinition {
	private final ToolbarButtonOfMenuItem buttonProperties;

	public SubToolbarButtonDefinition(final ToolbarButtonOfMenuItem buttonProperties) {
		this.buttonProperties = buttonProperties;
	}

	@Override
	public ToolbarButtonOfMenuItem getButtonProperties() {
		return buttonProperties;
	}
}
