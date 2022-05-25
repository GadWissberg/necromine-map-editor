package com.gadarts.necromine.editor.desktop.dialogs;

import com.gadarts.necromine.assets.Assets.SurfaceTextures;
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

import static com.gadarts.necromine.assets.Assets.SurfaceTextures.MISSING;
import static com.gadarts.necromine.editor.desktop.dialogs.WallSpinners.DEF_H_OFFSET;
import static com.gadarts.necromine.editor.desktop.dialogs.WallSpinners.DEF_V_OFFSET;
import static com.gadarts.necromine.editor.desktop.dialogs.WallSpinners.DEF_V_SCALE;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createTitledBorder;

public class WallTilingDialog extends DialogPane {
	public static final int SECTION_PADDING = 10;
	public static final int STEP_SIZE_VERTICAL_SCALE = 1;
	public static final float STEP_SIZE_OFFSET = 0.1F;
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
	private WallSpinners eastWallSpinners;
	private JSpinner southWallVScale;
	private JSpinner westWallVScale;
	private JSpinner northWallVScale;
	private JSpinner southWallHorOffset;
	private JSpinner southWallVerOffset;
	private JSpinner westWallHorOffset;
	private JSpinner westWallVerOffset;
	private JSpinner northWallHorOffset;
	private JSpinner northWallVerOffset;

	public WallTilingDialog(File assetsFolderLocation, MapRenderer mapRenderer, FlatNode src, FlatNode dst) {
		this.assetsFolderLocation = assetsFolderLocation;
		this.mapRenderer = mapRenderer;
		this.src = src;
		this.dst = dst;
		init();
	}

	@Override
	void initializeView() {
		try {
			List<MapNodeData> nodes = mapRenderer.getRegion(src, dst);
			JPanel sectionsPanel = new JPanel();
			sectionsPanel.setLayout(new GridLayout(2, 2));
			addEastSection(sectionsPanel, decideEastInitialValues(nodes));
//			addNorthSection(sectionsPanel, decideNorthInitialValues(nodes));
//			addSouthSection(sectionsPanel, decideSouthIcon(nodes));
//			addWestSection(sectionsPanel, decideWestIcon(nodes));
			add(sectionsPanel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		addGeneralButtons(e -> {
			mapRenderer.onNodeWallsDefined(new NodeWallsDefinitions(createWallDef(eastImageButton, eastWallSpinners), createWallDef(southImageButton, eastWallSpinners), createWallDef(westImageButton, eastWallSpinners), createWallDef(northImageButton, eastWallSpinners)), src, dst);
			closeDialog();
		});
	}

	private WallDefinition decideEastInitialValues(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream().filter(n -> n.getWalls().getEastWall() != null).map(mapNodeData -> mapNodeData.getWalls().getEastWall()).findFirst();
		WallDefinition initialValues = new WallDefinition(MISSING, DEF_V_SCALE, DEF_H_OFFSET, DEF_V_OFFSET);

		if (def.isPresent()) {
			Wall wall = def.get();
			SurfaceTextures textureDefinition = wall.getDefinition();

			boolean allSameTexture = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> d.getWalls().getEastWall().getDefinition() == textureDefinition);
			initialValues.setTexture(allSameTexture ? textureDefinition : MISSING);

			boolean allSameVerScale = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> d.getWalls().getEastWall().getVScale() == wall.getVScale());
			initialValues.setVScale(allSameVerScale ? wall.getVScale() : DEF_V_SCALE);

			boolean allSameHorOffset = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> d.getWalls().getEastWall().getHOffset() == wall.getHOffset());
			initialValues.setHorizontalOffset(allSameHorOffset ? wall.getHOffset() : DEF_H_OFFSET);

			boolean allSameVerOffset = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> d.getWalls().getEastWall().getVOffset() == wall.getVOffset());
			initialValues.setVerticalOffset(allSameVerOffset ? wall.getVOffset() : DEF_V_OFFSET);
		}

		return initialValues;
	}

	private WallDefinition decideNorthInitialValues(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream().filter(n -> n.getWalls().getNorthWall() != null).map(mapNodeData -> mapNodeData.getWalls().getNorthWall()).findFirst();
		WallDefinition initialValues = new WallDefinition(MISSING, DEF_V_SCALE, DEF_H_OFFSET, DEF_V_OFFSET);

		if (def.isPresent()) {
			SurfaceTextures definition = def.get().getDefinition();

			boolean allSameTexture = nodes.stream().filter(n -> n.getWalls().getNorthWall() != null).allMatch(d -> d.getWalls().getNorthWall().getDefinition() == definition);
			initialValues.setTexture(allSameTexture ? definition : MISSING);

			boolean allSameVerScale = nodes.stream().filter(n -> n.getWalls().getNorthWall() != null).allMatch(d -> d.getWalls().getNorthWall().getDefinition() == definition);
			initialValues.setVScale(allSameVerScale ? def.get().getVScale() : null);

			boolean allSameHorOffset = nodes.stream().filter(n -> n.getWalls().getNorthWall() != null).allMatch(d -> d.getWalls().getNorthWall().getDefinition() == definition);
			initialValues.setHorizontalOffset(allSameHorOffset ? def.get().getHOffset() : null);

			boolean allSameVerOffset = nodes.stream().filter(n -> n.getWalls().getNorthWall() != null).allMatch(d -> d.getWalls().getNorthWall().getDefinition() == definition);
			initialValues.setVerticalOffset(allSameVerOffset ? def.get().getVOffset() : null);
		}

		return initialValues;
	}


	private SurfaceTextures decideWestIcon(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream().filter(n -> n.getWalls().getWestWall() != null).map(mapNodeData -> mapNodeData.getWalls().getWestWall()).findFirst();
		SurfaceTextures texture = MISSING;
		if (def.isPresent()) {
			boolean allSame = nodes.stream().filter(n -> n.getWalls().getWestWall() != null).allMatch(data -> data.getWalls().getWestWall().getDefinition() == def.get().getDefinition());
			texture = allSame ? def.get().getDefinition() : MISSING;
		}
		return texture;
	}

	private SurfaceTextures decideNorthIcon(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream().filter(n -> n.getWalls().getNorthWall() != null).map(mapNodeData -> mapNodeData.getWalls().getNorthWall()).findFirst();
		SurfaceTextures texture = MISSING;
		if (def.isPresent()) {
			boolean allSame = nodes.stream().filter(n -> n.getWalls().getNorthWall() != null).allMatch(data -> data.getWalls().getNorthWall().getDefinition() == def.get().getDefinition());
			texture = allSame ? def.get().getDefinition() : MISSING;
		}
		return texture;
	}

	private SurfaceTextures decideSouthIcon(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream().filter(n -> n.getWalls().getSouthWall() != null).map(mapNodeData -> mapNodeData.getWalls().getSouthWall()).findFirst();
		SurfaceTextures texture = MISSING;
		if (def.isPresent()) {
			boolean allSame = nodes.stream().filter(n -> n.getWalls().getSouthWall() != null).allMatch(data -> data.getWalls().getSouthWall().getDefinition() == def.get().getDefinition());
			texture = allSame ? def.get().getDefinition() : MISSING;
		}
		return texture;
	}

	private void addEastSection(JPanel sectionsPanel, WallDefinition initialValues) throws IOException {
		eastImageButton = createImageButton(initialValues.getTexture());
		JPanel spinnersPanel = createSection(sectionsPanel, LABEL_EAST, eastImageButton);
		eastWallSpinners = new WallSpinners(spinnersPanel, initialValues);
	}

