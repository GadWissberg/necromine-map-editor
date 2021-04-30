package com.necromine.editor.handlers.action;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.*;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.CursorSelectionModel;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditor;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.actions.processes.*;
import com.necromine.editor.actions.types.*;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.EnvTools;
import com.necromine.editor.mode.tools.TilesTools;
import com.necromine.editor.model.elements.*;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.model.node.NodeWallsDefinitions;
import com.necromine.editor.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionsHandlerImpl implements ActionsHandler {
	private static final Vector3 auxVector = new Vector3();
	private final ActionHandlerRelatedData data;
	private final ActionHandlerRelatedServices services;
	private final MapManagerEventsNotifier eventsNotifier;

	@Setter
	private ElementDefinition selectedElement;

	@Getter
	private MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess;

	public ActionsHandlerImpl(final ActionHandlerRelatedData data,
							  final ActionHandlerRelatedServices services,
							  final MapManagerEventsNotifier eventsNotifier) {
		this.data = data;
		this.services = services;
		this.eventsNotifier = eventsNotifier;
	}

	/**
	 * Executes the given action/process.
	 *
	 * @param mappingAction The action to execute.
	 */
	public void executeAction(final MappingAction mappingAction) {
		mappingAction.execute(eventsNotifier);
		if (mappingAction.isProcess()) {
			currentProcess = (MappingProcess<? extends MappingProcess.FinishProcessParameters>) mappingAction;
		}
	}

	@Override
	public void beginTilePlacingProcess(final GameAssetsManager assetsManager,
										final Set<MapNodeData> initializedTiles) {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		PlaceTilesProcess placeTilesProcess = new PlaceTilesProcess(
				new FlatNode((int) position.z, (int) position.x),
				assetsManager,
				initializedTiles,
				data.getMap());
		currentProcess = placeTilesProcess;
		placeTilesProcess.execute(eventsNotifier);
	}

	/**
	 * Called when the mouse is pressed.
	 *
	 * @param assetsManager
	 * @param initializedTiles
	 * @param button
	 * @return Whether an action is taken in response to this event.
	 */
	@SuppressWarnings("JavaDoc")
	public boolean onTouchDown(final GameAssetsManager assetsManager,
							   final Set<MapNodeData> initializedTiles,
							   final int button) {
		EditorMode mode = MapEditor.getMode();
		Class<? extends EditorMode> modeClass = mode.getClass();
		EditorTool tool = MapEditor.getTool();
		if (button == Input.Buttons.LEFT) {
			if (modeClass.equals(EditModes.class)) {
				mode.onTouchDownLeft(currentProcess, this, assetsManager, initializedTiles);
			}
		} else if (button == Input.Buttons.RIGHT) {
			if (mode instanceof EditModes) {
				if (tool == TilesTools.BRUSH || tool == EnvTools.BRUSH) {
					return removeElementByMode();
				}
			}
		}
		return false;
	}


	@Override
	public void defineSelectedEnvObject() {
		MapNodeData mapNodeData = getMapNodeDataFromCursor();
		List<? extends PlacedElement> list = this.data.getPlacedElements().getPlacedObjects().get(MapEditor.getMode());
		List<PlacedElement> elementsInTheNode = list.stream()
				.filter(placedElement -> placedElement.getNode().equals(mapNodeData))
				.collect(Collectors.toList());
		int size = elementsInTheNode.size();
		if (size == 1) {
			eventsNotifier.selectedEnvObjectToDefine((PlacedEnvObject) elementsInTheNode.get(0));
		} else if (size > 1) {
			defineSelectedEnvObjectsInNode(mapNodeData, elementsInTheNode);
		}
	}

	private void defineSelectedEnvObjectsInNode(final MapNodeData mapNodeData,
												final List<PlacedElement> elementsInTheNode) {
		if (mapNodeData != null) {
			ActionAnswer<PlacedElement> answer = new ActionAnswer<>(data ->
					eventsNotifier.selectedEnvObjectToDefine((PlacedEnvObject) data));
			eventsNotifier.nodeSelectedToSelectObjectsInIt(elementsInTheNode, answer);
		}
	}

	private MapNodeData getMapNodeDataFromCursor() {
		Vector3 cursorPosition = services.getCursorHandler().getHighlighter().transform.getTranslation(auxVector);
		int row = (int) cursorPosition.z;
		int col = (int) cursorPosition.x;
		return data.getMap().getNodes()[row][col];
	}

	private boolean removeElementByMode() {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		RemoveElementAction action = new RemoveElementAction(
				data.getMap(),
				data.getPlacedElements(),
				new FlatNode((int) position.z, (int) position.x),
				(EditModes) MapEditor.getMode());
		executeAction(action);
		return true;
	}

	@Override
	public void placeEnvObject(final GameAssetsManager am) {
		GameMap map = data.getMap();
		CursorSelectionModel cursorSelectionModel = services.getCursorHandler().getCursorSelectionModel();
		Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		MapNodeData[][] nodes = map.getNodes();
		MapNodeData node = nodes[row][col];
		if (node == null) {
			node = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE);
			nodes[row][col] = node;
		}
		PlaceEnvObjectAction action = new PlaceEnvObjectAction(
				map,
				(List<PlacedEnvObject>) data.getPlacedElements().getPlacedObjects().get(EditModes.ENVIRONMENT),
				node,
				(EnvironmentDefinitions) selectedElement,
				am,
				cursorSelectionModel.getFacingDirection());
		executeAction(action);
	}

	@Override
	public void placePickup(final GameAssetsManager am) {
		CursorSelectionModel cursorSelectionModel = services.getCursorHandler().getCursorSelectionModel();
		Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.getMap();
		PlacePickupAction action = new PlacePickupAction(
				map,
				(List<PlacedPickup>) data.getPlacedElements().getPlacedObjects().get(EditModes.PICKUPS),
				map.getNodes()[row][col],
				(ItemDefinition) selectedElement,
				am,
				cursorSelectionModel.getFacingDirection());
		executeAction(action);
	}

	@Override
	public void placeLight(final GameAssetsManager am) {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.getMap();
		PlaceLightAction action = new PlaceLightAction(
				map,
				(List<PlacedLight>) data.getPlacedElements().getPlacedObjects().get(EditModes.LIGHTS),
				map.getNodes()[row][col],
				selectedElement,
				am);
		executeAction(action);
	}

	@Override
	public boolean beginSelectingTileForLiftProcess(final int direction,
													final Set<MapNodeData> initializedTiles) {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		FlatNode src = new FlatNode((int) position.z, (int) position.x);
		SelectTilesForLiftProcess current = new SelectTilesForLiftProcess(data.getMap(), src);
		current.setDirection(direction);
		current.setWallCreator(services.getWallCreator());
		current.setInitializedTiles(initializedTiles);
		currentProcess = current;
		return true;
	}

	@Override
	public void beginSelectingTilesForWallTiling() {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		FlatNode src = new FlatNode((int) position.z, (int) position.x);
		currentProcess = new SelectTilesForWallTilingProcess(data.getMap(), src);
	}

	@Override
	public ElementDefinition getSelectedElement() {
		return selectedElement;
	}

	@Override
	public void placeCharacter(final GameAssetsManager am) {
		CursorHandler cursorHandler = services.getCursorHandler();
		Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.getMap();
		PlaceCharacterAction action = new PlaceCharacterAction(
				map,
				(List<PlacedCharacter>) data.getPlacedElements().getPlacedObjects().get(EditModes.CHARACTERS),
				map.getNodes()[row][col],
				(CharacterDefinition) selectedElement,
				am,
				cursorHandler.getCursorCharacterDecal().getSpriteDirection());
		executeAction(action);
	}

	/**
	 * Called when the mouse button is released.
	 *
	 * @param selectedTile
	 * @param cursorTileModel
	 * @return Whether an action taken in response to this event.
	 */
	@SuppressWarnings("JavaDoc")
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
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		int dstRow = (int) position.z;
		int dstCol = (int) position.x;
		if (currentProcess instanceof PlaceTilesProcess) {
			PlaceTilesProcess currentProcess = (PlaceTilesProcess) this.currentProcess;
			currentProcess.finish(new PlaceTilesFinishProcessParameters(dstRow, dstCol, selectedTile, cursorTileModel));
		} else if (currentProcess instanceof SelectTilesForLiftProcess) {
			SelectTilesForLiftProcess currentProcess = (SelectTilesForLiftProcess) this.currentProcess;
			currentProcess.finish(new SelectTilesForLiftFinishProcessParameters(dstRow, dstCol, services.getEventsNotifier()));
		} else if (currentProcess instanceof SelectTilesForWallTilingProcess) {
			SelectTilesForWallTilingProcess currentProcess = (SelectTilesForWallTilingProcess) this.currentProcess;
			currentProcess.finish(new SelectTilesForWallTilingFinishProcessParameters(dstRow, dstCol, services.getEventsNotifier()));
		}
		this.currentProcess = null;
	}

	/**
	 * Called when a node's walls were defined.
	 *
	 * @param defs
	 * @param src
	 * @param dst
	 * @param assetsManager
	 */
	@SuppressWarnings("JavaDoc")
	public void onNodeWallsDefined(final NodeWallsDefinitions defs,
								   final FlatNode src,
								   final FlatNode dst,
								   final GameAssetsManager assetsManager) {
		Utils.applyOnRegionOfTiles(src, dst, (row, col) -> {
			MapNodeData[][] nodes = data.getMap().getNodes();
			MapNodeData node = nodes[row][col];
			defineEast(defs, nodes[row][col + 1], assetsManager, node);
			defineSouth(defs, nodes[row + 1][col], assetsManager, node);
			defineWest(defs, nodes[row][col - 1], assetsManager, node);
			defineNorth(defs, nodes[row - 1][col], assetsManager, node);
		});
	}

	private void defineNorth(final NodeWallsDefinitions defs,
							 final MapNodeData mapNodeData,
							 final GameAssetsManager am,
							 final MapNodeData node) {
		Wall neighborWall = mapNodeData != null ? mapNodeData.getSouthWall() : null;
		defineWall(am, node.getNorthWall(), neighborWall, defs.getNorth());
	}

	private void defineWest(final NodeWallsDefinitions defs,
							final MapNodeData mapNodeData,
							final GameAssetsManager am,
							final MapNodeData node) {
		Wall neighborWall = mapNodeData != null ? mapNodeData.getEastWall() : null;
		defineWall(am, node.getWestWall(), neighborWall, defs.getWest());
	}

	private void defineSouth(final NodeWallsDefinitions defs,
							 final MapNodeData mapNodeData,
							 final GameAssetsManager am,
							 final MapNodeData node) {
		Wall neighborWall = mapNodeData != null ? mapNodeData.getNorthWall() : null;
		defineWall(am, node.getSouthWall(), neighborWall, defs.getSouth());
	}

	private void defineEast(final NodeWallsDefinitions defs,
							final MapNodeData neighborNode,
							final GameAssetsManager am,
							final MapNodeData node) {
		Wall neighborWall = neighborNode != null ? neighborNode.getWestWall() : null;
		defineWall(am, node.getEastWall(), neighborWall, defs.getEast());
	}

	private void defineWall(final GameAssetsManager assetsManager,
							final Wall selectedWall,
							final Wall neighborWall,
							final Assets.FloorsTextures texture) {
		Wall wall = Optional.ofNullable(selectedWall).orElse(neighborWall);
		Optional.ofNullable(wall).flatMap(w -> Optional.ofNullable(texture)).ifPresent(t -> {
			Material material = wall.getModelInstance().materials.get(0);
			wall.setDefinition(texture);
			TextureAttribute textureAttribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
			textureAttribute.textureDescription.texture = assetsManager.getTexture(texture);
			material.set(textureAttribute);
		});
	}

	/**
	 * Called when tiles height was changed.
	 *
	 * @param src
	 * @param dst
	 * @param value
	 */
	@SuppressWarnings("JavaDoc")
	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		LiftTilesAction.Parameters params = new LiftTilesAction.Parameters(src, dst, value, services.getWallCreator());
		ActionFactory.liftTiles(data.getMap(), params).execute(eventsNotifier);
	}

	/**
	 * Called when an environment object was defined.
	 *
	 * @param element
	 * @param height
	 */
	@SuppressWarnings("JavaDoc")
	public void onEnvObjectDefined(final PlacedEnvObject element, final float height) {
		ActionFactory.defineEnvObject(data.getMap(), element, height).execute(eventsNotifier);
	}
}
