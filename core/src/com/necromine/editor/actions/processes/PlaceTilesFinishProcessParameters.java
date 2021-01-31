package com.necromine.editor.actions.processes;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.assets.Assets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlaceTilesFinishProcessParameters extends MappingProcess.FinishProcessParameters {
	private final int dstRow;
	private final int dstCol;
	private final Assets.FloorsTextures selectedTile;
	private final Model tileModel;
}
