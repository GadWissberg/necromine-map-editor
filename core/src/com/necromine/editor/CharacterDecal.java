package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class CharacterDecal {

	private final Decal decal;

	@Setter
	private Direction spriteDirection = Direction.SOUTH;
}
