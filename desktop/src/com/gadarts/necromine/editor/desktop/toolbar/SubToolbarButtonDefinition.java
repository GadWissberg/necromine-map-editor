package com.gadarts.necromine.editor.desktop.toolbar;

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
