package com.necromine.editor.model;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.CharacterDecal;
import com.necromine.editor.Node;
import com.necromine.editor.Utils;
import lombok.Getter;

@Getter
public class PlacedCharacter extends PlacedElement {

	private final CharacterDecal characterDecal;

	public PlacedCharacter(final CharacterDefinition definition,
						   final Node node,
						   final GameAssetsManager assetsManager,
						   final Direction selectedCharacterDirection) {
		super(node, definition, selectedCharacterDirection);
		this.characterDecal = Utils.createCharacterDecal(assetsManager, definition, node, selectedCharacterDirection);
	}
}
