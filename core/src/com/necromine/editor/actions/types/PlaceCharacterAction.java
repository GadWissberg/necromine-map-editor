package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.model.node.MapNode;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedCharacter;

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
		super.execute();
		MapNode tile = map.getNodes()[node.getRow()][node.getCol()];
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
