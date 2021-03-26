package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedCharacter;
import com.necromine.editor.model.elements.PlacedElement.PlacedElementParameters;
import com.necromine.editor.model.node.Node;

import java.util.List;

import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_Y;

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
	public void execute(final MapManagerEventsNotifier eventsNotifier) {
		super.execute(eventsNotifier);
	}

	@Override
	protected void addElementToList(final PlacedCharacter element) {
		placedElements.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(final PlacedCharacter element, final MapNodeData tile) {
		Decal decal = element.getCharacterDecal().getDecal();
		Vector3 position = decal.getPosition();
		decal.setPosition(position.x, BILLBOARD_Y + tile.getHeight(), position.z);
	}


	@Override
	protected PlacedCharacter createElement(final MapNodeData tile) {
		PlacedCharacter result = null;
		if (tile != null) {
			PlacedElementParameters parameters = new PlacedElementParameters(elementDefinition, elementDirection, node, 0);
			result = new PlacedCharacter(parameters, assetsManager);
		}
		return result;
	}


	@Override
	public boolean isProcess() {
		return false;
	}
}
