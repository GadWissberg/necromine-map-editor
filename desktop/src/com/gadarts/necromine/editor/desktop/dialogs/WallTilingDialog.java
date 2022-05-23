package com.gadarts.necromine.editor.desktop.dialogs;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.model.node.NodeWallsDefinitions;
import com.necromine.editor.model.node.WallDefinition;

import javax.swing.*;
import javax.swing.border.Border;
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
	private final MapRenderer mapRenderer;
	private final FlatNode src;
	private final FlatNode dst;
	private GalleryButton eastImageButton;
	private GalleryButton southImageButton;
	private GalleryButton westImageButton;
	private GalleryButton northImageButton;
	private JSpinner eastWallVScale;
	private JSpinner southWallVScale;
	private JSpinner westWallVScale;
	private JSpinner northWallVScale;
	private JSpinner eastWallHorizontalOffset;
	private JSpinner eastWallVerticalOffset;
	private JSpinner southWallHorizontalOffset;
	private JSpinner southWallVerticalOffset;
	private JSpinner westWallHorizontalOffset;
	private JSpinner westWallVerticalOffset;
	private JSpinner northWallHorizontalOffset;
	private JSpinner northWallVerticalOffset;

	public WallTilingDialog(final File assetsFolderLocation, final MapRenderer mapRenderer, final FlatNode src, final FlatNode dst) {
		this.assetsFolderLocation = assetsFolderLocation;
		this.mapRenderer = mapRenderer;
		this.src = src;
		this.dst = dst;
		init();
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		try {
			c.gridx++;
			JPanel eastSection = new JPanel(new GridBagLayout());
			Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
			eastSection.setBorder(BorderFactory.createCompoundBorder(padding, BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(LABEL_EAST), padding)));
			eastSection.add(createImageButton());
			add(eastSection);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


//		addLabels(c);
//		addImageButtons(assetsFolderLocation, c);
//		addVScaleSelectors(c);
//		addHorizontalTextureOffsetSelectors(c);
//		addVerticalTextureOffsetSelectors(c);
		addOkButton(c, e -> {
			mapRenderer.onNodeWallsDefined(new NodeWallsDefinitions(createWallDefinition(eastImageButton, eastWallVScale, eastWallHorizontalOffset, eastWallVerticalOffset), createWallDefinition(southImageButton, southWallVScale, southWallHorizontalOffset, southWallVerticalOffset), createWallDefinition(westImageButton, westWallVScale, westWallHorizontalOffset, westWallVerticalOffset), createWallDefinition(northImageButton, northWallVScale, northWallHorizontalOffset, northWallVerticalOffset)), src, dst);
			closeDialog();
		});
	}

	private void addHorizontalTextureOffsetSelectors(final GridBagConstraints c) {
		c.gridx = 3;
		c.gridy = 0;
		eastWallHorizontalOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		southWallHorizontalOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		westWallHorizontalOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		northWallHorizontalOffset = addSpinner(0, 1, 0.1F, c);
	}

	private void addVerticalTextureOffsetSelectors(final GridBagConstraints c) {
		c.gridx = 4;
		c.gridy = 0;
		eastWallVerticalOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		southWallVerticalOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		westWallVerticalOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		northWallVerticalOffset = addSpinner(0, 1, 0.1F, c);
	}

	private WallDefinition createWallDefinition(final GalleryButton imageButton, final JSpinner vScaleSpinner, final JSpinner horizontalOffsetSpinner, final JSpinner verticalOffsetSpinner) {
		Assets.SurfaceTextures def = imageButton.getTextureDefinition();
		float vScale = ((Double) vScaleSpinner.getModel().getValue()).floatValue();
		float horizontalOffsetValue = ((Double) horizontalOffsetSpinner.getModel().getValue()).floatValue();
		float verticalOffsetValue = ((Double) verticalOffsetSpinner.getModel().getValue()).floatValue();
		boolean isDefined = def != Assets.SurfaceTextures.MISSING;
		return new WallDefinition(isDefined ? def : null, isDefined ? vScale : null, isDefined ? horizontalOffsetValue : null, isDefined ? verticalOffsetValue : null);
	}

	private void addVScaleSelectors(final GridBagConstraints c) {
		c.gridx = 2;
		c.gridy = 0;
		eastWallVScale = addSpinner(0, 10, 1, c);
		c.gridy++;
		southWallVScale = addSpinner(0, 10, 1, c);
		c.gridy++;
		westWallVScale = addSpinner(0, 10, 1, c);
		c.gridy++;
		northWallVScale = addSpinner(0, 10, 1, c);
	}

	public String getDialogTitle() {
		return "Tile Walls";
	}

	private void addImageButtons(final File assetsFolderLocation, final GridBagConstraints c) {
		c.gridy = 0;
		eastImageButton = addImageButton(c);
		southImageButton = addImageButton(c);
		westImageButton = addImageButton(c);
		northImageButton = addImageButton(c);
	}

	private GalleryButton addImageButton(final GridBagConstraints c) {
		GalleryButton button = null;
		try {
			button = createImageButton();
			c.gridx = 1;
			add(button, c);
			c.gridy += 1;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return button;
	}

	private GalleryButton createImageButton() throws IOException {
		GalleryButton button = GuiUtils.createTextureImageButton(assetsFolderLocation, Assets.SurfaceTextures.MISSING);
		button.addItemListener(itemEvent -> {
			if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
				GuiUtils.openNewDialog(getParent(), new TexturesGalleryDialog(assetsFolderLocation, image -> {
					try {
						button.applyTexture(image, GuiUtils.loadImage(assetsFolderLocation, image));
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}));
			}
		});
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
