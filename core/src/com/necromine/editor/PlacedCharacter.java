package com.necromine.editor;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;
import lombok.Setter;

public class PlacedCharacter {

	private final int row;
	private final int col;

	@Getter
	private final CharacterDecal characterDecal;

	@Getter
	@Setter
	private Direction facingDirection = Direction.SOUTH;

	public PlacedCharacter(final CharacterDefinition definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager) {
		this.row = row;
		this.col = col;
		this.characterDecal = Utils.createCharacterDecal(assetsManager, definition, row, col);
	}
}
