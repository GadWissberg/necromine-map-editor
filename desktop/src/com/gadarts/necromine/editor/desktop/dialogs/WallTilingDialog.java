package com.gadarts.necromine.editor.desktop.dialogs;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.node.NodeWallsDefinitions;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class WallTilingDialog extends DialogPane {
	private static final String LABEL_EAST = "East Wall:";
	private static final String LABEL_SOUTH = "South Wall:";
	private static final String LABEL_WEST = "West Wall:";
	private static final String LABEL_NORTH = "North Wall:";
	private final NodeWallsDefinitions definitions;
	private final File assetsFolderLocation;
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final Node node;
	private GalleryButton eastImageButton;
	private GalleryButton southImageButton;
	private GalleryButton westImageButton;
	private GalleryButton northImageButton;

	public WallTilingDialog(final File assetsFolderLocation,
							final GuiEventsSubscriber guiEventsSubscriber,
							final Node node,
							final NodeWallsDefinitions definitions) {
		this.definitions = definitions;
		this.assetsFolderLocation = assetsFolderLocation;
		this.guiEventsSubscriber = guiEventsSubscriber;
		this.node = node;
		init();
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		addLabels(c);
		addImageButtons(assetsFolderLocation, c);
		addOkButton(c, e -> {
			guiEventsSubscriber.onNodeWallsDefined(
					new NodeWallsDefinitions(
							eastImageButton.getTextureDefinition(),
							southImageButton.getTextureDefinition(),
							westImageButton.getTextureDefinition(),
							northImageButton.getTextureDefinition()),
					node.getRow(), node.getCol());
			closeDialog();
		});
	}

	public String getDialogTitle() {
		return "Tile Walls";
	}

	private void addImageButtons(final File assetsFolderLocation,
								 final GridBagConstraints c) {
		c.gridy = 0;
		eastImageButton = addImageButton(assetsFolderLocation, c, definitions.getEast());
		southImageButton = addImageButton(assetsFolderLocation, c, definitions.getSouth());
		westImageButton = addImageButton(assetsFolderLocation, c, definitions.getWest());
		northImageButton = addImageButton(assetsFolderLocation, c, definitions.getNorth());
	}

	private GalleryButton addImageButton(final File assetsLocation,
										 final GridBagConstraints c,
										 final Assets.FloorsTextures texture) {
		GalleryButton button = null;
		try {
			button = GuiUtils.createTextureImageButton(
					assetsLocation,
					Optional.ofNullable(texture).orElse(Assets.FloorsTextures.FLOOR_0));
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
		addLabel(c, LABEL_SOUTH);
		addLabel(c, LABEL_WEST);
		addLabel(c, LABEL_NORTH);
	}

}
