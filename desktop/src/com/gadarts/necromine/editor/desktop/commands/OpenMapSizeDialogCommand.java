package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenMapSizeDialogCommand extends MapperCommand {


	public OpenMapSizeDialogCommand(PersistenceManager persistenceManager,
									GuiEventsSubscriber guiEventsSubscriber,
									ModesHandler modesHandler) {
		super(persistenceManager, guiEventsSubscriber, modesHandler);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_OPEN_MAP_SIZE_DIALOG.name(), -1, 0);
	}

}
