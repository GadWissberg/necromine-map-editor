package com.gadarts.necromine.editor.desktop.actions;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.toolbar.MapperActionListener;
import com.necromine.editor.EditorModes;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SetModeAction extends MapperActionListener {
	private final EditorModes mode;

	public SetModeAction(final EditorModes mode) {
		this.mode = mode;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_CHANGE_MODE.name(), ModesHandler.getMode().ordinal(), mode.ordinal());
	}

}
