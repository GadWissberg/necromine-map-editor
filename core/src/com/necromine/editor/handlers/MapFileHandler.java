package com.necromine.editor.handlers;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.model.elements.PlacedElements;
import com.necromine.editor.utils.MapDeflater;
import com.necromine.editor.utils.MapInflater;

import java.util.Set;

public class MapFileHandler {
	private final MapDeflater deflater = new MapDeflater();
	private MapInflater inflater;

	public void init(final GameAssetsManager assetsManager,
					 final CursorHandler cursorHandler,
					 final Set<MapNodeData> placedTiles) {
		inflater = new MapInflater(assetsManager, cursorHandler, placedTiles);
	}

	public void onSaveMapRequested(final GameMap map, final PlacedElements placedElements) {
		deflater.deflate(map, placedElements);
	}

	public void onLoadMapRequested(final GameMap map,
								   final PlacedElements placedElements,
								   final WallCreator wallCreator,
								   final ViewAuxHandler viewAuxHandler) {
		inflater.inflateMap(map, placedElements, wallCreator, viewAuxHandler);

	}
}
