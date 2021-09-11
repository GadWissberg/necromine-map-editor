package com.necromine.editor.actions.processes;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.utils.Utils;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

import static com.gadarts.necromine.model.MapNodesTypes.PASSABLE_NODE;

@Getter
public class PlaceTilesProcess extends MappingProcess<PlaceTilesFinishProcessParameters> {
	private final GameAssetsManager assetsManager;
	private final Set<MapNodeData> initializedTiles;

	public PlaceTilesProcess(final FlatNode srcNode,
							 final GameAssetsManager assetsManager,
							 final Set<MapNodeData> initializedTiles,
							 final GameMap map) {
		super(map, srcNode, true);
		this.assetsManager = assetsManager;
		this.initializedTiles = initializedTiles;
	}


	@Override
	public boolean isProcess() {
		return true;
	}

	@Override
	public void execute(final MapEditorEventsNotifier eventsNotifier) {

	}

	@Override
	public void finish(final PlaceTilesFinishProcessParameters params) {
		Utils.applyOnRegionOfTiles(
				srcNode,
				new FlatNode(params.getDstRow(), params.getDstCol()),
				(row, col) -> defineTile(params, col, row));
	}

	private void defineTile(final PlaceTilesFinishProcessParameters params, final int col, final int row) {
		MapNodeData[][] tiles = map.getNodes();
		MapNodeData tile = Optional.ofNullable(tiles[row][col]).orElseGet(() -> {
			MapNodeData newNode = new MapNodeData(row, col, PASSABLE_NODE);
			tiles[row][col] = newNode;
			return newNode;
		});
		if (tile.getModelInstance() == null) {
			tile.initializeModelInstance(params.getTileModel());
		}
		Utils.initializeTile(tile, params.getSelectedTile(), assetsManager);
		initializedTiles.add(tile);
	}


}
