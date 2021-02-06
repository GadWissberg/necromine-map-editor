package com.necromine.editor;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.model.PlacedElement;

public interface PlacedElementCreation {
	PlacedElement create(final ElementDefinition definition,
						 final Node node,
						 final Direction direction,
						 final GameAssetsManager assetsManager);
}