//	private void addNorthSection(JPanel sectionsPanel, WallDefinition initialValues) throws IOException {
//		northImageButton = createImageButton(initialValues.getTexture());
//		JPanel sec = createSection(sectionsPanel, LABEL_NORTH, northImageButton);
//		Float vScale = initialValues.getVScale();
//		Float hor = initialValues.getHorizontalOffset();
//		Float ver = initialValues.getVerticalOffset();
//		northWallVScale = addSpinnerLine(sec, LABEL_VER_SCALE, STEP_SIZE_V_SCALE, (vScale != null ? vScale : DEF_V_SCALE));
//		northWallHorOffset = addSpinnerLine(sec, LABEL_HOR_OFFSET, STEP_SIZE_H_OFFSET, (hor != null ? hor : DEF_H_OFFSET));
//		northWallVerOffset = addSpinnerLine(sec, LABEL_VER_OFFSET, STEP_SIZE_V_OFFSET, (ver != null ? ver : DEF_V_OFFSET));
//	}


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
		CompoundBorder compoundBorder = createCompoundBorder(pad, createCompoundBorder(createTitledBorder(header), pad));
		section.setBorder(compoundBorder);
	}


	private WallDefinition createWallDef(GalleryButton imageButton, WallSpinners wallSpinners) {
		SurfaceTextures def = imageButton.getTextureDefinition();
		boolean isDefined = def != MISSING;
		return new WallDefinition(isDefined ? def : null, isDefined ? ((Double) wallSpinners.getWallVScale().getModel().getValue()).floatValue() : null, isDefined ? ((Double) wallSpinners.getWallHorOffset().getModel().getValue()).floatValue() : null, isDefined ? ((Double) wallSpinners.getWallVerOffset().getModel().getValue()).floatValue() : null);
	}

	public String getDialogTitle() {
		return "Tile Walls";
	}

	private GalleryButton createImageButton(SurfaceTextures texture) throws IOException {
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
