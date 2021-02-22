package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.menu.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menu.MenuItemProperties;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RadioToolBarButton extends JToggleButton {

	public RadioToolBarButton(final ImageIcon imageIcon, final ToolbarButtonProperties buttonDefinition) {
		super(imageIcon);
		MenuItemDefinition menuItemDefinition = buttonDefinition.getMenuItemDefinition();
		ActionListener action;
		if (menuItemDefinition != null) {
			MenuItemProperties menuItemProperties = menuItemDefinition.getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			action = menuItemProperties.getAction();
		} else {
			action = buttonDefinition.getMapperCommand();
		}
		addActionListener(action);
	}


}
