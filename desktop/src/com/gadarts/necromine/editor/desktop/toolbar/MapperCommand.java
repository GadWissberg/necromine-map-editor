package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.gui.FileManager;
import com.necromine.editor.GuiEventsSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.ActionListener;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class MapperCommand implements ActionListener {
	private final FileManager fileManager;
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final Map<String, String> settings;

}
