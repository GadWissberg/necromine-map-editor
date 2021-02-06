package com.necromine.editor;


import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;

public interface GuiEventsSubscriber {
	void onTileSelected(Assets.FloorsTextures texture);

	void onEditModeSet(EditModes mode);

	void onTreeCharacterSelected(CharacterDefinition<?> definition);

	void onSelectedObjectRotate(int direction);

	void onTreeEnvSelected(EnvironmentDefinitions env);

	void onTreePickupSelected(ItemDefinition definition);

	void onCameraModeSet(CameraModes mode);

	void onSaveMapRequested();

	void onLoadMapRequested();
}
