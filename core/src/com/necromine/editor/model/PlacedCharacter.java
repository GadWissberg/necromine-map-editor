package com.necromine.editor.model;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.CharacterDecal;
import com.necromine.editor.Utils;
import lombok.Getter;

@Getter
public class PlacedCharacter extends PlacedElement {

	private final CharacterDecal characterDecal;

	public PlacedCharacter(final CharacterDefinition definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager,
						   final Direction selectedCharacterDirection) {
		super(row, col, definition, selectedCharacterDirection);
		this.characterDecal = Utils.createCharacterDecal(assetsManager, definition, row, col, selectedCharacterDirection);
	}
}
