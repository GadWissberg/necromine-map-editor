package com.necromine.editor.actions;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.processes.SelectTilesForLiftProcess;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.model.node.FlatNode;

import java.util.Set;

public final class ProcessBuilder {
    private static final ProcessBuilder builder = new ProcessBuilder();
    private static MappingProcess<? extends MappingProcess.FinishProcessParameters> current;

    public static ProcessBuilder begin(final GameMap map, final FlatNode src) {
        current = new SelectTilesForLiftProcess(map, src);
        return builder;
    }

    public ProcessBuilder liftTiles(final int direction,
                                    final WallCreator wallCreator,
                                    final Set<MapNodeData> initializedTiles) {
        SelectTilesForLiftProcess current = (SelectTilesForLiftProcess) ProcessBuilder.current;
        current.setDirection(direction);
        current.setWallCreator(wallCreator);
        current.setInitializedTiles(initializedTiles);
        return builder;
    }

    public MappingProcess<? extends MappingProcess.FinishProcessParameters> finish() {
        MappingProcess<? extends MappingProcess.FinishProcessParameters> result = ProcessBuilder.current;
        current = null;
        return result;
    }
}
