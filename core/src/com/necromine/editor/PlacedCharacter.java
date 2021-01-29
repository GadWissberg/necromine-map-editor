package com.necromine.editor;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

@Getter
public class PlacedCharacter extends PlacedElement {

	private final CharacterDecal characterDecal;

	public PlacedCharacter(final CharacterDefinition definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager,
						   final Direction selectedCharacterDirection) {
		super(row, col, selectedCharacterDirection);
		this.characterDecal = Utils.createCharacterDecal(assetsManager, definition, row, col, selectedCharacterDirection);
	}
}
