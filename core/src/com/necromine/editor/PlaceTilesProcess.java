package com.necromine.editor;

import com.gadarts.necromine.Assets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlaceTilesProcess extends MappingProcess {
	private final Tile[][] map;
	private int srcRow;
	private int srcCol;
	private int dstRow;
	private int dstCol;

	public void begin(final int srcRow, final int srcCol) {
		super.begin();
		this.srcRow = srcRow;
		this.srcCol = srcCol;
	}

	public void finish(final int dstRow,
					   final int dstCol,
					   final Assets.FloorsTextures selectedTile) {
		super.finish();
		this.dstRow = dstRow;
		this.dstCol = dstCol;
		for (int col = Math.min(dstCol, srcCol); col <= Math.max(dstCol, srcCol); col++) {
			for (int row = Math.min(dstRow, srcRow); row <= Math.max(dstRow, srcRow); row++) {
				if (map[row][col] == null) {
					map[row][col] = new Tile();
				}
				Tile tile = map[row][col];
				tile.setTextureDefinition(selectedTile);
//				TextureAttribute textureAttribute = (TextureAttribute) tile.getModelInstance().materials.get(0).get(TextureAttribute.Diffuse);
//				textureAttribute.
			}
		}
	}
}
