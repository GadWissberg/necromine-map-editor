package com.necromine.editor;

import com.gadarts.necromine.model.CharacterDefinition;
import com.gadarts.necromine.model.CharacterTypes;

public class PlayerDefinition implements CharacterDefinition {
	@Override
	public String getDisplayName() {
		return "Player";
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

	@Override
	public CharacterTypes getCharacterType() {
		return CharacterTypes.PLAYER;
	}
}
