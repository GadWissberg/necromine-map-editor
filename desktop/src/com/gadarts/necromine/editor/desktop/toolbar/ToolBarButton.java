package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.gui.FileManager;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

public class ToolBarButton extends JButton {

	public ToolBarButton(ImageIcon imageIcon,
						 ToolbarButtonProperties buttonDefinition,
						 FileManager fileManager,
						 GuiEventsSubscriber guiEventsSubscriber,
						 Map<String, String> settings) {
		super(imageIcon);
		MenuItemDefinition menuItemDefinition = buttonDefinition.getMenuItemDefinition();
		Optional.ofNullable(menuItemDefinition).ifPresentOrElse(menuItem -> {
			MenuItemProperties menuItemProperties = menuItem.getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			MapperCommand action;
			try {
				Constructor<?> constructor = menuItemProperties.getActionClass().getConstructors()[0];
				action = (MapperCommand) constructor.newInstance(fileManager, guiEventsSubscriber, settings);
			} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			addActionListener(action);
		}, () -> addActionListener(buttonDefinition.getMapperCommand()));
	}


}
