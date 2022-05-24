package com.gadarts.necromine.editor.desktop.dialogs;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.model.map.MapNodeData;
import com.gadarts.necromine.model.map.Wall;
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
import java.util.List;
import java.util.Optional;

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

	public WallTilingDialog(File assetsFolderLocation,
							MapRenderer mapRenderer,
							FlatNode src,
							FlatNode dst) {
		this.assetsFolderLocation = assetsFolderLocation;
		this.mapRenderer = mapRenderer;
		this.src = src;
		this.dst = dst;
		init();
	}

	@Override
	void initializeView( ) {
		try {
			List<MapNodeData> nodes = mapRenderer.getRegion(src, dst);
			JPanel sectionsPanel = new JPanel();
			sectionsPanel.setLayout(new GridLayout(2, 2));
			addEastSection(sectionsPanel, decideEastIcon(nodes));
			addNorthSection(sectionsPanel);
			addSouthSection(sectionsPanel);
			addWestSection(sectionsPanel);
			add(sectionsPanel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		addGeneralButtons(e -> {
			mapRenderer.onNodeWallsDefined(new NodeWallsDefinitions(
							createWallDef(eastImageButton, eastWallVScale, eastWallHorOffset, eastWallVerOffset),
							createWallDef(southImageButton, southWallVScale, southWallHorOffset, southWallVerOffset),
							createWallDef(westImageButton, westWallVScale, westWallHorOffset, westWallVerOffset),
							createWallDef(northImageButton, northWallVScale, northWallHorOffset, northWallVeOffset)),
					src, dst);
			closeDialog();
		});
	}

	private Assets.SurfaceTextures decideEastIcon(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream()
				.filter(n -> n.getWalls().getEastWall() != null)
				.map(mapNodeData -> mapNodeData.getWalls().getEastWall())
				.findFirst();
		Assets.SurfaceTextures texture = Assets.SurfaceTextures.MISSING;
		if (def.isPresent()) {
			boolean allSame = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(data -> data.getWalls().getEastWall().getDefinition() == def.get().getDefinition());
			texture = allSame ? def.get().getDefinition() : Assets.SurfaceTextures.MISSING;
		}
		return texture;
	}

	private void addEastSection(JPanel sectionsPanel, Assets.SurfaceTextures texture) throws IOException {
		eastImageButton = createImageButton(texture);
		JPanel eastSection = createSection(sectionsPanel, LABEL_EAST, eastImageButton);
		eastWallVScale = addSpinnerLine(eastSection, LABEL_VERTICAL_SCALE, 1);
		eastWallHorOffset = addSpinnerLine(eastSection, LABEL_HORIZONTAL_OFFSET, 0.1F);
		eastWallVerOffset = addSpinnerLine(eastSection, LABEL_VERTICAL_OFFSET, 0.1F);
	}

	private void addNorthSection(JPanel sectionsPanel) throws IOException {
		northImageButton = createImageButton(Assets.SurfaceTextures.MISSING);
		JPanel northSection = createSection(sectionsPanel, LABEL_NORTH, northImageButton);
		northWallVScale = addSpinnerLine(northSection, LABEL_VERTICAL_SCALE, STEP_SIZE_VERTICAL_SCALE);
		northWallHorOffset = addSpinnerLine(northSection, LABEL_HORIZONTAL_OFFSET, STEP_SIZE_OFFSET);
		northWallVeOffset = addSpinnerLine(northSection, LABEL_VERTICAL_OFFSET, STEP_SIZE_OFFSET);
	}

	private void addSouthSection(JPanel sectionsPanel) throws IOException {
		southImageButton = createImageButton(Assets.SurfaceTextures.MISSING);
		JPanel southSection = createSection(sectionsPanel, LABEL_SOUTH, southImageButton);
		southWallVScale = addSpinnerLine(southSection, LABEL_VERTICAL_SCALE, STEP_SIZE_VERTICAL_SCALE);
		southWallHorOffset = addSpinnerLine(southSection, LABEL_HORIZONTAL_OFFSET, STEP_SIZE_OFFSET);
		southWallVerOffset = addSpinnerLine(southSection, LABEL_VERTICAL_OFFSET, STEP_SIZE_OFFSET);
	}

	private void addWestSection(JPanel sectionsPanel) throws IOException {
		westImageButton = createImageButton(Assets.SurfaceTextures.MISSING);
		JPanel westSection = createSection(sectionsPanel, LABEL_WEST, westImageButton);
		westWallVScale = addSpinnerLine(westSection, LABEL_VERTICAL_SCALE, STEP_SIZE_VERTICAL_SCALE);
		westWallHorOffset = addSpinnerLine(westSection, LABEL_HORIZONTAL_OFFSET, STEP_SIZE_OFFSET);
		westWallVerOffset = addSpinnerLine(westSection, LABEL_VERTICAL_OFFSET, STEP_SIZE_OFFSET);
	}

	private JPanel createSection(JPanel sectionsPanel, String header, GalleryButton imageButton) {
		JPanel section = new JPanel(new GridBagLayout());
		addSectionBorder(header, section);
		section.add(imageButton);
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

	private WallDefinition createWallDef(final GalleryButton imageButton, final JSpinner vScaleSpinner, final JSpinner horizontalOffsetSpinner, final JSpinner verticalOffsetSpinner) {
		Assets.SurfaceTextures def = imageButton.getTextureDefinition();
		float vScale = ((Double) vScaleSpinner.getModel().getValue()).floatValue();
		float horizontalOffsetValue = ((Double) horizontalOffsetSpinner.getModel().getValue()).floatValue();
		float verticalOffsetValue = ((Double) verticalOffsetSpinner.getModel().getValue()).floatValue();
		boolean isDefined = def != Assets.SurfaceTextures.MISSING;
		return new WallDefinition(isDefined ? def : null, isDefined ? vScale : null, isDefined ? horizontalOffsetValue : null, isDefined ? verticalOffsetValue : null);
	}

	public String getDialogTitle( ) {
		return "Tile Walls";
	}

	private GalleryButton createImageButton(Assets.SurfaceTextures texture) throws IOException {
		GalleryButton button = GuiUtils.createTextureImageButton(assetsFolderLocation, texture);
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

}
