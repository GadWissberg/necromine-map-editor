package com.gadarts.necromine.editor.desktop.actions;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.CameraModes;
import com.necromine.editor.EditModes;
import com.necromine.editor.EditorMode;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SetModeCommand extends MapperCommand {
	private final EditorMode mode;

	public SetModeCommand(final EditorMode mode) {
		this.mode = mode;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		Class<? extends EditorMode> modeType = mode.getClass();
		if (modeType.equals(CameraModes.class)) {
			source.firePropertyChange(Events.REQUEST_TO_SET_CAMERA_MODE.name(), -1, mode.ordinal());
		} else if (modeType.equals(EditModes.class)) {
			source.firePropertyChange(Events.REQUEST_TO_SET_EDIT_MODE.name(), -1, mode.ordinal());
		}
	}

}
