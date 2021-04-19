package com.gadarts.necromine.editor.desktop.dialogs;

import com.gadarts.necromine.assets.Assets.FloorsTextures;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.model.node.NodeWallsDefinitions;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

public class WallTilingDialog extends DialogPane {
	private static final String LABEL_EAST = "East Wall:";
	private static final String LABEL_SOUTH = "South Wall:";
	private static final String LABEL_WEST = "West Wall:";
	private static final String LABEL_NORTH = "North Wall:";
	private final File assetsFolderLocation;
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final FlatNode src;
	private final FlatNode dst;
	private GalleryButton eastImageButton;
	private GalleryButton southImageButton;
	private GalleryButton westImageButton;
	private GalleryButton northImageButton;

	public WallTilingDialog(final File assetsFolderLocation,
							final GuiEventsSubscriber guiEventsSubscriber,
							final FlatNode src,
							final FlatNode dst) {
		this.assetsFolderLocation = assetsFolderLocation;
		this.guiEventsSubscriber = guiEventsSubscriber;
		this.src = src;
		this.dst = dst;
		init();
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		addLabels(c);
		addImageButtons(assetsFolderLocation, c);
		addOkButton(c, e -> {
			guiEventsSubscriber.onNodeWallsDefined(
					new NodeWallsDefinitions(
							eastImageButton.getTextureDefinition() != FloorsTextures.FLOOR_PAVEMENT_0 ? eastImageButton.getTextureDefinition() : null,
							southImageButton.getTextureDefinition() != FloorsTextures.FLOOR_PAVEMENT_0 ? southImageButton.getTextureDefinition() : null,
							westImageButton.getTextureDefinition() != FloorsTextures.FLOOR_PAVEMENT_0 ? westImageButton.getTextureDefinition() : null,
							northImageButton.getTextureDefinition() != FloorsTextures.FLOOR_PAVEMENT_0 ? northImageButton.getTextureDefinition() : null),
					src, dst);
			closeDialog();
		});
	}

	public String getDialogTitle() {
		return "Tile Walls";
	}

	private void addImageButtons(final File assetsFolderLocation,
								 final GridBagConstraints c) {
		c.gridy = 0;
		eastImageButton = addImageButton(assetsFolderLocation, c);
		southImageButton = addImageButton(assetsFolderLocation, c);
		westImageButton = addImageButton(assetsFolderLocation, c);
		northImageButton = addImageButton(assetsFolderLocation, c);
	}

	private GalleryButton addImageButton(final File assetsLocation,
										 final GridBagConstraints c) {
		GalleryButton button = null;
		try {
			button = GuiUtils.createTextureImageButton(
					assetsLocation,
					FloorsTextures.FLOOR_PAVEMENT_0);
			GalleryButton finalButton = button;
			button.addItemListener(itemEvent -> {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					GuiUtils.openNewDialog(getParent(), new TexturesGalleryDialog(assetsLocation, image -> {
						try {
							finalButton.applyTexture(image, GuiUtils.loadImage(assetsLocation, image));
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}));
				}
			});
			c.gridx = 1;
			add(button, c);
			c.gridy += 1;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return button;
	}

	private void addLabels(final GridBagConstraints c) {
		addLabel(c, LABEL_EAST);
		c.gridy++;
		addLabel(c, LABEL_SOUTH);
		c.gridy++;
		addLabel(c, LABEL_WEST);
		c.gridy++;
		addLabel(c, LABEL_NORTH);
		c.gridy++;
	}

}
