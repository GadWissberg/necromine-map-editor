package com.gadarts.necromine.editor.desktop.actions;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;

@RequiredArgsConstructor
public class RotateCharacterCommand extends MapperCommand {

	public static final int CLOCKWISE = 1;
	public static final int COUNTER_CLOCKWISE = -1;
	private final int direction;

	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_ROTATE_SELECTED_CHARACTER.name(), 0, direction);
	}
}
