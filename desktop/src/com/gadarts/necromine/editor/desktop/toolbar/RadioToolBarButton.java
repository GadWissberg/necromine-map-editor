package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class RadioToolBarButton extends JToggleButton {

	public RadioToolBarButton(ImageIcon imageIcon,
							  ToolbarButtonProperties buttonDefinition,
							  PersistenceManager persistenceManager,
							  GuiEventsSubscriber guiEventsSubscriber,
							  ModesHandler modesHandler) {
		super(imageIcon);
		ActionListener action = createAction(buttonDefinition, persistenceManager, guiEventsSubscriber, modesHandler);
		addActionListener(action);
	}

	private ActionListener createAction(ToolbarButtonProperties buttonDefinition,
										PersistenceManager persistenceManager,
										GuiEventsSubscriber guiEventsSubscriber,
										ModesHandler modesHandler) {
		if (buttonDefinition.getMenuItemDefinition() != null) {
			MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
			setEnabled(!menuItemProperties.isDisabledOnStart());
			try {
				return (ActionListener) menuItemProperties.getActionClass().getConstructors()[0].newInstance(
						persistenceManager,
						guiEventsSubscriber,
						modesHandler);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		} else {
			return buttonDefinition.getMapperCommand();
		}
	}


}
