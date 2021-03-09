package com.necromine.editor.actions.processes;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.model.node.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class LiftTilesProcess extends MappingProcess<LiftTilesFinishProcessParameters> {
    private int direction;
    private WallCreator wallCreator;
    private Set<MapNodeData> initializedTiles;

    public LiftTilesProcess(final GameMap map, final GameAssetsManager assetsManager, final Node src) {
        super(map, src);
    }

    @Override
    public boolean isProcess() {
        return false;
    }

    @Override
    protected void execute() {

    }

    @Override
    public void finish(final LiftTilesFinishProcessParameters params) {
        params.getNotifier().tilesSelectedForLifting(srcNode, params.getDstRow(), params.getDstCol());
    }
}
