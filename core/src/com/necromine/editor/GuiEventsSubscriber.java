package com.necromine.editor;


import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.mode.CameraModes;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.node.NodeWallsDefinitions;

public interface GuiEventsSubscriber {
	void onTileSelected(Assets.FloorsTextures texture);

	void onEditModeSet(EditModes mode);

	void onTreeCharacterSelected(CharacterDefinition definition);

	void onSelectedObjectRotate(int direction);

	void onTreeEnvSelected(EnvironmentDefinitions env);

	void onTreePickupSelected(ItemDefinition definition);

	void onCameraModeSet(CameraModes mode);

	void onSaveMapRequested();

	void onLoadMapRequested();

	void onToolSet(EditorTool tool);

	void onNodeWallsDefined(NodeWallsDefinitions definitions, int row, int col);

	void onTilesLift(Node src, Node dst, float value);

	float onAmbientLightValueRequest();

	void onAmbientLightValueSet(float value);

	void onEnvObjectDefined(PlacedEnvObject element, float height);
}
