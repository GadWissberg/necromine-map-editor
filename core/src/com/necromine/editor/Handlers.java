package com.necromine.editor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.actions.ActionsHandler;
import com.necromine.editor.actions.CursorHandler;
import lombok.Getter;

@Getter
public class Handlers implements Disposable {
	private final ViewAuxHandler viewAuxHandler = new ViewAuxHandler();
	private final CursorHandler cursorHandler = new CursorHandler();
	private final BatchHandler batchHandler = new BatchHandler();
	private final GameAssetsManager assetsManager;
	private ActionsHandler actionsHandler;

	public Handlers(final GameAssetsManager assetsManager) {
		this.assetsManager = assetsManager;
	}

	public void onCreate(final Model tileModel,
						 final GameMap map,
						 final PlacedElements placedElements,
						 final Camera camera) {
        batchHandler.createBatches(camera);
        viewAuxHandler.createModels();
        cursorHandler.createCursors(assetsManager, tileModel);
        actionsHandler = new ActionsHandler(map, placedElements, cursorHandler);
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