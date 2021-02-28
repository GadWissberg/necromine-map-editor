package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.assets.Assets;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.node.NodeWallsDefinitions;

import javax.swing.*;
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
	private static final int PADDING = 10;
	private static final String BUTTON_LABEL_OK = "OK";
	private final NodeWallsDefinitions definitions;
	private GalleryButton eastImageButton;
	private GalleryButton southImageButton;
	private GalleryButton westImageButton;
	private GalleryButton northImageButton;

	public WallTilingDialog(final File assetsFolderLocation,
							final GuiEventsSubscriber guiEventsSubscriber,
							final Node node,
							final NodeWallsDefinitions definitions) {
		this.definitions = definitions;
		setLayout(new GridBagLayout());
		initializeView(assetsFolderLocation, guiEventsSubscriber, node);
	}

	private void initializeView(final File assetsFolderLocation,
								final GuiEventsSubscriber guiEventsSubscriber,
								final Node node) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		addLabels(c);
		addImageButtons(assetsFolderLocation, c);
		addOkButton(c, guiEventsSubscriber, node.getRow(), node.getCol());
	}

	private void addOkButton(final GridBagConstraints c,
							 final GuiEventsSubscriber guiEventsSubscriber,
							 final int row,
							 final int col) {
		c.gridwidth = 2;
		Button ok = new Button(BUTTON_LABEL_OK);
		ok.addActionListener(e -> {
			guiEventsSubscriber.onNodeWallsDefined(
					new NodeWallsDefinitions(
							eastImageButton.getTextureDefinition(),
							southImageButton.getTextureDefinition(),
							westImageButton.getTextureDefinition(),
							northImageButton.getTextureDefinition()),
					row, col);
			closeDialog();
		});
		add(ok, c);
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

	private void addLabel(final GridBagConstraints c, final String label) {
		add(new JLabel(label), c);
		c.gridy += 1;
	}
}
