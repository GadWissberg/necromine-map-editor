package com.gadarts.necromine.editor.desktop.toolbar;

public class SubToolbarButtonDefinition implements ToolbarButtonDefinition {
	private final ToolbarButtonProperties buttonProperties;

	public SubToolbarButtonDefinition(final ToolbarButtonProperties buttonProperties) {
		this.buttonProperties = buttonProperties;
	}

	@Override
	public ToolbarButtonProperties getButtonProperties() {
		return buttonProperties;
	}
}
