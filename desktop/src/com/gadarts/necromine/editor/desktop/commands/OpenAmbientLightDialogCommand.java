package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenAmbientLightDialogCommand extends MapperCommand {


	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_OPEN_AMBIENT_LIGHT_DIALOG.name(), -1, 0);
	}

}
