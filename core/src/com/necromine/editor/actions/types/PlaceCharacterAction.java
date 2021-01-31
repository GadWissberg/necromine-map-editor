package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.MapNode;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.PlacedCharacter;

import java.util.List;

public class PlaceCharacterAction extends PlaceElementAction<PlacedCharacter, CharacterDefinition> {

	public PlaceCharacterAction(final MapNode[][] map,
								final List<PlacedCharacter> placedElements,
								final int selectedRow,
								final int selectedCol,
								final CharacterDefinition selectedCharacter,
								final GameAssetsManager assetsManager,
								final Direction selectedCharacterDirection) {
		super(map, selectedRow, selectedCol, assetsManager, selectedCharacterDirection, selectedCharacter, placedElements);
	}

	@Override
	protected void execute() {
		MapNode tile = map[selectedRow][selectedCol];
		if (tile != null) {
			placedElements.add(new PlacedCharacter(
					elementDefinition,
					selectedRow,
					selectedCol,
					assetsManager,
					elementDirection));
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
