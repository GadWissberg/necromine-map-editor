package com.gadarts.necromine.editor.desktop.gui.toolbar;

import com.gadarts.necromine.editor.desktop.gui.Managers;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.necromine.editor.MapRenderer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class RadioToolBarButton extends JToggleButton {

	public RadioToolBarButton(ToolbarButtonProperties properties,
							  MapRenderer mapRenderer,
							  Managers managers) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
		setIcon(ToolbarButtonProperties.createButtonIcon(properties));
		ActionListener action = createAction(properties, mapRenderer, managers);
		addActionListener(action);
	}

	private ActionListener createAction(ToolbarButtonProperties buttonDef,
										MapRenderer mapRenderer,
										Managers managers) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		if (buttonDef.getMenuItemDefinition() != null) {
			return createActionFromMenuItem(buttonDef, mapRenderer, managers);
		} else {
			return (ActionListener) buttonDef.getMapperCommandClass().getConstructors()[0].newInstance(
					mapRenderer,
					managers);
		}
	}

	private ActionListener createActionFromMenuItem(ToolbarButtonProperties buttonDefinition,
													MapRenderer mapRenderer,
													Managers managers) {
		MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
		setEnabled(!menuItemProperties.isDisabledOnStart());
		try {
			return (ActionListener) menuItemProperties.getActionClass().getConstructors()[0].newInstance(
					mapRenderer,
					managers);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


}
