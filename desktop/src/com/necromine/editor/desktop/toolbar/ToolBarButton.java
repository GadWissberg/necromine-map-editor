package com.necromine.editor.desktop.toolbar;

import com.necromine.editor.desktop.menuitems.MenuItemProperties;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ToolBarButton extends JButton {

	public ToolBarButton(final ImageIcon imageIcon, final ToolbarButtonOfMenuItem buttonDefinition) {
		super(imageIcon);
		MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
		setEnabled(!menuItemProperties.isDisabledOnStart());
		MapperActionListener action = (MapperActionListener) menuItemProperties.getAction();
//		action.set
		addActionListener(action);
	}


}
