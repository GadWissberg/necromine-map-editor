package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class PlaceTilesProcess extends MappingProcess {
	private final Tile[][] map;
	private int srcRow;
	private int srcCol;
	private int dstRow;
	private int dstCol;
	private GameAssetsManager assetsManager;
	private Set<Tile> initializedTiles;

	public void begin(final int srcRow,
					  final int srcCol,
					  final GameAssetsManager assetsManager,
					  final Set<Tile> initializedTiles) {
		super.begin();
		this.srcRow = srcRow;
		this.srcCol = srcCol;
		this.assetsManager = assetsManager;
		this.initializedTiles = initializedTiles;
	}

	public void finish(final int dstRow,
					   final int dstCol,
					   final Assets.FloorsTextures selectedTile,
					   final Model tileModel) {
		super.finish();
		this.dstRow = dstRow;
		this.dstCol = dstCol;
		for (int col = Math.min(dstCol, srcCol); col <= Math.max(dstCol, srcCol); col++) {
			for (int row = Math.min(dstRow, srcRow); row <= Math.max(dstRow, srcRow); row++) {
				Tile tile = placeTile(selectedTile, tileModel, col, row);
				initializedTiles.add(tile);
			}
		}
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
}
