package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;

import javax.swing.*;
import java.util.Optional;

public class ToolBarButton extends JButton {

	public ToolBarButton(final ImageIcon imageIcon,
						 final ToolbarButtonProperties buttonDefinition) {
		super(imageIcon);
		MenuItemDefinition menuItemDefinition = buttonDefinition.getMenuItemDefinition();
		Optional.ofNullable(menuItemDefinition).ifPresentOrElse(menuItem -> {
			MenuItemProperties menuItemProperties = menuItem.getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			MapperCommand action = (MapperCommand) menuItemProperties.getAction();
			addActionListener(action);
		}, () -> addActionListener(buttonDefinition.getMapperCommand()));
	}


}
