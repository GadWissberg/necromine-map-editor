package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.menu.MenuItemProperties;

import javax.swing.*;

public class RadioToolBarButton extends JToggleButton {

	public RadioToolBarButton(final ImageIcon imageIcon, final ToolbarButtonOfMenuItem buttonDefinition) {
		super(imageIcon);
		MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
		setEnabled(!menuItemProperties.isDisabledOnStart());
		addActionListener(menuItemProperties.getAction());
	}


}
