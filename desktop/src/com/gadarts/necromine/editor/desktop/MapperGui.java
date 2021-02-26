package com.gadarts.necromine.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.menu.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.menu.definitions.Menus;
import com.gadarts.necromine.editor.desktop.toolbar.*;
import com.gadarts.necromine.editor.desktop.tree.EditorTree;
import com.gadarts.necromine.editor.desktop.tree.ResourcesTreeCellRenderer;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.MapHandlerEventsSubscriber;
import com.necromine.editor.mode.CameraModes;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.EntriesDisplayTypes;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.MapHandler;
import com.necromine.editor.mode.TilesTools;
import org.lwjgl.openal.AL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.necromine.editor.EntriesDisplayTypes.NONE;

public class MapperGui extends JFrame implements PropertyChangeListener, MapHandlerEventsSubscriber {
	public static final String FOLDER_TOOLBAR_BUTTONS = "toolbar_buttons";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final String ICON_FORMAT = ".png";
	private static final String FOLDER_ASSETS = "core" + File.separator + "assets";
	public static final String UI_ASSETS_FOLDER_PATH = FOLDER_ASSETS + File.separator + "%s" + File.separator + "%s" + ICON_FORMAT;

	private final LwjglAWTCanvas lwjgl;
	private final Map<String, ButtonGroup> buttonGroups = new HashMap<>();
	private final File assetsFolderLocation = new File(MapHandler.TEMP_ASSETS_FOLDER);
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final ModesHandler modesHandler;
	private JPanel entitiesPanel;


