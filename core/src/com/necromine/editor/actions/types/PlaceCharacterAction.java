package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapNode;
import com.necromine.editor.Node;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.PlacedCharacter;

import java.util.List;

public class PlaceCharacterAction extends PlaceElementAction<PlacedCharacter, CharacterDefinition> {

	public PlaceCharacterAction(final GameMap map,
								final List<PlacedCharacter> placedElements,
								final Node node,
								final CharacterDefinition selectedCharacter,
								final GameAssetsManager assetsManager,
								final Direction selectedCharacterDirection) {
		super(map, node, assetsManager, selectedCharacterDirection, selectedCharacter, placedElements);
	}

	@Override
	protected void execute() {
		MapNode tile = map.getTiles()[node.getRow()][node.getCol()];
		if (tile != null) {
			placedElements.add(new PlacedCharacter(
					elementDefinition,
					node,
					assetsManager,
					elementDirection));
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
