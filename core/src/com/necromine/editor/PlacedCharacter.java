package com.necromine.editor;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

@Getter
public class PlacedCharacter {

	private final int row;
	private final int col;
	private final CharacterDecal characterDecal;
	private final Direction facingDirection;

	public PlacedCharacter(final CharacterDefinition definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager,
						   final Direction selectedCharacterDirection) {
		this.row = row;
		this.col = col;
		this.characterDecal = Utils.createCharacterDecal(assetsManager, definition, row, col, selectedCharacterDirection);
		this.facingDirection = selectedCharacterDirection;
	}
}
