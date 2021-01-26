package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CharacterDecal {

	private final Decal decal;

	@Setter
	private CharacterDefinition characterDefinition;

	@Setter
	private Direction spriteDirection;

}
