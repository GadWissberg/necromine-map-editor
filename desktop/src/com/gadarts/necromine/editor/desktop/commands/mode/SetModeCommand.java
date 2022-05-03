package com.gadarts.necromine.editor.desktop.commands.mode;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.mode.EditorMode;

import java.awt.event.ActionEvent;

public abstract class SetModeCommand extends MapperCommand {
	public SetModeCommand(PersistenceManager persistenceManager,
						  GuiEventsSubscriber guiEventsSubscriber,
						  ModesHandler modesHandler) {
		super(persistenceManager, guiEventsSubscriber, modesHandler);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		applyMode();
	}

	private void applyMode( ) {
		if (ModesHandler.getCurrentMode() == getMode()) return;
		getModesHandler().applyMode(getMode());
	}

	protected abstract EditorMode getMode( );
}
