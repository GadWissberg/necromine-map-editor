package com.gadarts.necromine.editor.desktop.gui.toolbar.sub;

import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonDefinition;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;

public class SubToolbarButtonDefinition implements ToolbarButtonDefinition {
	private final ToolbarButtonProperties buttonProperties;

	public SubToolbarButtonDefinition(final ToolbarButtonProperties buttonProperties) {
		this.buttonProperties = buttonProperties;
	}

	public SubToolbarButtonDefinition() {
		this(null);
	}

	@Override
	public ToolbarButtonProperties getButtonProperties() {
		return buttonProperties;
	}
}
