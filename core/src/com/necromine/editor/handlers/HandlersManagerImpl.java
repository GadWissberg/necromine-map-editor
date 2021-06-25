package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.handlers.action.ActionHandlerRelatedData;
import com.necromine.editor.handlers.action.ActionHandlerRelatedServices;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.handlers.action.ActionsHandlerImpl;
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
	private final MapManagerEventsNotifier eventsNotifier;
	private ActionsHandler actionsHandler;
	private RenderHandler renderHandler;

	public HandlersManagerImpl(final GameMap map,
							   final MapManagerEventsNotifier eventsNotifier,
							   final PlacedElements placedElements) {
		this.eventsNotifier = eventsNotifier;
		handlersManagerRelatedData = new HandlersManagerRelatedData(map, placedElements);
	}

	public void onCreate(final Camera camera,
						 final WallCreator wallCreator,
						 final Dimension levelSize) {
		batchHandler.createBatches(camera);
		viewAuxHandler.createModels(levelSize);
		GameAssetsManager assetsManager = resourcesHandler.getAssetsManager();
		createActionsHandler(handlersManagerRelatedData.getPlacedElements(), wallCreator);
		renderHandler = new RenderHandler(assetsManager, this, camera);
		resourcesHandler.initializeGameFiles();
		cursorHandler.createCursors(assetsManager, renderHandler.getTileModel());
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

	public void onTileSelected( ) {
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
	}

	public void onTreeCharacterSelected(final ElementDefinition selectedElement, final CharacterDefinition definition) {
		actionsHandler.setSelectedElement(selectedElement);
		cursorHandler.getCursorCharacterDecal().setCharacterDefinition(definition);
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
	}

	public void onTreeEnvSelected(final ElementDefinition selectedElement) {
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		actionsHandler.setSelectedElement(selectedElement);
		cursorHandler.getCursorSelectionModel().setSelection(selectedElement, ((EnvironmentDefinitions) selectedElement).getModelDefinition());
		cursorHandler.applyOpacity();
	}

	public void onTreePickupSelected(final ElementDefinition selectedElement, final ItemDefinition definition) {
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		actionsHandler.setSelectedElement(selectedElement);
		cursorHandler.getCursorSelectionModel().setSelection(selectedElement, definition.getModelDefinition());
		cursorHandler.applyOpacity();
	}
}
