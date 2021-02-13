package com.necromine.editor.actions;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.necromine.editor.CharacterDecal;
import com.necromine.editor.CursorSelectionModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursorData {
	private ModelInstance cursorTileModelInstance;
	private CharacterDecal cursorCharacterDecal;
	private CursorSelectionModel cursorSelectionModel;

}
