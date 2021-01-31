package com.necromine.editor.actions;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.MapNode;
import com.necromine.editor.model.PlacedElement;

import java.util.List;

public abstract class PlaceElementAction<T extends PlacedElement, S extends ElementDefinition> extends MappingAction {

	protected final int selectedRow;
	protected final int selectedCol;
	protected final GameAssetsManager assetsManager;
	protected final Direction elementDirection;
	protected final S elementDefinition;
	protected final List<T> placedElements;

	public PlaceElementAction(final MapNode[][] map,
							  final int selectedRow,
							  final int selectedCol,
							  final GameAssetsManager assetsManager,
							  final Direction elementDirection,
							  final S elementDefinition,
							  final List<T> placedElements) {
		super(map);
		this.selectedRow = selectedRow;
		this.selectedCol = selectedCol;
		this.assetsManager = assetsManager;
		this.elementDirection = elementDirection;
		this.elementDefinition = elementDefinition;
		this.placedElements = placedElements;
	}
}
