package com.necromine.editor.actions;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.Tile;
import lombok.Getter;

import java.util.Set;

@Getter
public class PlaceTilesProcess extends MappingProcess<PlaceTilesFinishProcessParameters> {
	private final int srcRow;
	private final int srcCol;
	private final GameAssetsManager assetsManager;
	private final Set<Tile> initializedTiles;

	public PlaceTilesProcess(final int srcRow,
							 final int srcCol,
							 final GameAssetsManager assetsManager,
							 final Set<Tile> initializedTiles,
							 final Tile[][] map) {
		super(map);
		this.srcRow = srcRow;
		this.srcCol = srcCol;
		this.assetsManager = assetsManager;
		this.initializedTiles = initializedTiles;
	}


	private Tile placeTile(final Assets.FloorsTextures selectedTile, final Model tileModel, final int col, final int row) {
		if (map[row][col] == null) {
			map[row][col] = new Tile(tileModel, row, col);
		}
		Tile tile = map[row][col];
		tile.setTextureDefinition(selectedTile);
		Material material = tile.getModelInstance().materials.get(0);
		TextureAttribute textureAttribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
		textureAttribute.textureDescription.texture = assetsManager.getTexture(selectedTile);
		return tile;
	}

	@Override
	public boolean isProcess() {
		return true;
	}

	@Override
	protected void execute() {

	}

	@Override
	void finish(PlaceTilesFinishProcessParameters params) {
		int dstRow = params.getDstRow();
		int dstCol = params.getDstCol();
		for (int col = Math.min(dstCol, srcCol); col <= Math.max(dstCol, srcCol); col++) {
			for (int row = Math.min(dstRow, srcRow); row <= Math.max(dstRow, srcRow); row++) {
				Tile tile = placeTile(params.getSelectedTile(), params.getTileModel(), col, row);
				initializedTiles.add(tile);
			}
		}

	}


}
