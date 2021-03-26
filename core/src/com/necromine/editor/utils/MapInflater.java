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
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.elements.PlacedCharacter;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedElement.PlacedElementParameters;
import com.necromine.editor.model.elements.PlacedElementCreation;
import com.necromine.editor.model.elements.PlacedElements;
import com.necromine.editor.model.node.Node;
import lombok.RequiredArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.IntStream;

import static com.gadarts.necromine.assets.MapJsonKeys.*;
import static com.gadarts.necromine.model.characters.Direction.SOUTH;

@RequiredArgsConstructor
public class MapInflater {
	private final GameAssetsManager assetsManager;
	private final CursorHandler cursorHandler;
	private final Set<MapNodeData> initializedTiles;
	private final Gson gson = new Gson();

	public void inflateMap(final GameMap map, final PlacedElements placedElements, final WallCreator wallCreator) {
		try (Reader reader = new FileReader("test_map.json")) {
			JsonObject input = gson.fromJson(reader, JsonObject.class);
			inflateCharacters(input, placedElements, assetsManager);
			Arrays.stream(EditModes.values()).forEach(mode -> {
				if (mode.getCreationProcess() != null) {
					inflateElements(input, mode, placedElements);
				}
			});
			JsonObject tilesJsonObject = input.getAsJsonObject(TILES);
			map.setNodes(inflateTiles(tilesJsonObject, initializedTiles));
			JsonElement ambient = input.get(AMBIENT);
			Optional.ofNullable(ambient).ifPresent(a -> map.setAmbientLight(a.getAsFloat()));
			inflateHeights(tilesJsonObject, map, wallCreator);
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
								 final PlacedElements placedElements) {
		List<? extends PlacedElement> placedElementsList = placedElements.getPlacedObjects().get(mode);
		placedElementsList.clear();
		inflateElements(
				(List<PlacedElement>) placedElementsList,
				mode.getDefinitions(),
				input.get(mode.name().toLowerCase()).getAsJsonArray(),
				mode.getCreationProcess());
	}

	private void inflateCharacters(final JsonObject input,
								   final PlacedElements placedElements,
								   final GameAssetsManager assetsManager) {
		JsonObject charactersJsonObject = input.get(CHARACTERS).getAsJsonObject();
		List<? extends PlacedElement> placedCharacters = placedElements.getPlacedObjects().get(EditModes.CHARACTERS);
		placedCharacters.clear();
		Arrays.stream(CharacterTypes.values()).forEach(type -> {
			String typeName = type.name().toLowerCase();
			if (charactersJsonObject.has(typeName)) {
				JsonArray charactersArray = charactersJsonObject.get(typeName).getAsJsonArray();
				inflateElements(
						(List<PlacedElement>) placedCharacters,
						type.getDefinitions(),
						charactersArray,
						(params, am) -> new PlacedCharacter(params, assetsManager));
			}
		});
	}

	private void inflateElements(final List<PlacedElement> placedElements,
								 final ElementDefinition[] definitions,
								 final JsonArray elementsJsonArray,
								 final PlacedElementCreation creation) {
		elementsJsonArray.forEach(characterJsonObject -> {
			JsonObject json = characterJsonObject.getAsJsonObject();
			PlacedElementParameters parameters = inflateElementParameters(definitions, json);
			placedElements.add(creation.create(parameters, assetsManager));
		});
	}

	private PlacedElementParameters inflateElementParameters(final ElementDefinition[] definitions,
															 final JsonObject json) {
		Direction dir = json.has(DIRECTION) ? Direction.values()[json.get(DIRECTION).getAsInt()] : SOUTH;
		float height = json.has(HEIGHT) ? json.get(HEIGHT).getAsFloat() : 0;
		Node node = new Node(json.get(ROW).getAsInt(), json.get(COL).getAsInt());
		ElementDefinition definition = null;
		if (definitions != null) {
			definition = definitions[json.get(TYPE).getAsInt()];
		}
		return new PlacedElementParameters(definition, dir, node, height);
	}

	private MapNodeData[][] inflateTiles(final JsonObject tilesJsonObject, final Set<MapNodeData> initializedTiles) {
		int width = tilesJsonObject.get(WIDTH).getAsInt();
		int depth = tilesJsonObject.get(DEPTH).getAsInt();
		String matrix = tilesJsonObject.get(MATRIX).getAsString();
		MapNodeData[][] inputMap = new MapNodeData[depth][width];
		initializedTiles.clear();
		byte[] matrixByte = Base64.getDecoder().decode(matrix.getBytes());
		IntStream.range(0, depth)
				.forEach(row -> IntStream.range(0, width)
						.forEach(col -> inflateTile(width, matrixByte, inputMap, new Node(row, col))));
		return inputMap;
	}

	private void inflateTile(final int mapWidth,
							 final byte[] matrix,
							 final MapNodeData[][] inputMap,
							 final Node node) {
		int row = node.getRow();
		int col = node.getCol();
		byte tileId = matrix[row * mapWidth + col % mapWidth];
		if (tileId != 0) {
			Assets.FloorsTextures textureDefinition = Assets.FloorsTextures.values()[tileId - 1];
			MapNodeData tile = new MapNodeData(cursorHandler.getCursorTileModel(), node.getRow(), node.getCol(), MapNodesTypes.PASSABLE_NODE);
			Utils.initializeTile(tile, textureDefinition, assetsManager);
			inputMap[row][col] = tile;
			initializedTiles.add(tile);
		}
	}

}
