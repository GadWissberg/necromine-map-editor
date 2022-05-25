package com.gadarts.necromine.editor.desktop.gui.managers;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.editor.desktop.gui.EditorCardLayout;
import com.gadarts.necromine.editor.desktop.gui.TreeSection;
import com.gadarts.necromine.editor.desktop.tree.EditorTree;
import com.gadarts.necromine.editor.desktop.tree.ResourcesTreeCellRenderer;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.enemies.Enemies;
import com.gadarts.necromine.model.characters.player.PlayerDefinition;
import com.gadarts.necromine.model.env.EnvironmentDefinitions;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.gadarts.necromine.model.pickups.WeaponsDefinitions;
import com.necromine.editor.EntriesDisplayTypes;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.necromine.editor.EntriesDisplayTypes.NONE;


public class EntitiesSelectionPanelManager extends BaseManager {
	private static final String TREE_SECTION_ICON_CHARACTER = "character";
	private static final String TREE_SECTION_ICON_PICKUPS = "pickup";
	private static final String TREE_SECTION_ICON_ENV = "env";
	private final Map<EditModes, EntriesDisplayTypes> modeToEntriesDisplayType = Map.of(
			EditModes.CHARACTERS, EntriesDisplayTypes.TREE,
			EditModes.TILES, EntriesDisplayTypes.GALLERY,
			EditModes.ENVIRONMENT, EntriesDisplayTypes.TREE,
			EditModes.LIGHTS, NONE,
			EditModes.PICKUPS, EntriesDisplayTypes.TREE);
	private final Map<EditModes, TreeSection[]> modeToTreeSections = Map.of(
			EditModes.CHARACTERS, new TreeSection[]{
					new TreeSection(
							"Player",
							new CharacterDefinition[]{new PlayerDefinition()},
							TREE_SECTION_ICON_CHARACTER),
					new TreeSection(
							"Enemies",
							Enemies.values(),
							TREE_SECTION_ICON_CHARACTER)
			},
			EditModes.PICKUPS, new TreeSection[]{new TreeSection(
					"Pickups",
					WeaponsDefinitions.values(),
					TREE_SECTION_ICON_PICKUPS)
			},
			EditModes.ENVIRONMENT, new TreeSection[]{new TreeSection(
					"Environment",
					EnvironmentDefinitions.values(),
					TREE_SECTION_ICON_ENV)
			});
	private JPanel entitiesPanel;

	public EntitiesSelectionPanelManager(MapRenderer mapRenderer) {
		super(mapRenderer);
	}

	private void addEntitiesDataSelectors(File assetsFolderLocation) {
		CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
		Arrays.stream(EditModes.values()).forEach(mode -> {
			EntriesDisplayTypes entriesDisplayType = modeToEntriesDisplayType.get(mode);
			if (entriesDisplayType == EntriesDisplayTypes.GALLERY) {
				JScrollPane entitiesGallery = GuiUtils.createEntitiesGallery(assetsFolderLocation, itemEvent -> {
					if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
						Optional.ofNullable(getMapRenderer()).ifPresent(sub -> {
							Assets.SurfaceTextures texture = ((GalleryButton) itemEvent.getItem()).getTextureDefinition();
							sub.onTileSelected(texture);
						});
					}
				});
				entitiesPanel.add(entitiesGallery, mode.name() + "_" + TilesTools.BRUSH.name());
			} else if (entriesDisplayType == EntriesDisplayTypes.TREE) {
				EditorTree resourcesTree = createResourcesTree(mode);
				entitiesPanel.add(resourcesTree, mode.name() + "_" + TilesTools.BRUSH.name());
			}
		});
		entitiesPanel.add(new JPanel(), NONE.name());
		entitiesLayout.show(entitiesPanel, EditModes.TILES.name() + "_" + TilesTools.BRUSH.name());
	}

	private DefaultMutableTreeNode createSectionNodeForTree(final String header, final ElementDefinition[] definitions) {
		DefaultMutableTreeNode sectionNode = new DefaultMutableTreeNode(header);
		Arrays.stream(definitions)
				.filter(d -> d.isCanBeSeenOnTheMap())
				.forEach(def -> sectionNode.add(new DefaultMutableTreeNode(def, false)));
		return sectionNode;
	}

	private EditorTree createResourcesTree(final EditModes mode) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(mode.getDisplayName());
		EditorTree tree = new EditorTree(top);
		Arrays.stream(modeToTreeSections.get(mode)).forEach(modeSection -> {
			top.add(createSectionNodeForTree(modeSection.header(), modeSection.definitions()));
			tree.setCellRenderer(new ResourcesTreeCellRenderer(modeSection.entryIcon()));
			tree.addTreeSelectionListener(e -> {
				TreePath path = e.getPath();
				tree.setSelectionPath(path);
				if (path != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
					if (node.isLeaf()) {
						leafSelected(node, mode);
					}
				}
			});
			tree.addComponentListener(new ComponentListener() {
				@Override
				public void componentResized(ComponentEvent e) {

				}

				@Override
				public void componentMoved(ComponentEvent e) {

				}

				@Override
				public void componentShown(ComponentEvent e) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					tree.clearSelection();
					Optional.ofNullable(selectedNode)
							.ifPresent(s -> tree.getSelectionModel().setSelectionPath(new TreePath(s.getPath())));
				}

				@Override
				public void componentHidden(ComponentEvent e) {

				}
			});
		});
		tree.expandPath(new TreePath(top.getPath()));
		return tree;
	}

	private void leafSelected(DefaultMutableTreeNode node, EditModes mode) {
		ElementDefinition definition = (ElementDefinition) node.getUserObject();
		MapRenderer mapRenderer = getMapRenderer();
		if (mode == EditModes.CHARACTERS) {
			mapRenderer.onTreeCharacterSelected((CharacterDefinition) definition);
		} else if (mode == EditModes.PICKUPS) {
			mapRenderer.onTreePickupSelected((ItemDefinition) definition);
		} else if (mode == EditModes.ENVIRONMENT) {
			mapRenderer.onTreeEnvSelected((EnvironmentDefinitions) definition);
		}
	}

	public void onApplicationStart(JPanel entitiesPanel, File assetsFolderLocation) {
		this.entitiesPanel = entitiesPanel;
		addEntitiesDataSelectors(assetsFolderLocation);
	}

	public void changeEntitiesSelectionModePerTool(EditorMode mode) {
		changeEntitiesSelectionModePerTool(mode, null);
	}

	public void changeEntitiesSelectionModePerTool(EditorMode mode, EditorTool tool) {
		EditorCardLayout entitiesLayout = (EditorCardLayout) entitiesPanel.getLayout();
		String modeName = mode.name();
		String cardName = tool != null ? modeName + "_" + tool.name() : modeName;
		if (entitiesLayout.getCards().contains(cardName)) {
			entitiesLayout.show(entitiesPanel, cardName);
		} else {
			entitiesLayout.show(entitiesPanel, NONE.name());
		}
	}
}
