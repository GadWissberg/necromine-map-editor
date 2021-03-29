package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.EnvTools;
import com.necromine.editor.mode.tools.TilesTools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class SetToolCommand extends MapperCommand {
	private final EditorTool tool;

	public SetToolCommand(final EditorTool tool) {
		super();
		this.tool = tool;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		Events event = null;
		if (tool instanceof TilesTools) {
			event = Events.REQUEST_TO_SET_TILE_TOOL;
		} else if (tool instanceof EnvTools) {
			event = Events.REQUEST_TO_SET_ENV_TOOL;
		}
		Optional.ofNullable(event).ifPresent(ev -> source.firePropertyChange(ev.name(), -1, tool.ordinal()));
	}
}
