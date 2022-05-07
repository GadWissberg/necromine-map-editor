package com.gadarts.necromine.editor.desktop.gui.toolbar;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.necromine.editor.MapRenderer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties.createButtonIcon;

public class ToolBarButton extends JButton {

	public ToolBarButton(ToolbarButtonProperties properties,
						 PersistenceManager persistenceManager,
						 MapRenderer mapRenderer,
						 ModesManager modesManager,
						 DialogsManager dialogsManager) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
		setIcon(createButtonIcon(properties));
		MenuItemDefinition menuItemDefinition = properties.getMenuItemDefinition();
		if (menuItemDefinition != null) {
			addActionFromMenuItem(persistenceManager, mapRenderer, modesManager, dialogsManager, menuItemDefinition);
		} else {
			addActionFromProperties(properties, persistenceManager, mapRenderer, modesManager, dialogsManager);
		}
	}

	private void addActionFromProperties(ToolbarButtonProperties properties,
										 PersistenceManager persistenceManager,
										 MapRenderer mapRenderer,
										 ModesManager modesManager,
										 DialogsManager dialogsManager) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		addActionListener((ActionListener) properties.getMapperCommandClass().getConstructors()[0].newInstance(
				persistenceManager,
				mapRenderer,
				modesManager,
				dialogsManager));
	}

	private void addActionFromMenuItem(PersistenceManager persistenceManager,
									   MapRenderer mapRenderer,
									   ModesManager modesManager,
									   DialogsManager dialogsManager,
									   MenuItemDefinition menuItemDefinition) {
		MenuItemProperties menuItemProperties = menuItemDefinition.getMenuItemProperties();
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
	}


}
