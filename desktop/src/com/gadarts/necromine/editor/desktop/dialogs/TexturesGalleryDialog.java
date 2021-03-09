package com.gadarts.necromine.editor.desktop.dialogs;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.editor.desktop.OnTextureSelected;

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
	public String getDialogTitle() {
		return "Select Texture";
	}
}
