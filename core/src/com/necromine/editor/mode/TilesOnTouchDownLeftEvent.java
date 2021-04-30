package com.necromine.editor.mode;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.MapEditor;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.tools.TilesTools;

import java.util.Set;

public class TilesOnTouchDownLeftEvent implements OnTouchDownLeftEvent {

	@Override
	public boolean run(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   final ActionsHandler actionsHandler,
					   final GameAssetsManager assetsManager,
					   final Set<MapNodeData> initializedTiles) {
		if (currentProcess != null) return false;
		if (MapEditor.getTool() == TilesTools.BRUSH) {
			actionsHandler.beginTilePlacingProcess(assetsManager, initializedTiles);
		} else if (MapEditor.getTool() == TilesTools.LIFT) {
			actionsHandler.beginSelectingTileForLiftProcess(1, initializedTiles);
		} else if (MapEditor.getTool() == TilesTools.WALL_TILING) {
			actionsHandler.beginSelectingTilesForWallTiling();
		}
		return true;
	}
}
