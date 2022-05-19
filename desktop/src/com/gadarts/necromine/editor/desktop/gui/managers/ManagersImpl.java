package com.gadarts.necromine.editor.desktop.gui.managers;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.necromine.editor.MapRenderer;
import lombok.Getter;

import javax.swing.*;
import java.io.File;

@Getter
public class ManagersImpl implements Managers {
	private final PersistenceManager persistenceManager;
	private final ModesManager modesManager = new ModesManager();
	private final ToolbarsManager toolbarsManager;
	private final DialogsManager dialogsManager;
	private final EntitiesSelectionViewManager entitiesSelectionViewManager;

	public ManagersImpl(MapRenderer mapRenderer, JFrame parentWindow) {
		this.dialogsManager = new DialogsManager(mapRenderer, parentWindow);
		this.toolbarsManager = new ToolbarsManager(mapRenderer, this);
		this.entitiesSelectionViewManager = new EntitiesSelectionViewManager(mapRenderer);
		this.persistenceManager = new PersistenceManager(mapRenderer);
	}

	@Override
	public void onApplicationStart(JPanel mainPanel, JFrame windowParent, JPanel entitiesPanel, File assetsFolderLocation) {
		toolbarsManager.onApplicationStart(mainPanel, windowParent);
		entitiesSelectionViewManager.onApplicationStart(entitiesPanel, assetsFolderLocation);
	}

	@Override
	public void onMapRendererIsReady(MapRenderer mapRenderer, JFrame windowParent) {
		persistenceManager.readSettingsFile(windowParent, mapRenderer);
	}

	@Override
	public ToolbarsManager getEntitiesSelectionPanelManager() {
		return null;
	}


}
