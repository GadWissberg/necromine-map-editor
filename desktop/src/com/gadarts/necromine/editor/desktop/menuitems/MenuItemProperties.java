package com.gadarts.necromine.editor.desktop.menuitems;

import lombok.Getter;

import java.awt.event.ActionListener;

@Getter
public class MenuItemProperties {
	private final String label;
	private final ActionListener action;
	private final boolean disabledOnStart;
	private final String icon;

	public MenuItemProperties(final String label, final ActionListener action, final String icon) {
		this(label, action, false, icon);
	}

	public MenuItemProperties(final String label,
							  final ActionListener action,
							  final boolean disabledOnStart,
							  final String icon) {
		this.label = label;
		this.action = action;
		this.disabledOnStart = disabledOnStart;
		this.icon = icon;
	}


}
