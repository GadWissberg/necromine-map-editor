package com.gadarts.necromine.editor.desktop.commands;

import com.gadarts.necromine.editor.desktop.gui.FileManager;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import java.awt.event.ActionEvent;
import java.util.Map;

public class SetModeCommand extends MapperCommand {
//	private final EditorMode mode;

	public SetModeCommand(FileManager fileManager, GuiEventsSubscriber guiEventsSubscriber, Map<String, String> settings) {
		super(fileManager, guiEventsSubscriber, settings);
	}


	@Override
	public void actionPerformed(final ActionEvent e) {
//		JComponent source = (JComponent) e.getSource();
//		Class<? extends EditorMode> modeType = mode.getClass();
//		if (modeType.equals(CameraModes.class)) {
//			source.firePropertyChange(Events.REQUEST_TO_SET_CAMERA_MODE.name(), -1, mode.ordinal());
//		} else if (modeType.equals(EditModes.class)) {
//			source.firePropertyChange(Events.REQUEST_TO_SET_EDIT_MODE.name(), -1, mode.ordinal());
//		}
	}

}
