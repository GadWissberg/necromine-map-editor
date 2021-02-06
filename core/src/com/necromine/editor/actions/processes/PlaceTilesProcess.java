package com.necromine.editor.actions.processes;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.MapNode;
import com.necromine.editor.Utils;
import lombok.Getter;

import java.util.Set;

@Getter
public class PlaceTilesProcess extends MappingProcess<PlaceTilesFinishProcessParameters> {
	private final int srcRow;
	private final int srcCol;
	private final GameAssetsManager assetsManager;
	private final Set<MapNode> initializedTiles;

	public PlaceTilesProcess(final int srcRow,
							 final int srcCol,
							 final GameAssetsManager assetsManager,
							 final Set<MapNode> initializedTiles,
							 final MapNode[][] map) {
		super(map);
		this.srcRow = srcRow;
		this.srcCol = srcCol;
		this.assetsManager = assetsManager;
		this.initializedTiles = initializedTiles;
	}


	private MapNode placeTile(final Assets.FloorsTextures selectedTile, final Model tileModel, final int col, final int row) {
		return Utils.createAndAddTileIfNotExists(map, row, col, tileModel, selectedTile, assetsManager);
	}

	@Override
	public boolean isProcess() {
		return true;
	}

	@Override
	public void execute() {

	}

	@Override
	public void finish(final PlaceTilesFinishProcessParameters params) {
		int dstRow = params.getDstRow();
		int dstCol = params.getDstCol();
		for (int col = Math.min(dstCol, srcCol); col <= Math.max(dstCol, srcCol); col++) {
			for (int row = Math.min(dstRow, srcRow); row <= Math.max(dstRow, srcRow); row++) {
				MapNode tile = placeTile(params.getSelectedTile(), params.getTileModel(), col, row);
				initializedTiles.add(tile);
			}
		}

	}


}
