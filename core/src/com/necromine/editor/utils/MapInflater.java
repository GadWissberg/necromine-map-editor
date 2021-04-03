package com.necromine.editor.utils;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.assets.MapJsonKeys;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.MapNodesTypes;
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
import com.necromine.editor.model.elements.PlacedLight;
import com.necromine.editor.model.node.FlatNode;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static com.gadarts.necromine.assets.MapJsonKeys.AMBIENT;
import static com.gadarts.necromine.assets.MapJsonKeys.CHARACTERS;
import static com.gadarts.necromine.assets.MapJsonKeys.COL;
import static com.gadarts.necromine.assets.MapJsonKeys.DEPTH;
import static com.gadarts.necromine.assets.MapJsonKeys.DIRECTION;
import static com.gadarts.necromine.assets.MapJsonKeys.EAST;
import static com.gadarts.necromine.assets.MapJsonKeys.HEIGHT;
import static com.gadarts.necromine.assets.MapJsonKeys.HEIGHTS;
import static com.gadarts.necromine.assets.MapJsonKeys.MATRIX;
import static com.gadarts.necromine.assets.MapJsonKeys.NORTH;
import static com.gadarts.necromine.assets.MapJsonKeys.ROW;
import static com.gadarts.necromine.assets.MapJsonKeys.TILES;
import static com.gadarts.necromine.assets.MapJsonKeys.TYPE;
import static com.gadarts.necromine.assets.MapJsonKeys.WEST;
import static com.gadarts.necromine.assets.MapJsonKeys.WIDTH;
import static com.gadarts.necromine.model.characters.Direction.SOUTH;

@RequiredArgsConstructor
public class MapInflater {
	private final GameAssetsManager assetsManager;
	private final CursorHandler cursorHandler;
	private final Set<MapNodeData> initializedTiles;
	private final Gson gson = new Gson();
	private GameMap map;

	public void inflateMap(final GameMap map,
						   final PlacedElements placedElements,
						   final WallCreator wallCreator,
						   final ViewAuxHandler viewAuxHandler) {
		this.map = map;
		try (Reader reader = new FileReader("test_map.json")) {
			JsonObject input = gson.fromJson(reader, JsonObject.class);
			JsonObject tilesJsonObject = input.getAsJsonObject(TILES);
			map.setNodes(inflateTiles(tilesJsonObject, initializedTiles, viewAuxHandler));
			inflateHeights(tilesJsonObject, map, wallCreator);
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

	private void inflateHeights(final JsonObject tilesJsonObject, final GameMap map, final WallCreator wallCreator) {
		JsonElement heights = tilesJsonObject.get(HEIGHTS);
		if (heights != null) {
			heights.getAsJsonArray().forEach(nodeJsonElement -> {
				JsonObject nodeJsonObject = nodeJsonElement.getAsJsonObject();
				int row = nodeJsonObject.get(ROW).getAsInt();
				int col = nodeJsonObject.get(COL).getAsInt();
				MapNodeData mapNodeData = map.getNodes()[row][col];
				mapNodeData.lift(nodeJsonObject.get(HEIGHT).getAsFloat());
				inflateWalls(nodeJsonObject, mapNodeData, map, wallCreator);
			});
		}
	}

	private void inflateWalls(final JsonObject node,
							  final MapNodeData mapNodeData,
							  final GameMap map,
							  final WallCreator wallCreator) {
		MapNodeData[][] nodes = map.getNodes();
		int row = mapNodeData.getRow();
		int col = mapNodeData.getCol();
		Model wallModel = wallCreator.getWallModel();
		Optional.ofNullable(node.get(EAST)).ifPresent(east -> {
			mapNodeData.setEastWall(WallCreator.createEastWall(
					mapNodeData,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(east.getAsString())
			));
			WallCreator.adjustWallBetweenEastAndWest(nodes[row][col + 1], mapNodeData, true);
		});
		Optional.ofNullable(node.get(MapJsonKeys.SOUTH)).ifPresent(south -> {
			mapNodeData.setSouthWall(WallCreator.createSouthWall(
					mapNodeData,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(south.getAsString())
			));
			WallCreator.adjustWallBetweenNorthAndSouth(nodes[row + 1][col], mapNodeData);
		});
		Optional.ofNullable(node.get(WEST)).ifPresent(west -> {
			mapNodeData.setWestWall(WallCreator.createWestWall(
					mapNodeData,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(west.getAsString())
			));
			WallCreator.adjustWallBetweenEastAndWest(mapNodeData, nodes[row][col - 1], true);
		});
		Optional.ofNullable(node.get(NORTH)).ifPresent(north -> {
			mapNodeData.setNorthWall(WallCreator.createNorthWall(
					mapNodeData,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(north.getAsString())
			));
			WallCreator.adjustWallBetweenNorthAndSouth(mapNodeData, nodes[row - 1][col]);
		});
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
			Assets.FloorsTextures textureDefinition = Assets.FloorsTextures.values()[tileId - 1];
			tile = new MapNodeData(cursorHandler.getCursorTileModel(), row, col, MapNodesTypes.PASSABLE_NODE);
			Utils.initializeTile(tile, textureDefinition, assetsManager);
			initializedTiles.add(tile);
		} else {
			tile = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE);
		}
		inputMap[row][col] = tile;
	}

}
