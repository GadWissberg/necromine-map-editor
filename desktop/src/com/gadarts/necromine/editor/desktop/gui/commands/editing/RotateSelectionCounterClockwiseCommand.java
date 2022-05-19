package com.gadarts.necromine.editor.desktop.gui.commands.editing;

import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class RotateSelectionCounterClockwiseCommand extends MapperCommand {


	public RotateSelectionCounterClockwiseCommand(com.necromine.editor.MapRenderer mapRenderer,
												  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		getMapRenderer().onSelectedObjectRotate(MapRenderer.COUNTER_CLOCKWISE);
	}
}
