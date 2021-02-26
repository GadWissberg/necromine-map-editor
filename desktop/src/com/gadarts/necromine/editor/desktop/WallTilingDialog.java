package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.assets.Assets;
import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

public class WallTilingDialog extends JPanel {
	private static final String LABEL_EAST = "East Wall:";
	private static final String LABEL_SOUTH = "South Wall:";
	private static final String LABEL_WEST = "West Wall:";
	private static final String LABEL_NORTH = "North Wall:";
	private static final int PADDING = 10;

	public String getDialogTitle() {
		return "Tile Walls";
	}

	public WallTilingDialog(final File assetsFolderLocation, final GuiEventsSubscriber guiEventsSubscriber) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		addLabels(c);
		addImageButtons(assetsFolderLocation, guiEventsSubscriber, c);
	}

	private void addImageButtons(final File assetsFolderLocation,
								 final GuiEventsSubscriber guiEventsSubscriber,
								 final GridBagConstraints c) {
		c.gridy = 0;
		addImageButton(assetsFolderLocation, guiEventsSubscriber, c);
		addImageButton(assetsFolderLocation, guiEventsSubscriber, c);
		addImageButton(assetsFolderLocation, guiEventsSubscriber, c);
		addImageButton(assetsFolderLocation, guiEventsSubscriber, c);
	}

	private void addImageButton(final File assetsFolderLocation,
								final GuiEventsSubscriber guiEventsSubscriber,
								final GridBagConstraints c) {
		try {
			Assets.FloorsTextures texture = Assets.FloorsTextures.FLOOR_0;
			GalleryButton button = GuiUtils.createTextureImageButton(assetsFolderLocation, texture, itemEvent -> {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					guiEventsSubscriber.onTileSelected(texture);
				}
			});
			c.gridx = 1;
			add(button, c);
			c.gridy += 1;
		} catch (final IOException e) {
			e.printStackTrace();
		}
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
