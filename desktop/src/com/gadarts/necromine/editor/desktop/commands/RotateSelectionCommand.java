package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.gui.FileManager;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import java.awt.event.ActionEvent;
import java.util.Map;

public class RotateSelectionCommand extends MapperCommand {

	public static final int CLOCKWISE = -1;
	public static final int COUNTER_CLOCKWISE = 1;
//	private final int direction;

	public RotateSelectionCommand(FileManager fileManager, GuiEventsSubscriber guiEventsSubscriber, Map<String, String> settings) {
		super(fileManager, guiEventsSubscriber, settings);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
//		JComponent source = (JComponent) e.getSource();
//		source.firePropertyChange(Events.REQUEST_TO_ROTATE_SELECTED_OBJECT.name(), 0, direction);
	}
}
