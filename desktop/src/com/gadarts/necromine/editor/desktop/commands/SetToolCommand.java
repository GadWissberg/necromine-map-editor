package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.gui.FileManager;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import java.awt.event.ActionEvent;
import java.util.Map;

public class SetToolCommand extends MapperCommand {
	public SetToolCommand(FileManager fileManager, GuiEventsSubscriber guiEventsSubscriber, Map<String, String> settings) {
		super(fileManager, guiEventsSubscriber, settings);
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
