package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.toolbar.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class ToolBarButton extends JButton {

	public ToolBarButton(ImageIcon imageIcon,
						 ToolbarButtonProperties buttonDefinition,
						 PersistenceManager persistenceManager,
						 GuiEventsSubscriber guiEventsSubscriber, ModesHandler modesHandler) {
		super(imageIcon);
		MenuItemDefinition menuItemDefinition = buttonDefinition.getMenuItemDefinition();
		Optional.ofNullable(menuItemDefinition).ifPresentOrElse(menuItem -> {
			MenuItemProperties menuItemProperties = menuItem.getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			MapperCommand action;
			try {
				Constructor<?> constructor = menuItemProperties.getActionClass().getConstructors()[0];
				action = (MapperCommand) constructor.newInstance(persistenceManager, guiEventsSubscriber, modesHandler);
			} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			addActionListener(action);
		}, () -> addActionListener(buttonDefinition.getMapperCommand()));
	}


}
