package com.necromine.editor.handlers;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.model.ElementDefinition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectionHandler {
	private Assets.SurfaceTextures selectedTile;
	private ElementDefinition selectedElement;

	public void onTileSelected(final Assets.SurfaceTextures texture) {
		selectedTile = texture;
	}
}
