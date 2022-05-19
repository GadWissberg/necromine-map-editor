package com.gadarts.necromine.editor.desktop.gui.managers;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.necromine.editor.MapRenderer;

import javax.swing.*;
import java.io.File;

public interface Managers {
	void onApplicationStart(JPanel mainPanel, JFrame windowParent, JPanel entitiesPanel, File assetsFolderLocation);

	void onMapRendererIsReady(MapRenderer mapRenderer, JFrame windowParent);

	PersistenceManager getPersistenceManager();

	ModesManager getModesManager();

	ToolbarsManager getToolbarsManager();

	ToolbarsManager getEntitiesSelectionPanelManager();

	DialogsManager getDialogsManager();
}
