package com.necromine.editor.actions.processes;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.MapNodesTypes;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.utils.Utils;
import lombok.Getter;

import java.util.Set;

@Getter
public class PlaceTilesProcess extends MappingProcess<PlaceTilesFinishProcessParameters> {
    private final GameAssetsManager assetsManager;
    private final Set<MapNodeData> initializedTiles;

    public PlaceTilesProcess(final FlatNode srcNode,
                             final GameAssetsManager assetsManager,
                             final Set<MapNodeData> initializedTiles,
                             final GameMap map) {
        super(map, srcNode);
        this.assetsManager = assetsManager;
        this.initializedTiles = initializedTiles;
    }


    @Override
    public boolean isProcess() {
        return true;
    }

    @Override
    public void execute(MapManagerEventsNotifier eventsNotifier) {

    }

    @Override
    public void finish(final PlaceTilesFinishProcessParameters params) {
        int dstRow = params.getDstRow();
        int dstCol = params.getDstCol();
        int srcCol = srcNode.getCol();
        int srcRow = srcNode.getRow();
        for (int col = Math.min(dstCol, srcCol); col <= Math.max(dstCol, srcCol); col++) {
            for (int row = Math.min(dstRow, srcRow); row <= Math.max(dstRow, srcRow); row++) {
                defineTile(params, col, row);
            }
        }
    }

    private void defineTile(final PlaceTilesFinishProcessParameters params, final int col, final int row) {
        MapNodeData[][] tiles = map.getNodes();
        MapNodeData tile = tiles[row][col];
        if (tile == null) {
            tile = new MapNodeData(params.getTileModel(), row, col, MapNodesTypes.PASSABLE_NODE);
            tiles[row][col] = tile;
        }
        Utils.initializeTile(tile, params.getSelectedTile(), assetsManager);
        initializedTiles.add(tile);
    }


}
