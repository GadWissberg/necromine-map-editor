package com.necromine.editor.actions.processes;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.assets.Assets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PlaceTilesFinishProcessParameters extends SelectTilesFinishProcessParameters {
	private final Assets.FloorsTextures selectedTile;
	private final Model tileModel;


	public PlaceTilesFinishProcessParameters(final int dstRow,
											 final int dstCol,
											 final Assets.FloorsTextures selectedTile,
											 final Model tileModel) {
		super(dstRow, dstCol);
		this.selectedTile = selectedTile;
		this.tileModel = tileModel;
	}
}
