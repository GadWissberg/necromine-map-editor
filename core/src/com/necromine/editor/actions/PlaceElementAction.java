package com.necromine.editor.actions;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.MapNode;

public abstract class PlaceElementAction extends MappingAction {
	protected final int selectedRow;
	protected final int selectedCol;
	protected final GameAssetsManager assetsManager;
	protected final Direction selectedCharacterDirection;

	public PlaceElementAction(MapNode[][] map, final int selectedRow, final int selectedCol, final GameAssetsManager assetsManager, final Direction selectedCharacterDirection) {
		super(map);
		this.selectedRow = selectedRow;
		this.selectedCol = selectedCol;
		this.assetsManager = assetsManager;
		this.selectedCharacterDirection = selectedCharacterDirection;
	}
}
