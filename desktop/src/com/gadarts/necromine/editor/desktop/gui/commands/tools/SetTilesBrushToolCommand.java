package com.gadarts.necromine.editor.desktop.gui.commands.tools;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;

public class SetTilesBrushToolCommand extends SetToolCommand {

	public SetTilesBrushToolCommand(PersistenceManager persistenceManager,
									MapRenderer mapRenderer,
									ModesManager modesManager,
									DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	protected EditorTool getTool( ) {
		return TilesTools.BRUSH;
	}

}
