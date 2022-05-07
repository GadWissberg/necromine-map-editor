package com.necromine.editor.mode.events;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.MapRendererImpl;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.ElementTools;

import java.util.Set;

public class EnvOnTouchDownEventLeft implements OnTouchDownLeftEvent {
	@Override
	public boolean run(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   final ActionsHandler actionsHandler,
					   final GameAssetsManager assetsManager,
					   final Set<MapNodeData> initializedTiles) {
		EditorTool tool = MapRendererImpl.getTool();
		if (tool == ElementTools.BRUSH && actionsHandler.getSelectedElement() != null) {
			actionsHandler.placeEnvObject(assetsManager);
		} else if (tool == ElementTools.DEFINE) {
			actionsHandler.defineSelectedEnvObject();
		}
		return true;
	}
}
