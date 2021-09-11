package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.CursorSelectionModel;
import com.necromine.editor.MapEditor;
import com.necromine.editor.MapEditorData;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.handlers.action.ActionHandlerRelatedData;
import com.necromine.editor.handlers.action.ActionHandlerRelatedServices;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.handlers.action.ActionsHandlerImpl;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;
import com.necromine.editor.model.elements.CharacterDecal;
import com.necromine.editor.model.elements.PlacedElements;
import lombok.Getter;

import java.awt.*;

@Getter
public class HandlersManagerImpl implements HandlersManager, Disposable {
	private final ViewAuxHandler viewAuxHandler = new ViewAuxHandler();
	private final CursorHandler cursorHandler = new CursorHandler();
	private final BatchHandler batchHandler = new BatchHandler();
	private final HandlersManagerRelatedData handlersManagerRelatedData;
	private final MapFileHandler mapFileHandler = new MapFileHandler();
	private final ResourcesHandler resourcesHandler = new ResourcesHandler();
	private final MapEditorEventsNotifier eventsNotifier;
	private final SelectionHandler selectionHandler = new SelectionHandler();
	private ActionsHandler actionsHandler;
	private RenderHandler renderHandler;


	public HandlersManagerImpl(MapEditorData data) {
		this.eventsNotifier = new MapEditorEventsNotifier();
		handlersManagerRelatedData = new HandlersManagerRelatedData(data.getMap(), data.getPlacedElements());
	}

	private void createActionsHandler(final PlacedElements placedElements, final WallCreator wallCreator) {
		ActionHandlerRelatedData data = new ActionHandlerRelatedData(handlersManagerRelatedData.getMap(), placedElements);
		ActionHandlerRelatedServices services = new ActionHandlerRelatedServices(
				cursorHandler,
				wallCreator,
				eventsNotifier,
				resourcesHandler.getAssetsManager());
		actionsHandler = new ActionsHandlerImpl(data, services, eventsNotifier);
	}

	@Override
	public void dispose( ) {
		viewAuxHandler.dispose();
		cursorHandler.dispose();
		batchHandler.dispose();
		resourcesHandler.dispose();
		renderHandler.dispose();
	}

	@Override
	public void onCreate(final OrthographicCamera camera, final WallCreator wallCreator, final Dimension levelSize) {
		batchHandler.createBatches(camera);
		viewAuxHandler.createModels(levelSize);
		GameAssetsManager assetsManager = resourcesHandler.getAssetsManager();
		createActionsHandler(handlersManagerRelatedData.getPlacedElements(), wallCreator);
		renderHandler = new RenderHandler(assetsManager, this, camera);
		resourcesHandler.initializeGameFiles();
		cursorHandler.createCursors(assetsManager, renderHandler.getTileModel());
	}

	public void onTileSelected(final Assets.SurfaceTextures texture) {
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		selectionHandler.onTileSelected(texture);
	}

	@Override
	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		actionsHandler.setSelectedElement(selectionHandler.getSelectedElement());
		cursorHandler.getCursorCharacterDecal().setCharacterDefinition(definition);
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		selectionHandler.setSelectedElement(definition);
	}

	public void onTreeEnvSelected(final ElementDefinition selectedElement) {
		selectionHandler.setSelectedElement(selectedElement);
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		actionsHandler.setSelectedElement(selectedElement);
		cursorHandler.getCursorSelectionModel().setSelection(selectedElement, ((EnvironmentDefinitions) selectedElement).getModelDefinition());
		cursorHandler.applyOpacity();
	}

	@Override
	public void onTreePickupSelected(final ItemDefinition definition) {
		ElementDefinition selectedElement = selectionHandler.getSelectedElement();
		selectionHandler.setSelectedElement(selectedElement);
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		actionsHandler.setSelectedElement(selectedElement);
		cursorHandler.getCursorSelectionModel().setSelection(selectedElement, definition.getModelDefinition());
		cursorHandler.applyOpacity();
	}

	@Override
	public void onEditModeSet(final EditModes mode) {
		onModeSet(mode);
		if (mode != EditModes.LIGHTS && mode != EditModes.PICKUPS) {
			if (mode != EditModes.TILES || selectionHandler.getSelectedTile() == null) {
				getCursorHandler().setHighlighter(null);
			} else {
				onTileSelected(selectionHandler.getSelectedTile());
			}
		} else {
			getCursorHandler().setHighlighter(getCursorHandler().getCursorTileModelInstance());
		}
	}

	@Override
	public void onSelectedObjectRotate(final int direction, final EditorMode mode) {
		if (selectionHandler.getSelectedElement() != null) {
			CursorHandler cursorHandler = getCursorHandler();
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
		selectionHandler.setSelectedElement(null);
		getCursorHandler().setHighlighter(null);
		MapEditor.mode = mode;
	}

	@Override
	public void onToolSet(final EditorTool tool) {
		selectionHandler.setSelectedElement(null);
		CursorHandler cursorHandler = getCursorHandler();
		if (tool != TilesTools.BRUSH) {
			cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		} else {
			if (selectionHandler.getSelectedElement() == null) {
				cursorHandler.setHighlighter(null);
			}
		}
		MapEditor.tool = tool;
	}

	@Override
	public boolean onTouchUp(final Model cursorTileModel) {
		return actionsHandler.onTouchUp(selectionHandler.getSelectedTile(), cursorTileModel);
	}
}
