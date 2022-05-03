package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import java.awt.event.ActionEvent;

public class RotateSelectionCommand extends MapperCommand {

	public static final int CLOCKWISE = -1;
	public static final int COUNTER_CLOCKWISE = 1;
//	private final int direction;

	public RotateSelectionCommand(PersistenceManager persistenceManager,
								  GuiEventsSubscriber guiEventsSubscriber,
								  ModesHandler modesHandler) {
		super(persistenceManager, guiEventsSubscriber, modesHandler);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
//		JComponent source = (JComponent) e.getSource();
//		source.firePropertyChange(Events.REQUEST_TO_ROTATE_SELECTED_OBJECT.name(), 0, direction);
	}
}
