package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import lombok.Getter;

public class PlacedCharacter {
	final CharacterDefinition definition;
	final int row;
	final int col;

	@Getter
	private final Decal decal;

	public PlacedCharacter(final CharacterDefinition definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager) {
		this.definition = definition;
		this.row = row;
		this.col = col;
		this.decal = Utils.createCharacterDecal(assetsManager, Assets.Atlases.PLAYER_AXE_PICK, row, col);
	}
}
