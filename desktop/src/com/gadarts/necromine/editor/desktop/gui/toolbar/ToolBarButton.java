package com.gadarts.necromine.editor.desktop.gui.toolbar;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.necromine.editor.MapRenderer;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties.createButtonIcon;

public class ToolBarButton extends JButton {

	public ToolBarButton(ToolbarButtonProperties properties,
						 PersistenceManager persistenceManager,
						 MapRenderer mapRenderer,
						 ModesManager modesManager,
						 DialogsManager dialogsManager) throws IOException {
		setIcon(createButtonIcon(properties));
		MenuItemDefinition menuItemDefinition = properties.getMenuItemDefinition();
		Optional.ofNullable(menuItemDefinition).ifPresentOrElse(menuItem -> {
			MenuItemProperties menuItemProperties = menuItem.getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			MapperCommand action;
			try {
				Constructor<?> constructor = menuItemProperties.getActionClass().getConstructors()[0];
				action = (MapperCommand) constructor.newInstance(
						persistenceManager,
						mapRenderer,
						modesManager,
						dialogsManager);
			} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			addActionListener(action);
		}, ( ) -> addActionListener(properties.getMapperCommand()));
	}


}
