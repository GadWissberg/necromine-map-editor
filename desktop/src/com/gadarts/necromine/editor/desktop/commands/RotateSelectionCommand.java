package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.gui.Managers;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;

import java.awt.event.ActionEvent;

public class RotateSelectionCommand extends MapperCommand {

	public static final int CLOCKWISE = -1;
	public static final int COUNTER_CLOCKWISE = 1;
//	private final int direction;

	public RotateSelectionCommand(com.necromine.editor.MapRenderer mapRenderer,
								  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	}
}
