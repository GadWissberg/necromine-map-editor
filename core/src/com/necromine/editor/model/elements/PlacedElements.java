package com.necromine.editor.model.elements;

import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.mode.EditModes;
import lombok.Getter;

import java.util.*;

@Getter
public class PlacedElements {
	private final Set<MapNodeData> placedTiles = new HashSet<>();
	private final Map<EditModes, List<? extends PlacedElement>> placedObjects = new HashMap<>();

	public void clear( ) {
		placedTiles.clear();
		placedObjects.forEach((key, value) -> value.clear());
	}
}
