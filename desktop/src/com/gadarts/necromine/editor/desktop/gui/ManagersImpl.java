package com.gadarts.necromine.editor.desktop.gui;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.necromine.editor.MapRenderer;
import lombok.Getter;

import javax.swing.*;

@Getter
public class ManagersImpl implements Managers {
	private final PersistenceManager persistenceManager = new PersistenceManager();
	private final ModesManager modesManager = new ModesManager();
	private final ToolbarsManager toolbarsManager;
	private final DialogsManager dialogsManager;

	public ManagersImpl(MapRenderer mapRenderer, JFrame parentWindow) {
		this.dialogsManager = new DialogsManager(parentWindow);
		this.toolbarsManager = new ToolbarsManager(mapRenderer, this);
	}

	@Override
	public void onApplicationStart(JPanel mainPanel, JFrame windowParent) {
		toolbarsManager.addToolbars(mainPanel, windowParent);
	}

	@Override
	public void onMapRendererIsReady(MapRenderer mapRenderer, JFrame windowParent) {
		persistenceManager.readSettingsFile(windowParent, mapRenderer);
	}


}
