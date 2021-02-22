package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.mode.TilesTools;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SetTilesToolCommand extends MapperCommand {
	private final TilesTools tool;

	public SetTilesToolCommand(final TilesTools tool) {
		super();
		this.tool = tool;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_SET_TILE_TOOL.name(), -1, tool.ordinal());
	}
}
