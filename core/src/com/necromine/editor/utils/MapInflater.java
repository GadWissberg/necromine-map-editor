package com.necromine.editor.utils;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.assets.MapJsonKeys;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.MapNodesTypes;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.characters.Direction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.CursorHandler;
import com.necromine.editor.actions.types.LiftTileAction;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.elements.PlacedCharacter;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedElementCreation;
import com.necromine.editor.model.elements.PlacedElements;
import com.necromine.editor.model.node.MapNode;
import com.necromine.editor.model.node.Node;
import lombok.RequiredArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static com.gadarts.necromine.model.characters.Direction.SOUTH;

@RequiredArgsConstructor
public class MapInflater {
	private final GameAssetsManager assetsManager;
	private final CursorHandler cursorHandler;
	private final Set<MapNode> initializedTiles;
	private final Gson gson = new Gson();

	public void inflateMap(final GameMap map, final PlacedElements placedElements, final Model wallModel) {
		try (Reader reader = new FileReader("test_map.json")) {
			JsonObject input = gson.fromJson(reader, JsonObject.class);
			inflateCharacters(input, placedElements, assetsManager);
			Arrays.stream(EditModes.values()).forEach(mode -> {
				if (mode.getCreationProcess() != null) {
					inflateElements(input, mode, placedElements);
				}
			});
			JsonObject tilesJsonObject = input.getAsJsonObject(MapJsonKeys.TILES);
			map.setNodes(inflateTiles(tilesJsonObject, initializedTiles));
			inflateHeights(tilesJsonObject, map, wallModel);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void inflateHeights(final JsonObject tilesJsonObject, final GameMap map, final Model wallModel) {
		JsonArray heights = tilesJsonObject.get(MapJsonKeys.HEIGHTS).getAsJsonArray();
		if (heights != null) {
			heights.forEach(nodeJsonElement -> {
				JsonObject nodeJsonObject = nodeJsonElement.getAsJsonObject();
				int row = nodeJsonObject.get(MapJsonKeys.ROW).getAsInt();
				int col = nodeJsonObject.get(MapJsonKeys.COL).getAsInt();
				MapNode mapNode = map.getNodes()[row][col];
				mapNode.lift(nodeJsonObject.get(MapJsonKeys.HEIGHT).getAsFloat());
				inflateWalls(wallModel, nodeJsonObject, mapNode, map);
			});
		}
	}

	private void inflateWalls(final Model wallModel, final JsonObject node, final MapNode mapNode, final GameMap map) {
		MapNode[][] nodes = map.getNodes();
		int row = mapNode.getRow();
		int col = mapNode.getCol();
		Optional.ofNullable(node.get(MapJsonKeys.EAST)).ifPresent(east -> {
			LiftTileAction.createEastWall(
					mapNode,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(east.getAsString())
			);
			LiftTileAction.adjustWallBetweenEastAndWest(nodes[row][col + 1], mapNode);
		});
		Optional.ofNullable(node.get(MapJsonKeys.SOUTH)).ifPresent(south -> {
			LiftTileAction.createSouthWall(
					mapNode,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(south.getAsString())
			);
			LiftTileAction.adjustWallBetweenNorthAndSouth(nodes[row + 1][col], mapNode);
		});
		Optional.ofNullable(node.get(MapJsonKeys.WEST)).ifPresent(west -> {
			LiftTileAction.createWestWall(
					mapNode,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(west.getAsString())
			);
			LiftTileAction.adjustWallBetweenEastAndWest(mapNode, nodes[row][col - 1]);
		});
		Optional.ofNullable(node.get(MapJsonKeys.NORTH)).ifPresent(north -> {
			LiftTileAction.createNorthWall(
					mapNode,
					wallModel,
					assetsManager,
					Assets.FloorsTextures.valueOf(north.getAsString())
			);
			LiftTileAction.adjustWallBetweenNorthAndSouth(mapNode, nodes[row - 1][col]);
		});
	}

	private void inflateElements(final JsonObject input,
								 final EditModes mode, final PlacedElements placedElements) {
		List<? extends PlacedElement> placedElementsList = placedElements.getPlacedObjects().get(mode);
		placedElementsList.clear();
		inflateElements(
				(List<PlacedElement>) placedElementsList,
				mode.getDefinitions(),
				input.get(mode.name().toLowerCase()).getAsJsonArray(),
				mode.getCreationProcess(), assetsManager);
	}

	private void inflateCharacters(final JsonObject input,
								   final PlacedElements placedElements,
								   final GameAssetsManager assetsManager) {
		JsonObject charactersJsonObject = input.get(MapJsonKeys.CHARACTERS).getAsJsonObject();
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
						(def, node, dir, am) -> new PlacedCharacter((CharacterDefinition) def, node, assetsManager, dir), assetsManager);
			}
		});
	}

	private void inflateElements(final List<PlacedElement> placedElements,
								 final ElementDefinition[] definitions,
								 final JsonArray elementsJsonArray,
								 final PlacedElementCreation creation, final GameAssetsManager assetsManager) {
		elementsJsonArray.forEach(characterJsonObject -> {
			JsonObject json = characterJsonObject.getAsJsonObject();
			Direction direction = SOUTH;
			if (json.has(MapJsonKeys.DIRECTION)) {
				direction = Direction.values()[json.get(MapJsonKeys.DIRECTION).getAsInt()];
			}
			Node node = new Node(json.get(MapJsonKeys.ROW).getAsInt(), json.get(MapJsonKeys.COL).getAsInt());
			ElementDefinition definition = null;
			if (definitions != null) {
				definition = definitions[json.get(MapJsonKeys.TYPE).getAsInt()];
			}
			placedElements.add(creation.create(definition, node, direction, assetsManager));
		});
	}

	private MapNode[][] inflateTiles(final JsonObject tilesJsonObject, final Set<MapNode> initializedTiles) {
		int width = tilesJsonObject.get(MapJsonKeys.WIDTH).getAsInt();
		int depth = tilesJsonObject.get(MapJsonKeys.DEPTH).getAsInt();
		String matrix = tilesJsonObject.get(MapJsonKeys.MATRIX).getAsString();
		MapNode[][] inputMap = new MapNode[depth][width];
		initializedTiles.clear();
		IntStream.range(0, depth)
				.forEach(row -> IntStream.range(0, width)
						.forEach(col -> inflateTile(width, matrix, inputMap, new Node(row, col))));
		return inputMap;
	}

	private void inflateTile(final int mapWidth,
							 final String matrix,
							 final MapNode[][] inputMap,
							 final Node node) {
		int row = node.getRow();
		int col = node.getCol();
		char tileId = matrix.charAt(row * mapWidth + col % mapWidth);
		if (tileId != '0') {
			Assets.FloorsTextures textureDefinition = Assets.FloorsTextures.values()[tileId - '1'];
			MapNode tile = new MapNode(cursorHandler.getCursorTileModel(), node.getRow(), node.getCol(), MapNodesTypes.PASSABLE_NODE);
			Utils.initializeTile(tile, textureDefinition, assetsManager);
			inputMap[row][col] = tile;
			initializedTiles.add(tile);
		}
	}

}
