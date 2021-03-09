package com.necromine.editor.actions;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.processes.LiftTilesProcess;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.model.node.Node;

import java.util.Set;

public final class ProcessBuilder {
    private static final ProcessBuilder builder = new ProcessBuilder();
    private static MappingProcess<? extends MappingProcess.FinishProcessParameters> current;

    public static ProcessBuilder begin(final GameMap map, final GameAssetsManager assetsManager, final Node src) {
        current = new LiftTilesProcess(map, assetsManager, src);
        return builder;
    }

    public ProcessBuilder liftTiles(final int direction,
                                    final WallCreator wallCreator,
                                    final Set<MapNodeData> initializedTiles) {
        LiftTilesProcess current = (LiftTilesProcess) ProcessBuilder.current;
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
