package com.gadarts.necromine.editor.desktop.gui;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.necromine.editor.MapRenderer;

import javax.swing.*;

public interface Managers {
	void onApplicationStart(JPanel mainPanel, JFrame windowParent);

	void onMapRendererIsReady(MapRenderer mapRenderer, JFrame windowParent);

	PersistenceManager getPersistenceManager( );

	ModesManager getModesManager( );

	ToolbarsManager getToolbarsManager( );

	DialogsManager getDialogsManager( );
}
