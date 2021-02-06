package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveMapCommand extends MapperCommand {
	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_SAVE.name(), -1, 0);
	}
}
