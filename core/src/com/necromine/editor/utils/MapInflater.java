package com.necromine.editor.utils;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.assets.MapJsonKeys;
import com.gadarts.necromine.model.*;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.characters.Direction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.necromine.editor.GameMap;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.handlers.ViewAuxHandler;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedElement.PlacedElementParameters;
import com.necromine.editor.model.elements.PlacedElements;
import com.necromine.editor.model.node.FlatNode;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

import static com.gadarts.necromine.assets.MapJsonKeys.*;
import static com.gadarts.necromine.model.characters.Direction.SOUTH;

/**
 * Deserializes map json.
 */
@RequiredArgsConstructor
public class MapInflater {
	private final GameAssetsManager assetsManager;
	private final CursorHandler cursorHandler;
	private final Set<MapNodeData> initializedTiles;
	private final Gson gson = new Gson();
	private GameMap map;

	/**
	 * Deserializes map json into the given map object.
	 *
	 * @param map            The output map.
	 * @param placedElements The placed elements inside the map.
	 * @param wallCreator    The tool used to create walls.
	 * @param viewAuxHandler Stuff used for helping mapping.
	 */
	public void inflateMap(final GameMap map,
						   final PlacedElements placedElements,
						   final WallCreator wallCreator,
						   final ViewAuxHandler viewAuxHandler) {
		this.map = map;
		try (Reader reader = new FileReader("test_map.json")) {
			JsonObject input = gson.fromJson(reader, JsonObject.class);
			JsonObject tilesJsonObject = input.getAsJsonObject(TILES);
			map.setNodes(inflateTiles(tilesJsonObject, initializedTiles, viewAuxHandler));
			inflateHeightsAndWalls(tilesJsonObject, map, wallCreator);
			inflateCharacters(input, placedElements);
			Arrays.stream(EditModes.values()).forEach(mode -> {
				if (!mode.isSkipGenericElementLoading()) {
					inflateElements(input, mode, placedElements, map);
				}
			});
			JsonElement ambient = input.get(AMBIENT);
			Optional.ofNullable(ambient).ifPresent(a -> map.setAmbientLight(a.getAsFloat()));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void inflateHeightsAndWalls(final JsonObject tilesJsonObject,
										final GameMap map,
										final WallCreator wallCreator) {
		JsonElement heights = tilesJsonObject.get(HEIGHTS);
		if (heights != null) {
			heights.getAsJsonArray().forEach(nodeJsonElement -> {
				JsonObject nodeJsonObject = nodeJsonElement.getAsJsonObject();
				int row = nodeJsonObject.get(ROW).getAsInt();
				int col = nodeJsonObject.get(COL).getAsInt();
				MapNodeData mapNodeData = map.getNodes()[row][col];
				mapNodeData.lift(nodeJsonObject.get(HEIGHT).getAsFloat());
			});
			heights.getAsJsonArray().forEach(nodeJsonElement -> {
				JsonObject nodeJsonObject = nodeJsonElement.getAsJsonObject();
				int row = nodeJsonObject.get(ROW).getAsInt();
				int col = nodeJsonObject.get(COL).getAsInt();
				MapNodeData mapNodeData = map.getNodes()[row][col];
				inflateWalls(nodeJsonObject, mapNodeData, map, wallCreator);
			});
		}
	}

	private void inflateWalls(final JsonObject node,
							  final MapNodeData mapNodeData,
							  final GameMap map,
							  final WallCreator wallCreator) {
		MapNodeData[][] nodes = map.getNodes();
		Model wallModel = wallCreator.getWallModel();
		float height = mapNodeData.getHeight();
		Coords coords = mapNodeData.getCoords();
		Optional.ofNullable(node.get(EAST)).ifPresent(east -> {
			MapNodeData eastNode = nodes[coords.getRow()][coords.getCol() + 1];
			if (height != eastNode.getHeight()) {
				inflateEastWall(mapNodeData, wallModel, east, eastNode);
			}
		});
		Optional.ofNullable(node.get(MapJsonKeys.SOUTH)).ifPresent(south -> {
			MapNodeData southNode = nodes[coords.getRow() + 1][coords.getCol()];
			if (height != southNode.getHeight()) {
				inflateSouthWall(mapNodeData, wallModel, south, southNode);
			}
		});
		Optional.ofNullable(node.get(WEST)).ifPresent(west -> {
			MapNodeData westNode = nodes[coords.getRow()][coords.getCol() - 1];
			if (height != westNode.getHeight()) {
				inflateWestWall(mapNodeData, wallModel, west, westNode);
			}
		});
		Optional.ofNullable(node.get(NORTH)).ifPresent(north -> {
			MapNodeData northNode = nodes[coords.getRow() - 1][coords.getCol()];
			if (height != northNode.getHeight()) {
				inflateNorthWall(mapNodeData, wallModel, north, northNode);
			}
		});
	}

	private void inflateNorthWall(final MapNodeData mapNodeData,
								  final Model wallModel,
								  final JsonElement north,
								  final MapNodeData northNode) {
		JsonObject wallJsonObj = north.getAsJsonObject();
		Wall northWall = WallCreator.createNorthWall(
				mapNodeData,
				wallModel,
				assetsManager,
				Assets.SurfaceTextures.valueOf(wallJsonObj.get(TEXTURE).getAsString()));
		mapNodeData.getWalls().setNorthWall(northWall);
		WallCreator.adjustWallBetweenNorthAndSouth(mapNodeData, northNode, defineVScale(wallJsonObj, northWall));
	}

	private void inflateWestWall(final MapNodeData mapNodeData,
								 final Model wallModel,
								 final JsonElement west,
								 final MapNodeData westNode) {
		JsonObject wallJsonObj = west.getAsJsonObject();
		Wall westWall = WallCreator.createWestWall(
				mapNodeData,
				wallModel,
				assetsManager,
				Assets.SurfaceTextures.valueOf(wallJsonObj.get(TEXTURE).getAsString()));
		mapNodeData.getWalls().setWestWall(westWall);
		WallCreator.adjustWallBetweenEastAndWest(mapNodeData, westNode, true, defineVScale(wallJsonObj, westWall));
	}

	private void inflateSouthWall(final MapNodeData mapNodeData,
								  final Model wallModel,
								  final JsonElement south,
								  final MapNodeData southNode) {
		JsonObject wallJsonObj = south.getAsJsonObject();
		Wall southWall = WallCreator.createSouthWall(
				mapNodeData,
				wallModel,
				assetsManager,
				Assets.SurfaceTextures.valueOf(wallJsonObj.get(TEXTURE).getAsString()));
		mapNodeData.getWalls().setSouthWall(southWall);
		WallCreator.adjustWallBetweenNorthAndSouth(southNode, mapNodeData, defineVScale(wallJsonObj, southWall));
	}

	private void inflateEastWall(final MapNodeData mapNodeData,
								 final Model wallModel,
								 final JsonElement east,
								 final MapNodeData eastNode) {
		JsonObject wallJsonObj = east.getAsJsonObject();
		Wall eastWall = WallCreator.createEastWall(
				mapNodeData,
				wallModel,
				assetsManager,
				Assets.SurfaceTextures.valueOf(wallJsonObj.get(TEXTURE).getAsString()));
		mapNodeData.getWalls().setEastWall(eastWall);
		WallCreator.adjustWallBetweenEastAndWest(eastNode, mapNodeData, true, defineVScale(wallJsonObj, eastWall));
	}

	private float defineVScale(final JsonObject wallJsonObj, final Wall eastWall) {
		float vScale = wallJsonObj.has(V_SCALE) ? wallJsonObj.get(V_SCALE).getAsFloat() : 0;
		eastWall.setVScale(vScale);
		return vScale;
	}

	private void inflateElements(final JsonObject input,
								 final EditModes mode,
								 final PlacedElements placedElements,
								 final GameMap map) {
		List<? extends PlacedElement> placedElementsList = placedElements.getPlacedObjects().get(mode);
		placedElementsList.clear();
		inflateElements(
				(List<PlacedElement>) placedElementsList,
				mode,
				input.get(mode.name().toLowerCase()).getAsJsonArray(),
				map);
	}

	private void inflateCharacters(final JsonObject input,
								   final PlacedElements placedElements) {
		JsonObject charactersJsonObject = input.get(CHARACTERS).getAsJsonObject();
		List<? extends PlacedElement> placedCharacters = placedElements.getPlacedObjects().get(EditModes.CHARACTERS);
		placedCharacters.clear();
		Arrays.stream(CharacterTypes.values()).forEach(type -> {
			String typeName = type.name().toLowerCase();
			if (charactersJsonObject.has(typeName)) {
				JsonArray charactersArray = charactersJsonObject.get(typeName).getAsJsonArray();
				inflateElements(
						(List<PlacedElement>) placedCharacters,
						EditModes.CHARACTERS,
						charactersArray,
						type.getDefinitions());
			}
		});
	}

	private void inflateElements(final List<PlacedElement> placedElements,
								 final EditModes mode,
								 final JsonArray elementsJsonArray,
								 final GameMap map) {
		elementsJsonArray.forEach(characterJsonObject -> {
			JsonObject json = characterJsonObject.getAsJsonObject();
			PlacedElementParameters parameters = inflateElementParameters(mode.getDefinitions(), json, map);
			placedElements.add(mode.getCreationProcess().create(parameters, assetsManager));
		});
	}

	private void inflateElements(final List<PlacedElement> placedElements,
								 final EditModes mode,
								 final JsonArray elementsJsonArray,
								 final ElementDefinition[] defs) {
		elementsJsonArray.forEach(characterJsonObject -> {
			JsonObject json = characterJsonObject.getAsJsonObject();
			PlacedElementParameters parameters = inflateElementParameters(defs, json, map);
			placedElements.add(mode.getCreationProcess().create(parameters, assetsManager));
		});
	}

	private PlacedElementParameters inflateElementParameters(final ElementDefinition[] definitions,
															 final JsonObject json,
															 final GameMap map) {
		Direction dir = json.has(DIRECTION) ? Direction.values()[json.get(DIRECTION).getAsInt()] : SOUTH;
		float height = json.has(HEIGHT) ? json.get(HEIGHT).getAsFloat() : 0;
		MapNodeData node = map.getNodes()[json.get(ROW).getAsInt()][json.get(COL).getAsInt()];
		ElementDefinition definition = null;
		if (definitions != null) {
			definition = definitions[json.get(TYPE).getAsInt()];
		}
		return new PlacedElementParameters(definition, dir, node, height);
	}

	private MapNodeData[][] inflateTiles(final JsonObject tilesJsonObject,
										 final Set<MapNodeData> initializedTiles,
										 final ViewAuxHandler viewAuxHandler) {
		int width = tilesJsonObject.get(WIDTH).getAsInt();
		int depth = tilesJsonObject.get(DEPTH).getAsInt();
		viewAuxHandler.createModels(new Dimension(width, depth));
		String matrix = tilesJsonObject.get(MATRIX).getAsString();
		MapNodeData[][] inputMap = new MapNodeData[depth][width];
		initializedTiles.clear();
		byte[] matrixByte = Base64.getDecoder().decode(matrix.getBytes());
		IntStream.range(0, depth)
				.forEach(row -> IntStream.range(0, width)
						.forEach(col -> inflateTile(width, matrixByte, inputMap, new FlatNode(row, col))));
		return inputMap;
	}

	private void inflateTile(final int mapWidth,
							 final byte[] matrix,
							 final MapNodeData[][] inputMap,
							 final FlatNode node) {
		int row = node.getRow();
		int col = node.getCol();
		byte tileId = matrix[row * mapWidth + col % mapWidth];
		MapNodeData tile;
		if (tileId != 0) {
			Assets.SurfaceTextures textureDefinition = Assets.SurfaceTextures.values()[tileId - 1];
			tile = new MapNodeData(cursorHandler.getCursorTileModel(), row, col, MapNodesTypes.PASSABLE_NODE);
			Utils.initializeTile(tile, textureDefinition, assetsManager);
			initializedTiles.add(tile);
		} else {
			tile = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE);
		}
		inputMap[row][col] = tile;
	}

}
