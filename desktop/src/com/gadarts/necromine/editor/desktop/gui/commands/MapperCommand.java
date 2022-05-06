package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.MapRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.ActionListener;

@RequiredArgsConstructor
@Getter
public abstract class MapperCommand implements ActionListener {
	private final PersistenceManager persistenceManager;
	private final MapRenderer mapRenderer;
	private final ModesManager modesManager;
	private final DialogsManager dialogsManager;

}
