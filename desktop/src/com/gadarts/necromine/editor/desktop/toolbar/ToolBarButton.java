package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.menu.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menu.MenuItemProperties;

import javax.swing.*;
import java.util.Optional;

public class ToolBarButton extends JButton {

	public ToolBarButton(final ImageIcon imageIcon,
						 final ToolbarButtonOfMenuItem buttonDefinition,
						 final ModesHandler modesHandler) {
		super(imageIcon);
		MenuItemDefinition menuItemDefinition = buttonDefinition.getMenuItemDefinition();
		Optional.ofNullable(menuItemDefinition).ifPresentOrElse(menuItem -> {
			MenuItemProperties menuItemProperties = menuItem.getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			MapperCommand action = (MapperCommand) menuItemProperties.getAction();
			action.setModesHandler(modesHandler);
			addActionListener(action);
		}, () -> addActionListener(buttonDefinition.getMapperCommand()));
	}


}
