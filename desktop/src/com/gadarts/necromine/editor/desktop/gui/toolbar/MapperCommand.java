package com.gadarts.necromine.editor.desktop.gui.toolbar;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.GuiEventsSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.ActionListener;

@RequiredArgsConstructor
@Getter
public abstract class MapperCommand implements ActionListener {
	private final PersistenceManager persistenceManager;
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final ModesHandler modesHandler;

}
