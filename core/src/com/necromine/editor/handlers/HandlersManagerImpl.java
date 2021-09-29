package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.CursorSelectionModel;
import com.necromine.editor.MapEditor;
import com.necromine.editor.MapEditorData;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;
import com.necromine.editor.model.elements.CharacterDecal;
import lombok.Getter;

import java.awt.*;

@Getter
public class HandlersManagerImpl implements HandlersManager, Disposable {
	private final HandlersManagerRelatedData handlersManagerRelatedData;
	private final MapFileHandler mapFileHandler = new MapFileHandler();
	private final ResourcesHandler resourcesHandler = new ResourcesHandler();
	private final MapEditorEventsNotifier eventsNotifier;
	private final LogicHandlers logicHandlers;
	private RenderHandler renderHandler;


	public HandlersManagerImpl(MapEditorData data) {
		this.eventsNotifier = new MapEditorEventsNotifier();
		handlersManagerRelatedData = new HandlersManagerRelatedData(data.getMap(), data.getPlacedElements());
		this.logicHandlers = new LogicHandlers(eventsNotifier);
	}


	@Override
	public void dispose() {
		logicHandlers.dispose();
		resourcesHandler.dispose();
		renderHandler.dispose();
	}

	@Override
	public void onCreate(final OrthographicCamera camera, final WallCreator wallCreator, final Dimension levelSize) {
		GameAssetsManager assetsManager = resourcesHandler.getAssetsManager();
		resourcesHandler.initializeGameFiles();
		renderHandler = new RenderHandler(assetsManager, this, camera);
		logicHandlers.onCreate(handlersManagerRelatedData, wallCreator, assetsManager, renderHandler, resourcesHandler);
		renderHandler.createBatches(camera);
		renderHandler.createModels(levelSize);
	}

	public void onTileSelected(final Assets.SurfaceTextures texture) {
		logicHandlers.onTileSelected(texture);
	}

	@Override
	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		logicHandlers.onTreeCharacterSelected(definition);
	}

	public void onTreeEnvSelected(final ElementDefinition selectedElement) {
		logicHandlers.onTreeEnvSelected(selectedElement);
	}

	@Override
	public void onTreePickupSelected(final ItemDefinition definition) {
		logicHandlers.onTreePickupSelected(definition);
	}

	@Override
	public void onEditModeSet(final EditModes mode) {
		onModeSet(mode);
		if (mode != EditModes.LIGHTS && mode != EditModes.PICKUPS) {
			if (mode != EditModes.TILES || logicHandlers.getSelectionHandler().getSelectedTile() == null) {
				logicHandlers.getCursorHandler().setHighlighter(null);
			} else {
				onTileSelected(logicHandlers.getSelectionHandler().getSelectedTile());
			}
		} else {
			logicHandlers.getCursorHandler().setHighlighter(logicHandlers.getCursorHandler().getCursorTileModelInstance());
		}
	}

	@Override
	public void onSelectedObjectRotate(final int direction, final EditorMode mode) {
		if (logicHandlers.getSelectionHandler().getSelectedElement() != null) {
			CursorHandler cursorHandler = logicHandlers.getCursorHandler();
			if (mode == EditModes.CHARACTERS) {
				CharacterDecal cursorCharacterDecal = cursorHandler.getCursorCharacterDecal();
				int ordinal = cursorCharacterDecal.getSpriteDirection().ordinal() + direction;
				int length = Direction.values().length;
				int index = (ordinal < 0 ? ordinal + length : ordinal) % length;
				cursorCharacterDecal.setSpriteDirection(Direction.values()[index]);
			} else {
				CursorSelectionModel cursorSelectionModel = cursorHandler.getCursorSelectionModel();
				int ordinal = cursorSelectionModel.getFacingDirection().ordinal() + direction * 2;
				int length = Direction.values().length;
				int index = (ordinal < 0 ? ordinal + length : ordinal) % length;
				cursorSelectionModel.setFacingDirection(Direction.values()[index]);
			}
		}
	}

	@Override
	public void onModeSet(final EditorMode mode) {
		logicHandlers.getSelectionHandler().setSelectedElement(null);
		logicHandlers.getCursorHandler().setHighlighter(null);
		MapEditor.mode = mode;
	}

	@Override
	public void onToolSet(final EditorTool tool) {
		logicHandlers.getSelectionHandler().setSelectedElement(null);
		CursorHandler cursorHandler = logicHandlers.getCursorHandler();
		if (tool != TilesTools.BRUSH) {
			cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		} else {
			if (logicHandlers.getSelectionHandler().getSelectedElement() == null) {
				cursorHandler.setHighlighter(null);
			}
		}
		MapEditor.tool = tool;
	}

	@Override
	public boolean onTouchUp(final Model cursorTileModel) {
		SelectionHandler selectionHandler = logicHandlers.getSelectionHandler();
		return logicHandlers.getActionsHandler().onTouchUp(selectionHandler.getSelectedTile(), cursorTileModel);
	}
}