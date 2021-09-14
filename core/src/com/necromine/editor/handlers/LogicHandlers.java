package com.necromine.editor.handlers;

import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.handlers.action.ActionHandlerRelatedData;
import com.necromine.editor.handlers.action.ActionHandlerRelatedServices;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.handlers.action.ActionsHandlerImpl;
import lombok.Getter;

@Getter
public class LogicHandlers implements Disposable {
	private final CursorHandler cursorHandler = new CursorHandler();
	private final SelectionHandler selectionHandler = new SelectionHandler();
	private final MapEditorEventsNotifier eventsNotifier;
	private ActionsHandler actionsHandler;

	public LogicHandlers(MapEditorEventsNotifier eventsNotifier) {
		this.eventsNotifier = eventsNotifier;
	}

	private void createActionsHandler(final HandlersManagerRelatedData handlersManagerRelatedData,
									  final WallCreator wallCreator,
									  final ResourcesHandler resourcesHandler) {
		GameMap map = handlersManagerRelatedData.getMap();
		ActionHandlerRelatedData data = new ActionHandlerRelatedData(map, handlersManagerRelatedData.getPlacedElements());
		ActionHandlerRelatedServices services = new ActionHandlerRelatedServices(
				getCursorHandler(),
				wallCreator,
				eventsNotifier,
				resourcesHandler.getAssetsManager());
		actionsHandler = new ActionsHandlerImpl(data, services, eventsNotifier);
	}

	@Override
	public void dispose() {
		cursorHandler.dispose();
	}

	public void onCreate(final HandlersManagerRelatedData handlersManagerRelatedData,
						 final WallCreator wallCreator,
						 final GameAssetsManager assetsManager,
						 final RenderHandler renderHandler,
						 final ResourcesHandler resourcesHandler) {
		createActionsHandler(handlersManagerRelatedData, wallCreator, resourcesHandler);
		cursorHandler.createCursors(assetsManager, renderHandler.getTileModel());
	}

	public void onTileSelected(final Assets.SurfaceTextures texture) {
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		selectionHandler.onTileSelected(texture);
	}

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

	public void onTreePickupSelected(final ItemDefinition definition) {
		ElementDefinition selectedElement = selectionHandler.getSelectedElement();
		selectionHandler.setSelectedElement(selectedElement);
		cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		actionsHandler.setSelectedElement(selectedElement);
		cursorHandler.getCursorSelectionModel().setSelection(selectedElement, definition.getModelDefinition());
		cursorHandler.applyOpacity();
	}
}
