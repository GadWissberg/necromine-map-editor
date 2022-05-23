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
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

import static javax.swing.BorderFactory.*;

public class WallTilingDialog extends DialogPane {
	private static final String LABEL_EAST = "East Wall:";
	private static final String LABEL_SOUTH = "South Wall:";
	private static final String LABEL_WEST = "West Wall:";
	private static final String LABEL_NORTH = "North Wall:";
	public static final String LABEL_VERTICAL_SCALE = "V. scale: ";
	public static final String LABEL_HORIZONTAL_OFFSET = "H. offset: ";
	public static final String LABEL_VERTICAL_OFFSET = "V. offset: ";
	public static final int SECTION_PADDING = 10;
	public static final int STEP_SIZE_VERTICAL_SCALE = 1;
	public static final float STEP_SIZE_OFFSET = 0.1F;
	public static final int SPINNER_VALUE_MAX = 10;
	public static final int SPINNER_VALUE_MIN = -10;
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
	private JSpinner eastWallHorOffset;
	private JSpinner eastWallVerOffset;
	private JSpinner southWallHorOffset;
	private JSpinner southWallVerOffset;
	private JSpinner westWallHorOffset;
	private JSpinner westWallVerOffset;
	private JSpinner northWallHorOffset;
	private JSpinner northWallVeOffset;

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
			c.gridx = 0;
			c.gridy = 0;
			JPanel sectionsPanel = new JPanel();
			sectionsPanel.setLayout(new GridLayout(2, 2));
			addEastSection(sectionsPanel);
			addNorthSection(sectionsPanel);
			addSouthSection(sectionsPanel);
			addWestSection(sectionsPanel);
			add(sectionsPanel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		addOkButton(c, e -> {
			mapRenderer.onNodeWallsDefined(new NodeWallsDefinitions(
							createWallDef(eastImageButton, eastWallVScale, eastWallHorOffset, eastWallVerOffset),
							createWallDef(southImageButton, southWallVScale, southWallHorOffset, southWallVerOffset),
							createWallDef(westImageButton, westWallVScale, westWallHorOffset, westWallVerOffset),
							createWallDef(northImageButton, northWallVScale, northWallHorOffset, northWallVeOffset)),
					src, dst);
			closeDialog();
		});
	}

	private void addEastSection(JPanel sectionsPanel) throws IOException {
		JPanel eastSection = createSection(sectionsPanel, LABEL_EAST);
		eastWallVScale = addSpinnerLine(eastSection, LABEL_VERTICAL_SCALE, 1);
		eastWallHorOffset = addSpinnerLine(eastSection, LABEL_HORIZONTAL_OFFSET, 0.1F);
		eastWallVerOffset = addSpinnerLine(eastSection, LABEL_VERTICAL_OFFSET, 0.1F);
	}

	private void addNorthSection(JPanel sectionsPanel) throws IOException {
		JPanel northSection = createSection(sectionsPanel, LABEL_NORTH);
		northWallVScale = addSpinnerLine(northSection, LABEL_VERTICAL_SCALE, STEP_SIZE_VERTICAL_SCALE);
		northWallHorOffset = addSpinnerLine(northSection, LABEL_HORIZONTAL_OFFSET, STEP_SIZE_OFFSET);
		northWallVeOffset = addSpinnerLine(northSection, LABEL_VERTICAL_OFFSET, STEP_SIZE_OFFSET);
	}

	private void addSouthSection(JPanel sectionsPanel) throws IOException {
		JPanel southSection = createSection(sectionsPanel, LABEL_SOUTH);
		southWallVScale = addSpinnerLine(southSection, LABEL_VERTICAL_SCALE, STEP_SIZE_VERTICAL_SCALE);
		southWallHorOffset = addSpinnerLine(southSection, LABEL_HORIZONTAL_OFFSET, STEP_SIZE_OFFSET);
		southWallVerOffset = addSpinnerLine(southSection, LABEL_VERTICAL_OFFSET, STEP_SIZE_OFFSET);
	}

	private void addWestSection(JPanel sectionsPanel) throws IOException {
		JPanel westSection = createSection(sectionsPanel, LABEL_WEST);
		westWallVScale = addSpinnerLine(westSection, LABEL_VERTICAL_SCALE, STEP_SIZE_VERTICAL_SCALE);
		westWallHorOffset = addSpinnerLine(westSection, LABEL_HORIZONTAL_OFFSET, STEP_SIZE_OFFSET);
		westWallVerOffset = addSpinnerLine(westSection, LABEL_VERTICAL_OFFSET, STEP_SIZE_OFFSET);
	}

	private JPanel createSection(JPanel sectionsPanel, String header) throws IOException {
		JPanel section = new JPanel(new GridBagLayout());
		addSectionBorder(header, section);
		section.add(createImageButton());
		JPanel spinnersPanel = addSpinnersPanel(section);
		sectionsPanel.add(section);
		return spinnersPanel;
	}

	private JPanel addSpinnersPanel(JPanel section) {
		JPanel spinnersPanel = new JPanel();
		spinnersPanel.setBorder(createEmptyBorder(SECTION_PADDING, SECTION_PADDING, SECTION_PADDING, SECTION_PADDING));
		spinnersPanel.setLayout(new GridLayout(3, 2));
		section.add(spinnersPanel);
		return spinnersPanel;
	}

	private void addSectionBorder(String header, JPanel section) {
		Border pad = createEmptyBorder(SECTION_PADDING, SECTION_PADDING, SECTION_PADDING, SECTION_PADDING);
		CompoundBorder compoundBorder = createCompoundBorder(
				pad,
				createCompoundBorder(createTitledBorder(header), pad));
		section.setBorder(compoundBorder);
	}

	private JSpinner addSpinnerLine(JPanel spinnersPanel, String label, float step) {
		spinnersPanel.add(new JLabel(label));
		JSpinner spinner = createSpinner(0, SPINNER_VALUE_MAX, step, SPINNER_VALUE_MIN);
		JPanel paddingPanel = new JPanel();
		paddingPanel.add(spinner);
		spinnersPanel.add(paddingPanel);
		return spinner;
	}


	private void addHorizontalTextureOffsetSelectors(final GridBagConstraints c) {
		c.gridx = 3;
		c.gridy = 0;
		eastWallHorOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		southWallHorOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		westWallHorOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		northWallHorOffset = addSpinner(0, 1, 0.1F, c);
	}

	private void addVerticalTextureOffsetSelectors(final GridBagConstraints c) {
		c.gridx = 4;
		c.gridy = 0;
		eastWallVerOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		southWallVerOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		westWallVerOffset = addSpinner(0, 1, 0.1F, c);
		c.gridy++;
		northWallVeOffset = addSpinner(0, 1, 0.1F, c);
	}

	private WallDefinition createWallDef(final GalleryButton imageButton, final JSpinner vScaleSpinner, final JSpinner horizontalOffsetSpinner, final JSpinner verticalOffsetSpinner) {
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

	public String getDialogTitle( ) {
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

	private GalleryButton createImageButton( ) throws IOException {
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
