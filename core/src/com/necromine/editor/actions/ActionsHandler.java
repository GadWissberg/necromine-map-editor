package com.necromine.editor.actions;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.CursorSelectionModel;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapNode;
import com.necromine.editor.NecromineMapEditor;
import com.necromine.editor.Node;
import com.necromine.editor.PlacedElements;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.actions.processes.PlaceTilesFinishProcessParameters;
import com.necromine.editor.actions.processes.PlaceTilesProcess;
import com.necromine.editor.actions.types.PlaceCharacterAction;
import com.necromine.editor.actions.types.PlaceEnvObjectAction;
import com.necromine.editor.actions.types.PlaceLightAction;
import com.necromine.editor.actions.types.PlacePickupAction;
import com.necromine.editor.actions.types.RemoveElementAction;
import com.necromine.editor.model.PlacedCharacter;
import com.necromine.editor.model.PlacedEnvObject;
import com.necromine.editor.model.PlacedLight;
import com.necromine.editor.model.PlacedPickup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ActionsHandler {
    private static final Vector3 auxVector = new Vector3();

    private final GameMap map;
    private final PlacedElements placedElements;

    @Getter
    private final CursorHandler cursorHandler;

    @Setter
    private Assets.FloorsTextures selectedTile;

    @Setter
    private ElementDefinition selectedElement;

    @Getter
    private MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess;

    public void executeAction(final MappingAction mappingAction) {
        mappingAction.execute();
        if (mappingAction.isProcess()) {
            currentProcess = (MappingProcess<? extends MappingProcess.FinishProcessParameters>) mappingAction;
        }
    }

    private void beginTilePlacingProcess(final GameAssetsManager assetsManager,
                                         final Set<MapNode> initializedTiles) {
        Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
        int row = (int) position.z;
        int col = (int) position.x;
        PlaceTilesProcess placeTilesProcess = new PlaceTilesProcess(new Node(row, col), assetsManager, initializedTiles, map);
        currentProcess = placeTilesProcess;
        placeTilesProcess.execute();
    }

    public boolean onTouchDown(final GameAssetsManager assetsManager,
                               final Set<MapNode> initializedTiles,
                               final int button) {
        EditorMode mode = NecromineMapEditor.getMode();
        Class<? extends EditorMode> modeClass = mode.getClass();
        if (button == Input.Buttons.LEFT) {
            if (modeClass.equals(EditModes.class)) {
                if (mode == EditModes.TILES && currentProcess == null && selectedTile != null) {
                    beginTilePlacingProcess(assetsManager, initializedTiles);
                    return true;
                } else if (mode == EditModes.LIGHTS) {
                    placeLight(map, assetsManager);
                    return true;
                } else if (selectedElement != null) {
                    if (mode == EditModes.CHARACTERS) {
                        placeCharacter(map, assetsManager);
                        return true;
                    } else if (mode == EditModes.ENVIRONMENT) {
                        placeEnvObject(map, assetsManager);
                    } else if (mode == EditModes.PICKUPS) {
                        placePickup(map, assetsManager);
                        return true;
                    }
                }
            }
        } else if (button == Input.Buttons.RIGHT && mode instanceof EditModes) {
            removeElementByMode();
        }
        return false;
    }

    private void removeElementByMode() {
        Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
        RemoveElementAction action = new RemoveElementAction(
                map,
                placedElements,
                new Node((int) position.z, (int) position.x),
                (EditModes) NecromineMapEditor.getMode());
        executeAction(action);
    }

    private void placeEnvObject(final GameMap map,
                                final GameAssetsManager am) {
        CursorSelectionModel cursorSelectionModel = cursorHandler.getCursorSelectionModel();
        Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
        int row = (int) position.z;
        int col = (int) position.x;
        PlaceEnvObjectAction action = new PlaceEnvObjectAction(
                map,
                (List<PlacedEnvObject>) placedElements.getPlacedObjects().get(EditModes.ENVIRONMENT),
                new Node(row, col),
                (EnvironmentDefinitions) selectedElement,
                am,
                cursorSelectionModel.getFacingDirection());
        executeAction(action);
    }

    private void placePickup(final GameMap map,
                             final GameAssetsManager am) {
        CursorSelectionModel cursorSelectionModel = cursorHandler.getCursorSelectionModel();
        Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
        int row = (int) position.z;
        int col = (int) position.x;
        PlacePickupAction action = new PlacePickupAction(
                map,
                (List<PlacedPickup>) placedElements.getPlacedObjects().get(EditModes.PICKUPS),
                new Node(row, col),
                (ItemDefinition) selectedElement,
                am,
                cursorSelectionModel.getFacingDirection());
        executeAction(action);
    }

    private void placeLight(final GameMap map,
                            final GameAssetsManager am) {
        Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
        int row = (int) position.z;
        int col = (int) position.x;
        PlaceLightAction action = new PlaceLightAction(
                map,
                (List<PlacedLight>) placedElements.getPlacedObjects().get(EditModes.LIGHTS),
                new Node(row, col),
                selectedElement,
                am);
        executeAction(action);
    }

    private void placeCharacter(final GameMap map,
                                final GameAssetsManager am) {
        Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
        int row = (int) position.z;
        int col = (int) position.x;
        PlaceCharacterAction action = new PlaceCharacterAction(
                map,
                (List<PlacedCharacter>) placedElements.getPlacedObjects().get(EditModes.CHARACTERS),
                new Node(row, col),
                (CharacterDefinition) selectedElement,
                am,
                cursorHandler.getCursorCharacterDecal().getSpriteDirection());
        executeAction(action);
    }

    public boolean onTouchUp(final Assets.FloorsTextures selectedTile,
                             final Model cursorTileModel) {
        boolean result = false;
        if (currentProcess != null) {
            finishProcess(selectedTile, cursorTileModel);
            result = true;
        }
        return result;
    }

    private void finishProcess(final Assets.FloorsTextures selectedTile, final Model cursorTileModel) {
        Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
        PlaceTilesProcess currentProcess = (PlaceTilesProcess) this.currentProcess;
        int dstRow = (int) position.z;
        int dstCol = (int) position.x;
        currentProcess.finish(new PlaceTilesFinishProcessParameters(dstRow, dstCol, selectedTile, cursorTileModel));
        this.currentProcess = null;
    }
}
