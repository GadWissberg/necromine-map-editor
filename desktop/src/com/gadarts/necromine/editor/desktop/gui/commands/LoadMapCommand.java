package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.Events;
import com.gadarts.necromine.editor.desktop.gui.FileManager;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class LoadMapCommand extends MapperCommand {
	public LoadMapCommand(FileManager fileManager, GuiEventsSubscriber guiEventsSubscriber, Map<String, String> settings) {
		super(fileManager, guiEventsSubscriber, settings);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JComponent source = (JComponent) e.getSource();
		source.firePropertyChange(Events.REQUEST_TO_LOAD.name(), -1, 0);
	}
}
