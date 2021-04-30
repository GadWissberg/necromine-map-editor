package com.necromine.editor.mode;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.MapEditor;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.EnvTools;

import java.util.Set;

public class EnvOnTouchDownEventLeft implements OnTouchDownLeftEvent {
	@Override
	public boolean run(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   final ActionsHandler actionsHandler,
					   final GameAssetsManager assetsManager,
					   final Set<MapNodeData> initializedTiles) {
		EditorTool tool = MapEditor.getTool();
		if (tool == EnvTools.BRUSH && actionsHandler.getSelectedElement() != null) {
			actionsHandler.placeEnvObject(assetsManager);
		} else if (tool == EnvTools.DEFINE) {
			actionsHandler.defineSelectedEnvObject();
		}
		return true;
	}
}
