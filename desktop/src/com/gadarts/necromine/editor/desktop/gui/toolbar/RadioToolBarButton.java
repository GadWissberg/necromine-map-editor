package com.gadarts.necromine.editor.desktop.gui.toolbar;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.necromine.editor.MapRenderer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class RadioToolBarButton extends JToggleButton {

	public RadioToolBarButton(ToolbarButtonProperties properties,
							  PersistenceManager persistenceManager,
							  MapRenderer mapRenderer,
							  ModesManager modesManager,
							  DialogsManager dialogsManager) throws IOException {
		setIcon(ToolbarButtonProperties.createButtonIcon(properties));
		ActionListener action = createAction(properties, persistenceManager, mapRenderer, modesManager, dialogsManager);
		addActionListener(action);
	}

	private ActionListener createAction(ToolbarButtonProperties buttonDefinition,
										PersistenceManager persistenceManager,
										MapRenderer mapRenderer,
										ModesManager modesManager,
										DialogsManager dialogsManager) {
		if (buttonDefinition.getMenuItemDefinition() != null) {
			MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			try {
				return (ActionListener) menuItemProperties.getActionClass().getConstructors()[0].newInstance(
						persistenceManager,
						mapRenderer,
						modesManager,
						dialogsManager);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		} else {
			return buttonDefinition.getMapperCommand();
		}
	}


}
