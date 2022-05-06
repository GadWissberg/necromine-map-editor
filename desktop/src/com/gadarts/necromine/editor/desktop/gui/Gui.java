package com.gadarts.necromine.editor.desktop.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.*;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.Menus;
import com.gadarts.necromine.editor.desktop.gui.toolbar.*;
import com.google.gson.Gson;
import com.necromine.editor.EntriesDisplayTypes;
import com.necromine.editor.MapManagerEventsSubscriber;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;
import org.lwjgl.openal.AL;
import org.pushingpixels.radiance.theming.api.skin.RadianceTwilightLookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;

import static com.necromine.editor.EntriesDisplayTypes.NONE;


public class Gui extends JFrame implements MapManagerEventsSubscriber {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final String FOLDER_TOOLBAR_BUTTONS = "toolbar_buttons";
	public static final int MENU_ITEM_IMAGE_SIZE = 16;
	public static final int MENU_LABEL_PAD = 4;
	public static final String DEFAULT_MAP_NAME = "Unnamed map";
	public static final String WINDOW_HEADER = "%s - %s";
	public static final String PROGRAM_TILE = "Necronemes Map Editor";
	public static final String SETTINGS_KEY_LAST_OPENED_FILE = "last_opened_file";
	static final String SETTINGS_FILE = "settings.json";
	private final LwjglAWTCanvas lwjgl;
	private final MapRenderer mapRenderer;
	private final File assetsFolderLocation;
	private final Gson gson = new Gson();
	private final Map<EditModes, EntriesDisplayTypes> modeToEntriesDisplayType = Map.of(
			EditModes.CHARACTERS, EntriesDisplayTypes.TREE,
			EditModes.TILES, EntriesDisplayTypes.GALLERY,
			EditModes.ENVIRONMENT, EntriesDisplayTypes.TREE,
			EditModes.LIGHTS, NONE,
			EditModes.PICKUPS, EntriesDisplayTypes.TREE);
	private final Map<String, ButtonGroup> buttonGroups = new HashMap<>();
	private final PersistenceManager persistenceManager = new PersistenceManager();
	private final ModesManager modesManager = new ModesManager();
	private final DialogsManager dialogsManager = new DialogsManager(this);
	private JPanel entitiesPanel;
	private JPanel subToolbarPanel;

