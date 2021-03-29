package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenMapSizeDialogCommand extends MapperCommand {


	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_OPEN_MAP_SIZE_DIALOG.name(), -1, 0);
	}

}
