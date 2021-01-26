package com.necromine.editor.actions;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.PlacedCharacter;
import com.necromine.editor.Tile;

import java.util.List;

public class PlaceCharacterAction extends MappingAction {

	private final List<PlacedCharacter> placedCharacters;
	private final int selectedRow;
	private final int selectedCol;
	private final CharacterDefinition selectedCharacter;
	private final GameAssetsManager assetsManager;
	private final Direction selectedCharacterDirection;

	public PlaceCharacterAction(final Tile[][] map,
								final List<PlacedCharacter> placedCharacters,
								final int selectedRow,
								final int selectedCol,
								final CharacterDefinition selectedCharacter,
								final GameAssetsManager assetsManager,
								final Direction selectedCharacterDirection) {
		super(map);
		this.placedCharacters = placedCharacters;
		this.selectedRow = selectedRow;
		this.selectedCol = selectedCol;
		this.selectedCharacter = selectedCharacter;
		this.assetsManager = assetsManager;
		this.selectedCharacterDirection = selectedCharacterDirection;
	}

	@Override
	protected void execute() {
		Tile tile = map[selectedRow][selectedCol];
		if (tile != null) {
			PlacedCharacter character = new PlacedCharacter(selectedCharacter, selectedRow, selectedCol, assetsManager, selectedCharacterDirection);
			placedCharacters.add(character);
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
