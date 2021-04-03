package com.necromine.editor.handlers.action;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.WallCreator;
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
import com.necromine.editor.actions.ProcessBuilder;
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
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionsHandler {
	private static final Vector3 auxVector = new Vector3();

	private final ActionHandlerRelatedData data;
	private final ActionHandlerRelatedServices services;
	private final MapManagerEventsNotifier eventsNotifier;

	@Setter
	private ElementDefinition selectedElement;

	@Getter
	private MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess;

	public ActionsHandler(final ActionHandlerRelatedData data,
						  final ActionHandlerRelatedServices services,
						  final MapManagerEventsNotifier eventsNotifier) {
		this.data = data;
		this.services = services;
		this.eventsNotifier = eventsNotifier;
	}


	public void executeAction(final MappingAction mappingAction) {
		mappingAction.execute(eventsNotifier);
		if (mappingAction.isProcess()) {
			currentProcess = (MappingProcess<? extends MappingProcess.FinishProcessParameters>) mappingAction;
		}
	}

	private void beginTilePlacingProcess(final GameAssetsManager assetsManager,
										 final Set<MapNodeData> initializedTiles) {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceTilesProcess placeTilesProcess = new PlaceTilesProcess(
				new FlatNode(row, col),
				assetsManager,
				initializedTiles,
				data.getMap());
		currentProcess = placeTilesProcess;
		placeTilesProcess.execute(eventsNotifier);
	}

	public boolean onTouchDown(final GameAssetsManager assetsManager,
							   final Set<MapNodeData> initializedTiles,
							   final int button,
							   final MapManagerEventsNotifier eventsNotifier) {
		EditorMode mode = MapEditor.getMode();
		Class<? extends EditorMode> modeClass = mode.getClass();
		EditorTool tool = MapEditor.getTool();
		if (button == Input.Buttons.LEFT) {
			if (modeClass.equals(EditModes.class)) {
				GameMap map = data.getMap();
				if (mode == EditModes.TILES && currentProcess == null) {
					if (tool == TilesTools.BRUSH) {
						beginTilePlacingProcess(assetsManager, initializedTiles);
					} else if (tool == TilesTools.LIFT) {
						beginSelectingTileForLiftProcess(1, services.getWallCreator(), initializedTiles);
					} else if (tool == TilesTools.WALL_TILING) {
						tileWall(eventsNotifier);
					}
					return true;
				} else if (mode == EditModes.ENVIRONMENT && currentProcess == null) {
					if (tool == EnvTools.BRUSH && selectedElement != null) {
						placeEnvObject(map, assetsManager);
					} else if (tool == EnvTools.DEFINE) {
						defineSelectedEnvObject();
					}
					return true;
				} else if (mode == EditModes.LIGHTS) {
					placeLight(map, assetsManager);
					return true;
				} else if (selectedElement != null) {
					if (mode == EditModes.CHARACTERS) {
						placeCharacter(map, assetsManager);
						return true;
					} else if (mode == EditModes.PICKUPS) {
						placePickup(map, assetsManager);
						return true;
					}
				}
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

	private void defineSelectedEnvObject() {
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

	private void tileWall(final MapManagerEventsNotifier eventsNotifier) {
		Vector3 cursorPosition = services.getCursorHandler().getHighlighter().transform.getTranslation(auxVector);
		int row = (int) cursorPosition.z;
		int col = (int) cursorPosition.x;
		MapNodeData mapNodeData = data.getMap().getNodes()[row][col];
		if (mapNodeData != null) {
			eventsNotifier.tileSelectedUsingWallTilingTool(row, col, new NodeWallsDefinitions(mapNodeData));
		}
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

	private void placeEnvObject(final GameMap map,
								final GameAssetsManager am) {
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

	private void placePickup(final GameMap map,
							 final GameAssetsManager am) {
		CursorSelectionModel cursorSelectionModel = services.getCursorHandler().getCursorSelectionModel();
		Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlacePickupAction action = new PlacePickupAction(
				map,
				(List<PlacedPickup>) data.getPlacedElements().getPlacedObjects().get(EditModes.PICKUPS),
				map.getNodes()[row][col],
				(ItemDefinition) selectedElement,
				am,
				cursorSelectionModel.getFacingDirection());
		executeAction(action);
	}

	private void placeLight(final GameMap map,
							final GameAssetsManager am) {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceLightAction action = new PlaceLightAction(
				map,
				(List<PlacedLight>) data.getPlacedElements().getPlacedObjects().get(EditModes.LIGHTS),
				map.getNodes()[row][col],
				selectedElement,
				am);
		executeAction(action);
	}

	private boolean beginSelectingTileForLiftProcess(final int direction,
													 final WallCreator wallCreator,
													 final Set<MapNodeData> initializedTiles) {
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		FlatNode src = new FlatNode(row, col);
		currentProcess = ProcessBuilder.begin(data.getMap(), src)
				.liftTiles(direction, wallCreator, initializedTiles)
				.finish();
		return true;
	}

	private void placeCharacter(final GameMap map,
								final GameAssetsManager am) {
		CursorHandler cursorHandler = services.getCursorHandler();
		Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceCharacterAction action = new PlaceCharacterAction(
				map,
				(List<PlacedCharacter>) data.getPlacedElements().getPlacedObjects().get(EditModes.CHARACTERS),
				map.getNodes()[row][col],
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
		Vector3 position = services.getCursorHandler().getCursorTileModelInstance().transform.getTranslation(auxVector);
		int dstRow = (int) position.z;
		int dstCol = (int) position.x;
		if (currentProcess instanceof PlaceTilesProcess) {
			PlaceTilesProcess currentProcess = (PlaceTilesProcess) this.currentProcess;
			currentProcess.finish(new PlaceTilesFinishProcessParameters(dstRow, dstCol, selectedTile, cursorTileModel));
		} else if (currentProcess instanceof SelectTilesForLiftProcess) {
			SelectTilesForLiftProcess currentProcess = (SelectTilesForLiftProcess) this.currentProcess;
			currentProcess.finish(new SelectTilesForLiftFinishProcessParameters(dstRow, dstCol, services.getEventsNotifier()));
		}
		this.currentProcess = null;
	}

	public void onNodeWallsDefined(final NodeWallsDefinitions defs,
								   final int row,
								   final int col,
								   final GameAssetsManager am) {
		MapNodeData[][] nodes = data.getMap().getNodes();
		MapNodeData node = nodes[row][col];
		defineEast(defs, nodes[row][col + 1], am, node);
		defineSouth(defs, nodes[row + 1][col], am, node);
		defineWest(defs, nodes[row][col - 1], am, node);
		defineNorth(defs, nodes[row - 1][col], am, node);
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
		Optional.ofNullable(wall).ifPresent(w -> {
			Material material = wall.getModelInstance().materials.get(0);
			wall.setDefinition(texture);
			TextureAttribute textureAttribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
			textureAttribute.textureDescription.texture = assetsManager.getTexture(texture);
			material.set(textureAttribute);
		});
	}

	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		LiftTilesAction.Parameters params = new LiftTilesAction.Parameters(src, dst, value, services.getWallCreator());
		ActionFactory.liftTiles(data.getMap(), params).execute(eventsNotifier);
	}

	public void onEnvObjectDefined(final PlacedEnvObject element, final float height) {
		ActionFactory.defineEnvObject(data.getMap(), element, height).execute(eventsNotifier);
	}
}
