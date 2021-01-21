package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.menuitems.MenuItemProperties;

import javax.swing.*;

public class ToolBarButton extends JButton {

	public ToolBarButton(final ImageIcon imageIcon,
						 final ToolbarButtonOfMenuItem buttonDefinition,
						 final ModesHandler modesHandler) {
		super(imageIcon);
		MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
		setEnabled(!menuItemProperties.isDisabledOnStart());
		MapperActionListener action = (MapperActionListener) menuItemProperties.getAction();
		action.setModesHandler(modesHandler);
		addActionListener(action);
	}


}