	public MapperGui(final String header, final LwjglAWTCanvas lwjgl, final GuiEventsSubscriber guiEventsSubscriber) {
		super(header);
		this.lwjgl = lwjgl;
		this.guiEventsSubscriber = guiEventsSubscriber;
		modesHandler = new ModesHandler();
		modesHandler.setVisible(false);
		add(modesHandler);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		addToolBar(ToolbarDefinition.values(), this).add(Box.createHorizontalGlue());
		addMenuBar();
		defineMapperWindow(lwjgl.getCanvas());
	}

	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		Arrays.stream(Menus.values()).forEach(menu -> {
			JMenu jMenu = new JMenu(menu.getLabel());
			Arrays.stream(menu.getDefinitions()).forEach(item -> {
				MenuItemProperties menuItemProperties = item.getMenuItemProperties();
				JMenuItem menuItem = new JMenuItem(menuItemProperties.getLabel());
				menuItem.addActionListener(menuItemProperties.getAction());
				jMenu.add(menuItem);
			});
			menuBar.add(jMenu);
		});
		setJMenuBar(menuBar);
	}

	protected JToolBar addToolBar(final ToolbarButtonDefinition[] toolbarOptions, final Container container) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		Arrays.stream(toolbarOptions).forEach(option -> {
			if (option.getButtonProperties() != null) {
				AbstractButton jButton;
				try {
					jButton = createToolbarButtonOfMenuItem(option);
					toolBar.add(jButton);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			} else {
				toolBar.addSeparator();
			}
		});
		container.add(toolBar, BorderLayout.PAGE_START);
		return toolBar;
	}

	private AbstractButton createToolbarButtonOfMenuItem(final ToolbarButtonDefinition button) throws IOException {
		ToolbarButtonProperties buttonProperties = button.getButtonProperties();
		ImageIcon imageIcon = getButtonIcon(buttonProperties);
		AbstractButton toolBarButton;
		MenuItemDefinition menuItemDefinition = buttonProperties.getMenuItemDefinition();
		if ((menuItemDefinition == null && buttonProperties.getButtonGroup() == null) || (menuItemDefinition != null && menuItemDefinition.getMenuItemProperties().getButtonGroup() == null)) {
			toolBarButton = new ToolBarButton(imageIcon, buttonProperties, modesHandler);
		} else {
			toolBarButton = createToolbarRadioButtonOfMenuItem(button, buttonProperties, imageIcon);
		}
		toolBarButton.addPropertyChangeListener(modesHandler);
		toolBarButton.addPropertyChangeListener(this);
		modesHandler.addPropertyChangeListener(this);
		return toolBarButton;
	}

	private AbstractButton createToolbarRadioButtonOfMenuItem(final ToolbarButtonDefinition button,
															  final ToolbarButtonProperties buttonProperties,
															  final ImageIcon imageIcon) {
		AbstractButton toolBarButton;
		MenuItemDefinition menuItemDefinition = button.getButtonProperties().getMenuItemDefinition();
		String groupName;
		if (menuItemDefinition != null) {
			MenuItemProperties menuItemProperties = menuItemDefinition.getMenuItemProperties();
			groupName = menuItemProperties.getButtonGroup();
		} else {
			groupName = buttonProperties.getButtonGroup();
		}
		boolean isNew = !buttonGroups.containsKey(groupName);
		if (isNew) {
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroups.put(groupName, buttonGroup);
		}
		toolBarButton = new RadioToolBarButton(imageIcon, buttonProperties);
		ButtonGroup buttonGroup = buttonGroups.get(groupName);
		buttonGroup.add(toolBarButton);
		if (isNew) {
			toolBarButton.setSelected(true);
		}
		return toolBarButton;
	}

	private ImageIcon getButtonIcon(final ToolbarButtonProperties buttonProperties) throws IOException {
		String path = String.format(UI_ASSETS_FOLDER_PATH, FOLDER_TOOLBAR_BUTTONS, buttonProperties.getIcon());
		return new ImageIcon(ImageIO.read(new File(path)), buttonProperties.getToolTip());
	}

	private void defineMapperWindow(final Canvas canvas) {
		defineMapperWindowAttributes();
		JPanel mainPanel = new JPanel(new BorderLayout());
		addSubToolbars(mainPanel);
		entitiesPanel = createEntitiesPanel();
		addEntitiesDataSelectors(entitiesPanel);
		JSplitPane splitPane = createSplitPane(canvas, entitiesPanel);
		mainPanel.add(splitPane);
		getContentPane().add(mainPanel);
	}

	private void addSubToolbars(final JPanel mainPanel) {
		CardLayout subToolbarsCardLayout = new CardLayout();
		JPanel panel = new JPanel(subToolbarsCardLayout);
		mainPanel.add(panel, BorderLayout.PAGE_START);
		Arrays.stream(SubToolbarsDefinitions.values()).forEach(subToolbarDefinition ->
				addToolBar(subToolbarDefinition.getButtons(), panel).add(Box.createHorizontalGlue()));
	}

	private JSplitPane createSplitPane(final Canvas canvas, final JPanel entitiesPanel) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entitiesPanel, canvas);
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(0.4);
		return splitPane;
	}

	private JPanel createEntitiesPanel() {
		CardLayout entitiesLayout = new CardLayout();
		return new JPanel(entitiesLayout);
	}

	private DefaultMutableTreeNode createSectionNodeForTree(final String header, final ElementDefinition[] definitions) {
		DefaultMutableTreeNode sectionNode = new DefaultMutableTreeNode(header);
		Arrays.stream(definitions).forEach(def -> sectionNode.add(new DefaultMutableTreeNode(def, false)));
		return sectionNode;
	}

	private EditorTree createResourcesTree(final EditModes mode) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(mode.getDisplayName());
		EditorTree tree = new EditorTree(top);
		Arrays.stream(mode.getTreeSections()).forEach(modeSection -> {
			top.add(createSectionNodeForTree(modeSection.getHeader(), modeSection.getDefinitions()));
			tree.setCellRenderer(new ResourcesTreeCellRenderer(modeSection.getEntryIcon()));
			tree.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					super.mouseClicked(e);
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					tree.setSelectionPath(path);
					if (path != null && e.getClickCount() > 1) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						if (node.isLeaf()) {
							ElementDefinition definition = (ElementDefinition) node.getUserObject();
							tree.firePropertyChange(Events.TREE_ENTRY_SELECTED.name(), null, definition);
						}
					}
				}
			});
		});
		tree.expandPath(new TreePath(top.getPath()));
		return tree;
	}

	private void addEntitiesDataSelectors(final JPanel entitiesPanel) {
		CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
		Arrays.stream(EditModes.values()).forEach(mode -> {
			EntriesDisplayTypes entriesDisplayType = mode.getEntriesDisplayTypes();
			if (entriesDisplayType == EntriesDisplayTypes.GALLERY) {
				entitiesPanel.add(createEntitiesGallery(), EditModes.TILES.name());
			} else if (entriesDisplayType == EntriesDisplayTypes.TREE) {
				EditorTree resourcesTree = createResourcesTree(mode);
				resourcesTree.addPropertyChangeListener(this);
				entitiesPanel.add(resourcesTree, mode.name());
			}
		});
		entitiesPanel.add(new JPanel(), NONE.name());
		EditModes mode = (EditModes) ModesHandler.getMode();
		entitiesLayout.show(entitiesPanel, mode.name());
	}

	private JScrollPane createEntitiesGallery() {
		GridLayout layout = new GridLayout(0, 3);
		JPanel gallery = new JPanel(layout);
		JScrollPane jScrollPane = new JScrollPane(gallery);
		fillGallery(gallery);
		jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return jScrollPane;
	}

	private void fillGallery(final JPanel gallery) {
		ButtonGroup buttonGroup = new ButtonGroup();
		Arrays.stream(Assets.FloorsTextures.values()).forEach(texture -> {
			try {
				addImageButtonToGallery(gallery, texture, buttonGroup);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void addImageButtonToGallery(final JPanel gallery,
										 final Assets.FloorsTextures texture,
										 final ButtonGroup buttonGroup) throws IOException {
		GalleryButton button = GuiUtils.createTextureImageButton(assetsFolderLocation, texture, itemEvent -> {
			if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
				guiEventsSubscriber.onTileSelected(texture);
			}
		});
		buttonGroup.add(button);
		gallery.add(button);
	}


	private void defineMapperWindowAttributes() {
		defineWindowClose();
		setSize(WIDTH, HEIGHT);
		setLocationByPlatform(true);
		setVisible(true);
		setResizable(false);
	}

	private void defineWindowClose() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				lwjgl.stop();
				AL.destroy();
				e.getWindow().dispose();
			}
		});
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		EditorMode mode = ModesHandler.getMode();
		if (propertyName.equals(Events.MODE_SET_EDIT.name())) {
			int newModeIndex = (int) evt.getNewValue();
			mode = EditModes.values()[newModeIndex];
			EditModes editMode = (EditModes) mode;
			CardLayout cardLayout = (CardLayout) entitiesPanel.getLayout();
			guiEventsSubscriber.onEditModeSet(editMode);
			if (editMode.getEntriesDisplayTypes() != NONE) {
				cardLayout.show(entitiesPanel, editMode.name());
			} else {
				cardLayout.show(entitiesPanel, NONE.name());
			}
		} else if (propertyName.equals(Events.MODE_SET_CAMERA.name())) {
			guiEventsSubscriber.onCameraModeSet((CameraModes) mode);
			CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
			entitiesLayout.show(entitiesPanel, NONE.name());
		} else if (propertyName.equals(Events.TILE_TOOL_SET.name())) {
			TilesTools selectedTool = TilesTools.values()[(int) evt.getNewValue()];
			CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
			if (selectedTool == TilesTools.BRUSH) {
				entitiesLayout.show(entitiesPanel, EditModes.TILES.name());
			} else {
				entitiesLayout.show(entitiesPanel, NONE.name());
			}
			guiEventsSubscriber.onToolSet(selectedTool);
		} else if (propertyName.equals(Events.TREE_ENTRY_SELECTED.name())) {
			if (mode == EditModes.CHARACTERS) {
				guiEventsSubscriber.onTreeCharacterSelected((CharacterDefinition) evt.getNewValue());
			} else if (mode == EditModes.ENVIRONMENT) {
				guiEventsSubscriber.onTreeEnvSelected((EnvironmentDefinitions) evt.getNewValue());
			} else if (mode == EditModes.PICKUPS) {
				guiEventsSubscriber.onTreePickupSelected((ItemDefinition) evt.getNewValue());
			}
		} else if (propertyName.equals(Events.REQUEST_TO_ROTATE_SELECTED_OBJECT.name())) {
			guiEventsSubscriber.onSelectedObjectRotate((Integer) evt.getNewValue());
		} else if (propertyName.equals(Events.REQUEST_TO_SAVE.name())) {
			guiEventsSubscriber.onSaveMapRequested();
		} else if (propertyName.equals(Events.REQUEST_TO_LOAD.name())) {
			guiEventsSubscriber.onLoadMapRequested();
		}
	}

	@Override
	public void onTileSelectedUsingWallTilingTool(final int row, final int col) {
		WallTilingDialog pane = new WallTilingDialog(assetsFolderLocation, guiEventsSubscriber);
		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this));
		defineWallTilingDialog(pane, dialog);
		dialog.setVisible(true);
	}

	private void defineWallTilingDialog(final WallTilingDialog pane, final JDialog dialog) {
		dialog.setTitle(pane.getDialogTitle());
		dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setContentPane(pane);
		dialog.setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		dialog.pack();
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}