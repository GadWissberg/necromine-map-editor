package com.necromine.editor.desktop.actions;

import com.necromine.editor.desktop.EditorModes;
import com.necromine.editor.desktop.MapperWindow;
import com.necromine.editor.desktop.toolbar.MapperActionListener;

import java.awt.event.ActionEvent;

public class SetModeAction extends MapperActionListener {
	private final EditorModes mode;

	public SetModeAction(final EditorModes mode) {
		this.mode = mode;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		MapperWindow.setMode(mode);
	}

}
