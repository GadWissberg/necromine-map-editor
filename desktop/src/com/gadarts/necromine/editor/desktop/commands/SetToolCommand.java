package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import java.awt.event.ActionEvent;

public class SetToolCommand extends MapperCommand {
	public SetToolCommand(PersistenceManager persistenceManager,
						  GuiEventsSubscriber guiEventsSubscriber,
						  ModesHandler modesHandler) {
		super(persistenceManager, guiEventsSubscriber, modesHandler);
	}
//	private final EditorTool tool;

	@Override
	public void actionPerformed(final ActionEvent e) {
//		JComponent source = (JComponent) e.getSource();
//		Events event = null;
//		if (tool instanceof TilesTools) {
//			event = Events.REQUEST_TO_SET_TILE_TOOL;
//		} else if (tool instanceof EnvTools) {
//			event = Events.REQUEST_TO_SET_ENV_TOOL;
//		}
//		Optional.ofNullable(event).ifPresent(ev -> source.firePropertyChange(ev.name(), -1, tool.ordinal()));
	}
}
