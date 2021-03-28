package com.necromine.editor.model.elements;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.utils.Utils;
import lombok.Getter;

@Getter
public class PlacedCharacter extends PlacedElement {

	private final CharacterDecal characterDecal;

	public PlacedCharacter(final PlacedElementParameters parameters, final GameAssetsManager assetsManager) {
		super(parameters);
		this.characterDecal = Utils.createCharacterDecal(
				assetsManager,
				(CharacterDefinition) parameters.getDefinition(),
				new FlatNode(parameters.getNode()),
				parameters.getFacingDirection());
	}
}
