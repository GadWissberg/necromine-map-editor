package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
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
import com.necromine.editor.model.elements.PlacedElements;
import lombok.Getter;

import java.awt.*;

@Getter
public class Handlers implements Disposable {
	private final ViewAuxHandler viewAuxHandler = new ViewAuxHandler();
	private final CursorHandler cursorHandler = new CursorHandler();
	private final BatchHandler batchHandler = new BatchHandler();
	private final GameAssetsManager assetsManager;
	private final GameMap map;
	private final MapManagerEventsNotifier eventsNotifier;
	private final PlacedElements placedElements;
	private ActionsHandler actionsHandler;

	public Handlers(final GameAssetsManager assetsManager,
					final GameMap map,
					final MapManagerEventsNotifier eventsNotifier,
					final PlacedElements placedElements) {
		this.assetsManager = assetsManager;
		this.map = map;
		this.eventsNotifier = eventsNotifier;
		this.placedElements = placedElements;
	}

	public void onCreate(final Model tileModel,
						 final Camera camera,
						 final WallCreator wallCreator,
						 final Dimension levelSize) {
		batchHandler.createBatches(camera);
		viewAuxHandler.createModels(levelSize);
		cursorHandler.createCursors(assetsManager, tileModel);
		viewAuxHandler.createModels();
		cursorHandler.createCursors(assetsManager, tileModel, map);
		createActionsHandler(placedElements, wallCreator);
	}

	private void createActionsHandler(final PlacedElements placedElements, final WallCreator wallCreator) {
		ActionHandlerRelatedData data = new ActionHandlerRelatedData(map, placedElements);
		ActionHandlerRelatedServices services = new ActionHandlerRelatedServices(
				cursorHandler,
				wallCreator,
				eventsNotifier,
				assetsManager);
		actionsHandler = new ActionsHandler(data, services, eventsNotifier);
	}

	@Override
	public void dispose() {
		viewAuxHandler.dispose();
		cursorHandler.dispose();
		batchHandler.dispose();
	}

	public void onTileSelected() {
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
	}

	public void onTreePickupSelected(final ElementDefinition selectedElement, final ItemDefinition definition) {
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		actionsHandler.setSelectedElement(selectedElement);
		cursorHandler.getCursorSelectionModel().setSelection(selectedElement, definition.getModelDefinition());
	}
}
