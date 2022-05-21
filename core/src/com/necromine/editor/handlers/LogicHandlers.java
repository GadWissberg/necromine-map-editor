package com.necromine.editor.handlers;

import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.env.EnvironmentDefinitions;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.CursorSelectionModel;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.handlers.action.ActionHandlerRelatedData;
import com.necromine.editor.handlers.action.ActionHandlerRelatedServices;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.handlers.action.ActionsHandlerImpl;
import com.necromine.editor.model.GameMap;
import lombok.Getter;

import java.util.Optional;

@Getter
public class LogicHandlers implements Disposable {
	private final CursorHandler cursorHandler = new CursorHandler();
	private final SelectionHandler selectionHandler = new SelectionHandler();
	private final MapEditorEventsNotifier eventsNotifier;
	private final ResourcesHandler resourcesHandler;
	private ActionsHandler actionsHandler;

	public LogicHandlers(final MapEditorEventsNotifier eventsNotifier, ResourcesHandler resourcesHandler) {
		this.eventsNotifier = eventsNotifier;
		this.resourcesHandler = resourcesHandler;
	}

	private void createActionsHandler(final HandlersManagerRelatedData handlersManagerRelatedData,
									  final WallCreator wallCreator,
									  final ResourcesHandler resourcesHandler) {
		GameMap map = handlersManagerRelatedData.getMap();
		ActionHandlerRelatedData data = new ActionHandlerRelatedData(map, handlersManagerRelatedData.getPlacedElements());
		actionsHandler = new ActionsHandlerImpl(data, new ActionHandlerRelatedServices(
				getCursorHandler(),
				wallCreator,
				eventsNotifier,
				resourcesHandler.getAssetsManager(),
				selectionHandler));
	}

	@Override
	public void dispose( ) {
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
		cursorHandler.setHighlighter(cursorHandler.getCursorHandlerModelData().getCursorTileModelInstance());
		selectionHandler.onTileSelected(texture);
	}

	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		Optional.ofNullable(cursorHandler.getCursorCharacterDecal()).ifPresentOrElse(
				c -> cursorHandler.getCursorCharacterDecal().setCharacterDefinition(definition),
				( ) -> cursorHandler.initializeCursorCharacterDecal(
						resourcesHandler.getAssetsManager(),
						definition));
		cursorHandler.setHighlighter(cursorHandler.getCursorHandlerModelData().getCursorTileModelInstance());
		selectionHandler.setSelectedElement(definition);
	}

	public void onTreeEnvSelected(final ElementDefinition selectedElement) {
		selectionHandler.setSelectedElement(selectedElement);
		cursorHandler.setHighlighter(cursorHandler.getCursorHandlerModelData().getCursorTileModelInstance());
		CursorSelectionModel cursorSelectionModel = cursorHandler.getCursorHandlerModelData().getCursorSelectionModel();
		cursorSelectionModel.setSelection(selectedElement, ((EnvironmentDefinitions) selectedElement).getModelDefinition());
		cursorHandler.applyOpacity();
	}

	public void onTreePickupSelected(final ItemDefinition selectedElement) {
		selectionHandler.setSelectedElement(selectedElement);
		CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
		cursorHandler.setHighlighter(cursorHandlerModelData.getCursorTileModelInstance());
		Assets.Models modelDefinition = selectedElement.getModelDefinition();
		cursorHandlerModelData.getCursorSelectionModel().setSelection(selectedElement, modelDefinition);
		cursorHandler.applyOpacity();
	}
}
