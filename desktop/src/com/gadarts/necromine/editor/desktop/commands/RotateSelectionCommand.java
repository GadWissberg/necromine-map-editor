package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;

import java.awt.event.ActionEvent;

public class RotateSelectionCommand extends MapperCommand {

	public static final int CLOCKWISE = -1;
	public static final int COUNTER_CLOCKWISE = 1;
//	private final int direction;

	public RotateSelectionCommand(PersistenceManager persistenceManager,
								  com.necromine.editor.MapRenderer mapRenderer,
								  ModesManager modesManager,
								  DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	}
}
