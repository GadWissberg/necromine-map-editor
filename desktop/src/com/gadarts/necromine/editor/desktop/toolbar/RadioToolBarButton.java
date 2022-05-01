package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;

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
