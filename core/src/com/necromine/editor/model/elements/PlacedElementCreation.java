package com.necromine.editor.model.elements;

import com.gadarts.necromine.assets.GameAssetsManager;

public interface PlacedElementCreation {
	PlacedElement create(final PlacedElement.PlacedElementParameters parameters, final GameAssetsManager assetsManager);
}
