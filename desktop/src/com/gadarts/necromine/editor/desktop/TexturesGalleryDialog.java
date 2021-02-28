package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.assets.Assets;

import java.io.File;

public class TexturesGalleryDialog extends DialogPane {
	public TexturesGalleryDialog(final File assetsFolderLocation, final OnTextureSelected onTextureSelected) {
		add(GuiUtils.createEntitiesGallery(assetsFolderLocation, itemEvent -> {
			Assets.FloorsTextures texture = ((GalleryButton) itemEvent.getItem()).getTextureDefinition();
			onTextureSelected.run(texture);
			closeDialog();
		}));
	}



	@Override
	String getDialogTitle() {
		return "Select Texture";
	}
}
