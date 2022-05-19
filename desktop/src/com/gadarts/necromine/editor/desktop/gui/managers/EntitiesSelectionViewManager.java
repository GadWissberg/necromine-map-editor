package com.gadarts.necromine.editor.desktop.gui.managers;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.editor.desktop.ModesManager;
import com.necromine.editor.EntriesDisplayTypes;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.mode.EditModes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.necromine.editor.EntriesDisplayTypes.NONE;


public class EntitiesSelectionViewManager extends BaseManager {
	private final Map<EditModes, EntriesDisplayTypes> modeToEntriesDisplayType = Map.of(
			EditModes.CHARACTERS, EntriesDisplayTypes.TREE,
			EditModes.TILES, EntriesDisplayTypes.GALLERY,
			EditModes.ENVIRONMENT, EntriesDisplayTypes.TREE,
			EditModes.LIGHTS, NONE,
			EditModes.PICKUPS, EntriesDisplayTypes.TREE);

	public EntitiesSelectionViewManager(MapRenderer mapRenderer) {
		super(mapRenderer);
	}

	private void addEntitiesDataSelectors(JPanel entitiesPanel, File assetsFolderLocation) {
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
				entitiesPanel.add(entitiesGallery, EditModes.TILES.name());
			} else if (entriesDisplayType == EntriesDisplayTypes.TREE) {
//				EditorTree resourcesTree = createResourcesTree(mode);
//				resourcesTree.addPropertyChangeListener(this);
//				entitiesPanel.add(resourcesTree, mode.name());
			}
		});
		entitiesPanel.add(new JPanel(), NONE.name());
		EditModes mode = (EditModes) ModesManager.getSelectedMode();
		entitiesLayout.show(entitiesPanel, mode.name());
	}

	public void onApplicationStart(JPanel entitiesPanel, File assetsFolderLocation) {
		addEntitiesDataSelectors(entitiesPanel, assetsFolderLocation);
	}
}
