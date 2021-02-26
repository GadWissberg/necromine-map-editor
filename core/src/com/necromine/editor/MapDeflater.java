package com.necromine.editor;

import com.gadarts.necromine.assets.MapJsonKeys;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.PlacedElement;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.necromine.editor.MapHandler.LEVEL_SIZE;
import static com.necromine.editor.MapHandler.TARGET_VERSION;

public class MapDeflater {
	private final Gson gson = new Gson();

	public void deflate(final GameMap map, final PlacedElements placedElements) {
		JsonObject output = new JsonObject();
		output.addProperty(MapJsonKeys.KEY_TARGET, TARGET_VERSION);
		JsonObject tiles = createTilesData(map);
		output.add(MapJsonKeys.KEY_TILES, tiles);
		addCharacters(output, placedElements);
		addElementsGroup(output, EditModes.ENVIRONMENT, true, placedElements);
		addElementsGroup(output, EditModes.PICKUPS, false, placedElements);
		addElementsGroup(output, EditModes.LIGHTS, false, placedElements);
		try (Writer writer = new FileWriter("test_map.json")) {
			gson.toJson(output, writer);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void addElementsGroup(final JsonObject output,
								  final EditModes mode,
								  final boolean addFacingDirection,
								  final PlacedElements placedElements) {
		JsonArray jsonArray = new JsonArray();
		placedElements.getPlacedObjects().get(mode).forEach(element -> jsonArray.add(createElementJsonObject(element, addFacingDirection)));
		output.add(mode.name().toLowerCase(), jsonArray);
	}

	private void addCharacters(final JsonObject output, final PlacedElements placedElements) {
		JsonObject charactersJsonObject = new JsonObject();
		Arrays.stream(CharacterTypes.values()).forEach(type -> {
			JsonArray charactersJsonArray = new JsonArray();
			charactersJsonObject.add(type.name().toLowerCase(), charactersJsonArray);
			placedElements.getPlacedObjects().get(EditModes.CHARACTERS).stream()
					.filter(character -> ((CharacterDefinition) character.getDefinition()).getCharacterType() == type)
					.forEach(character -> charactersJsonArray.add(createElementJsonObject(character, true)));
		});
		output.add(MapJsonKeys.KEY_CHARACTERS, charactersJsonObject);
	}

	private JsonObject createElementJsonObject(final PlacedElement e, final boolean addFacingDirection) {
		JsonObject charJsonObject = new JsonObject();
		charJsonObject.addProperty(MapJsonKeys.KEY_ROW, e.getNode().getRow());
		charJsonObject.addProperty(MapJsonKeys.KEY_COL, e.getNode().getCol());
		if (addFacingDirection) {
			charJsonObject.addProperty(MapJsonKeys.KEY_DIRECTION, e.getFacingDirection().ordinal());
		}
		ElementDefinition definition = e.getDefinition();
		Optional.ofNullable(definition).ifPresent(d -> charJsonObject.addProperty(MapJsonKeys.KEY_TYPE, d.ordinal()));
		return charJsonObject;
	}

	private JsonObject createTilesData(final GameMap map) {
		JsonObject tiles = new JsonObject();
		tiles.addProperty(MapJsonKeys.KEY_WIDTH, LEVEL_SIZE);
		tiles.addProperty(MapJsonKeys.KEY_DEPTH, LEVEL_SIZE);
		StringBuilder builder = new StringBuilder();
		IntStream.range(0, LEVEL_SIZE).forEach(row ->
				IntStream.range(0, LEVEL_SIZE).forEach(col -> {
					MapNode mapNode = map.getTiles()[row][col];
					if (mapNode != null && mapNode.getTextureDefinition() != null) {
						builder.append(mapNode.getTextureDefinition().ordinal() + 1);
					} else {
						builder.append(0);
					}
				})
		);
		tiles.addProperty(MapJsonKeys.KEY_MATRIX, builder.toString());
		return tiles;
	}
}