	public Gui(final LwjglAWTCanvas lwjgl, final MapRenderer mapRenderer, final Properties properties) {
		super(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		this.lwjgl = lwjgl;
		this.mapRenderer = mapRenderer;
		this.assetsFolderLocation = new File(properties.getProperty(DesktopLauncher.PROPERTIES_KEY_ASSETS_PATH));
		try {
			UIManager.setLookAndFeel(new RadianceTwilightLookAndFeel());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		defineWindow();
	}

	private void addEntitiesDataSelectors( ) {
		CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
		Arrays.stream(EditModes.values()).forEach(mode -> {
			EntriesDisplayTypes entriesDisplayType = modeToEntriesDisplayType.get(mode);
			if (entriesDisplayType == EntriesDisplayTypes.GALLERY) {
				JScrollPane entitiesGallery = GuiUtils.createEntitiesGallery(assetsFolderLocation, itemEvent -> {
					if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
						Optional.ofNullable(mapRenderer).ifPresent(sub -> {
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
		EditModes mode = (EditModes) ModesManager.getCurrentMode();
		entitiesLayout.show(entitiesPanel, mode.name());
	}

//	private void addSubToolbars(final JPanel mainPanel) {
//		CardLayout subToolbarsCardLayout = new CardLayout();
//		subToolbarPanel = new JPanel(subToolbarsCardLayout);
//		mainPanel.add(subToolbarPanel, BorderLayout.PAGE_START);
//		Arrays.stream(SubToolbarsDefinitions.values()).forEach(sub ->
//				addToolBar(sub.getButtons(), subToolbarPanel, sub.name()).add(Box.createHorizontalGlue()));
//		subToolbarsCardLayout.show(subToolbarPanel, SubToolbarsDefinitions.TILES.name());
//	}

	private void addMenuBar( ) {
		JMenuBar menuBar = new JMenuBar();
		Arrays.stream(Menus.values()).forEach(menu -> {
			JMenu jMenu = new JMenu(menu.getLabel());
			jMenu.setBorder(new EmptyBorder(MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD));
			Arrays.stream(menu.getDefinitions()).forEach(item -> jMenu.add(createMenuItem(item)));
			menuBar.add(jMenu);
		});
		setJMenuBar(menuBar);
	}

	private JMenuItem createMenuItem(MenuItemDefinition item) {
		JMenuItem menuItem = new JMenuItem(item.getMenuItemProperties().getLabel());
		if (item.getMenuItemProperties().getIcon() != null) {
			try {
				ImageIcon icon = createMenuItemIcon(item.getMenuItemProperties());
				menuItem.setIcon(icon);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			Constructor<?> constructor = item.getMenuItemProperties().getActionClass().getConstructors()[0];
			menuItem.addActionListener((ActionListener) constructor.newInstance(
					persistenceManager,
					mapRenderer,
					modesManager,
					dialogsManager));
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		menuItem.setBorder(new EmptyBorder(MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD));
		return menuItem;
	}

	private ImageIcon createMenuItemIcon(MenuItemProperties prop) throws IOException {
		String path = String.format(MapperGui.UI_ASSETS_FOLDER_PATH, FOLDER_TOOLBAR_BUTTONS, prop.getIcon());
		ImageIcon icon = new ImageIcon(ImageIO.read(new File(path)));
		icon = new ImageIcon(icon.getImage().getScaledInstance(MENU_ITEM_IMAGE_SIZE, MENU_ITEM_IMAGE_SIZE, 0));
		return icon;
	}

	private JPanel createEntitiesPanel( ) {
		CardLayout entitiesLayout = new CardLayout();
		return new JPanel(entitiesLayout);
	}

	private void defineWindow( ) {
		addWindowComponents();
		defineWindowClose();
		setSize(WIDTH, HEIGHT);
		setLocationByPlatform(true);
		setVisible(true);
		setResizable(false);
	}

	private AbstractButton createToolbarRadioButtonOfMenuItem(ToolbarButtonDefinition button,
															  ToolbarButtonProperties buttonProperties) throws IOException {
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
		toolBarButton = new RadioToolBarButton(
				buttonProperties,
				persistenceManager,
				mapRenderer,
				modesManager,
				dialogsManager);
		ButtonGroup buttonGroup = buttonGroups.get(groupName);
		buttonGroup.add(toolBarButton);
		if (isNew) {
			toolBarButton.setSelected(true);
		}
		return toolBarButton;
	}

	private AbstractButton createToolbarButtonOfMenuItem(final ToolbarButtonDefinition buttonDefinition) throws IOException {
		ToolbarButtonProperties props = buttonDefinition.getButtonProperties();
		AbstractButton button;
		MenuItemDefinition def = props.getMenuItemDefinition();
		if (isRegularToolbarButton(props, def)) {
			button = new ToolBarButton(props, persistenceManager, mapRenderer, modesManager, dialogsManager);
		} else {
			button = createToolbarRadioButtonOfMenuItem(buttonDefinition, props);
		}
		return button;
	}

	private boolean isRegularToolbarButton(ToolbarButtonProperties props, MenuItemDefinition def) {
		return (def == null && props.getButtonGroup() == null)
				|| (def != null && def.getMenuItemProperties().getButtonGroup() == null);
	}

	protected JToolBar addToolBar(final ToolbarButtonDefinition[] toolbarOptions) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		Arrays.stream(toolbarOptions).forEach(option -> {
			if (option.getButtonProperties() != null) {
				AbstractButton jButton;
				try {
					jButton = createToolbarButtonOfMenuItem(option);
					toolBar.add(jButton);
					toolBar.setName(BorderLayout.PAGE_START);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			} else {
				toolBar.addSeparator();
			}
		});
		add(toolBar, BorderLayout.PAGE_START);
		return toolBar;
	}

	private void addWindowComponents( ) {
		JPanel mainPanel = new JPanel(new BorderLayout());
		entitiesPanel = createEntitiesPanel();
		JSplitPane splitPane = createSplitPane(lwjgl.getCanvas(), entitiesPanel);
		mainPanel.add(splitPane);
		addMenuBar();
		addToolBar(ToolbarDefinitions.values()).add(Box.createHorizontalGlue());
		addEntitiesDataSelectors();
		getContentPane().add(mainPanel);
	}

	private JSplitPane createSplitPane(final Canvas canvas, final JPanel entitiesPanel) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entitiesPanel, canvas);
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(0.4);
		return splitPane;
	}

	private void defineWindowClose( ) {
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
	public void onTileSelectedUsingWallTilingTool(final FlatNode src, final FlatNode dst) {

	}

	@Override
	public void onTilesSelectedForLifting(final int srcRow, final int srcCol, final int dstRow, final int dstCol) {

	}

	@Override
	public void onNodeSelectedToSelectPlacedObjectsInIt(final List<? extends PlacedElement> elementsInTheNode, final ActionAnswer<PlacedElement> answer) {

	}

	@Override
	public void onSelectedEnvObjectToDefine(final PlacedEnvObject data) {

	}


	@Override
	public void onEditorIsReady( ) {
		persistenceManager.readSettingsFile(this, mapRenderer);
	}
}
