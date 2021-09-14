package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;

import java.awt.*;

public interface HandlersManager extends Disposable {

	ResourcesHandler getResourcesHandler();

	MapFileHandler getMapFileHandler();

	void onCreate(OrthographicCamera camera, WallCreator wallCreator, Dimension dimension);

	RenderHandler getRenderHandler();

	void onTileSelected(Assets.SurfaceTextures texture);

	void onTreeCharacterSelected(CharacterDefinition definition);

	void onTreeEnvSelected(ElementDefinition selectedElement);

	void onTreePickupSelected(ItemDefinition definition);

	MapEditorEventsNotifier getEventsNotifier();


	void onEditModeSet(EditModes mode);

	void onSelectedObjectRotate(int direction, EditorMode mode);

	void onModeSet(EditorMode mode);

	void onToolSet(EditorTool tool);

	boolean onTouchUp(Model cursorTileModel);

	LogicHandlers getLogicHandlers();
}
