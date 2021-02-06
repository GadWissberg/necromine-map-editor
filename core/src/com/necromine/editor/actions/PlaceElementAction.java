package com.necromine.editor.actions;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.Node;
import com.necromine.editor.model.PlacedElement;

import java.util.List;

public abstract class PlaceElementAction<T extends PlacedElement, S extends ElementDefinition> extends MappingAction {

	protected final GameAssetsManager assetsManager;
	protected final Direction elementDirection;
	protected final S elementDefinition;
	protected final List<T> placedElements;
	protected final Node node;

	public PlaceElementAction(final GameMap map,
							  final Node node,
							  final GameAssetsManager assetsManager,
							  final Direction elementDirection,
							  final S elementDefinition,
							  final List<T> placedElements) {
		super(map);
		this.node = node;
		this.assetsManager = assetsManager;
		this.elementDirection = elementDirection;
		this.elementDefinition = elementDefinition;
		this.placedElements = placedElements;
	}
}
