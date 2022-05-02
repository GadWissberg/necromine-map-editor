package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class RadioToolBarButton extends JToggleButton {

	public RadioToolBarButton(final ImageIcon imageIcon, final ToolbarButtonProperties buttonDefinition) {
		super(imageIcon);
		ActionListener action = createAction(buttonDefinition);
		addActionListener(action);
	}

	private ActionListener createAction(ToolbarButtonProperties buttonDefinition) {
		if (buttonDefinition.getMenuItemDefinition() != null) {
			MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			try {
				return (ActionListener) menuItemProperties.getActionClass().getConstructors()[0].newInstance();
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		} else {
			return buttonDefinition.getMapperCommand();
		}
	}


}
