package com.necromine.editor;


import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.model.CharacterDefinition;

public interface GuiEventsSubscriber {
	void onTileSelected(Assets.FloorsTextures texture);

	void onModeChanged(EditorModes mode);

	void onTreeCharacterSelected(CharacterDefinition definition);
}
