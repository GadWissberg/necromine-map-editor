package com.necromine.editor;

import com.necromine.editor.model.elements.PlacedElements;
import lombok.Getter;

@Getter
public class MapEditorData {
	private final ViewportResolution viewportResolution;
	private final PlacedElements placedElements = new PlacedElements();
	private final GameMap map = new GameMap();

	public MapEditorData(ViewportResolution viewportResolution) {
		this.viewportResolution = viewportResolution;
	}

	public void reset( ) {
		placedElements.clear();
		map.reset();
	}
}
