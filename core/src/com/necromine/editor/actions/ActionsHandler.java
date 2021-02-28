package com.necromine.editor.actions;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.CursorSelectionModel;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditor;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.actions.processes.PlaceTilesFinishProcessParameters;
import com.necromine.editor.actions.processes.PlaceTilesProcess;
import com.necromine.editor.actions.types.*;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.EditorTool;
import com.necromine.editor.mode.TilesTools;
import com.necromine.editor.model.elements.*;
import com.necromine.editor.model.node.MapNode;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.node.NodeWallsDefinitions;
import com.necromine.editor.model.node.Wall;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ActionsHandler {
	private static final Vector3 auxVector = new Vector3();

	private final GameMap map;
	private final PlacedElements placedElements;


	@Getter
	private final CursorHandler cursorHandler;

	@Setter
	private ElementDefinition selectedElement;

	@Getter
	private MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess;
	private Model wallModel;

	public ActionsHandler(final GameMap map, final PlacedElements placedElements, final CursorHandler cursorHandler) {
		this.map = map;
		this.placedElements = placedElements;
		this.cursorHandler = cursorHandler;
		createWallModel();
	}

	private void createWallModel() {
		ModelBuilder modelBuilder = new ModelBuilder();
		wallModel = modelBuilder.createRect(
				0, 0, 1,
				1, 0, 1,
				1, 0, 0,
				0, 0, 0,
				0, 1, 0,
				new Material(TextureAttribute.createDiffuse((Texture) null)),
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);
	}

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
							   final int button,
							   final MapManagerEventsNotifier eventsNotifier) {
		EditorMode mode = MapEditor.getMode();
		Class<? extends EditorMode> modeClass = mode.getClass();
		EditorTool tool = MapEditor.getTool();
		if (button == Input.Buttons.LEFT) {
			if (modeClass.equals(EditModes.class)) {
				if (mode == EditModes.TILES && currentProcess == null) {
					if (tool == TilesTools.BRUSH) {
						beginTilePlacingProcess(assetsManager, initializedTiles);
					} else if (tool == TilesTools.LIFT) {
						liftTile(map, 1, assetsManager);
					} else if (tool == TilesTools.WALL_TILING) {
						tileWall(eventsNotifier);
					}
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
		} else if (button == Input.Buttons.RIGHT) {
			if (mode instanceof EditModes) {
				if (tool == TilesTools.BRUSH) {
					return removeElementByMode();
				} else if (tool == TilesTools.LIFT) {
					return liftTile(map, -1, assetsManager);
				}
			}
		}
		return false;
	}

	private void tileWall(final MapManagerEventsNotifier eventsNotifier) {
		Vector3 cursorPosition = cursorHandler.getHighlighter().transform.getTranslation(auxVector);
		int row = (int) cursorPosition.z;
		int col = (int) cursorPosition.x;
		MapNode mapNode = map.getTiles()[row][col];
		if (mapNode != null) {
			eventsNotifier.tileSelectedUsingWallTilingTool(row, col, new NodeWallsDefinitions(mapNode));
		}
	}

	private boolean removeElementByMode() {
		Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
		RemoveElementAction action = new RemoveElementAction(
				map,
				placedElements,
				new Node((int) position.z, (int) position.x),
				(EditModes) MapEditor.getMode());
		executeAction(action);
		return true;
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

	private boolean liftTile(final GameMap map, final int direction, final GameAssetsManager assetsManager) {
		Vector3 position = cursorHandler.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		executeAction(ActionBuilder.begin(map).liftTile(new Node(row, col), direction, wallModel, assetsManager).finish());
		return true;
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

	public void onNodeWallsDefined(final NodeWallsDefinitions defs,
								   final int row,
								   final int col,
								   final GameAssetsManager am) {
		MapNode[][] nodes = map.getTiles();
		MapNode node = nodes[row][col];
		defineEast(defs, nodes[row][col + 1], am, node);
		defineSouth(defs, nodes[row + 1][col], am, node);
		defineWest(defs, nodes[row][col - 1], am, node);
		defineNorth(defs, nodes[row - 1][col], am, node);
	}

	private void defineNorth(final NodeWallsDefinitions defs,
							 final MapNode mapNode,
							 final GameAssetsManager am,
							 final MapNode node) {
		Wall neighborWall = mapNode != null ? mapNode.getSouthWall() : null;
		defineWall(am, node.getNorthWall(), neighborWall, defs.getNorth());
	}

	private void defineWest(final NodeWallsDefinitions defs,
							final MapNode mapNode,
							final GameAssetsManager am,
							final MapNode node) {
		Wall neighborWall = mapNode != null ? mapNode.getEastWall() : null;
		defineWall(am, node.getWestWall(), neighborWall, defs.getWest());
	}

	private void defineSouth(final NodeWallsDefinitions defs,
							 final MapNode mapNode,
							 final GameAssetsManager am,
							 final MapNode node) {
		Wall neighborWall = mapNode != null ? mapNode.getNorthWall() : null;
		defineWall(am, node.getSouthWall(), neighborWall, defs.getSouth());
	}

	private void defineEast(final NodeWallsDefinitions defs,
							final MapNode neighborNode,
							final GameAssetsManager am,
							final MapNode node) {
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
}
