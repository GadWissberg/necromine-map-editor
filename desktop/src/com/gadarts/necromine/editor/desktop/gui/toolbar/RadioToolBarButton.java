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
							  DialogsManager dialogsManager) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
		setIcon(ToolbarButtonProperties.createButtonIcon(properties));
		ActionListener action = createAction(properties, persistenceManager, mapRenderer, modesManager, dialogsManager);
		addActionListener(action);
	}

	private ActionListener createAction(ToolbarButtonProperties buttonDef,
										PersistenceManager persistenceManager,
										MapRenderer mapRenderer,
										ModesManager modesManager,
										DialogsManager dialogsManager) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		if (buttonDef.getMenuItemDefinition() != null) {
			return createActionFromMenuItem(buttonDef, persistenceManager, mapRenderer, modesManager, dialogsManager);
		} else {
			return (ActionListener) buttonDef.getMapperCommandClass().getConstructors()[0].newInstance(
					persistenceManager,
					mapRenderer,
					modesManager,
					dialogsManager);
		}
	}

	private ActionListener createActionFromMenuItem(ToolbarButtonProperties buttonDefinition,
													PersistenceManager persistenceManager,
													MapRenderer mapRenderer,
													ModesManager modesManager,
													DialogsManager dialogsManager) {
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
	}


}
