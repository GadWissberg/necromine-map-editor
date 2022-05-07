package com.necromine.editor.handlers;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.MapRendererData;
import com.necromine.editor.utils.MapDeflater;
import com.necromine.editor.utils.MapInflater;

import java.io.IOException;
import java.util.Set;

public class MapFileHandler {
	private final MapDeflater deflater = new MapDeflater();
	private MapInflater inflater;

	public void init(final GameAssetsManager assetsManager,
					 final CursorHandler cursorHandler,
					 final Set<MapNodeData> placedTiles) {
		inflater = new MapInflater(assetsManager, cursorHandler, placedTiles);
	}

	public void onSaveMapRequested(final MapRendererData data, final String path) {
		deflater.deflate(data, path);
	}

	public void onLoadMapRequested(final MapRendererData data,
								   final WallCreator wallCreator,
								   final RenderHandler renderHandler,
								   final String path) throws IOException {
		inflater.inflateMap(data, wallCreator, renderHandler, path);
	}

}
