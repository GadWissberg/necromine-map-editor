package com.necromine.editor;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

public class PlacedCharacter {
	private final CharacterDefinition definition;
	private final int row;
	private final int col;

	@Getter
	private Direction facingDirection = Direction.SOUTH;

	@Getter
	private final CharacterDecal characterDecal;

	public PlacedCharacter(final CharacterDefinition definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager) {
		this.definition = definition;
		this.row = row;
		this.col = col;
		this.characterDecal = Utils.createCharacterDecal(assetsManager, Assets.Atlases.PLAYER_AXE_PICK, row, col);
	}
}
