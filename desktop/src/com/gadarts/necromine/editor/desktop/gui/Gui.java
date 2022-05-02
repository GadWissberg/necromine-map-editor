package com.gadarts.necromine.editor.desktop.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.bulenkov.darcula.DarculaLaf;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.DesktopLauncher;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.editor.desktop.MapperGui;
import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.Menus;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarDefinition;
import com.gadarts.necromine.editor.desktop.toolbar.RadioToolBarButton;
import com.gadarts.necromine.editor.desktop.toolbar.ToolBarButton;
import com.gadarts.necromine.editor.desktop.toolbar.ToolbarButtonDefinition;
import com.google.gson.Gson;
import com.necromine.editor.EntriesDisplayTypes;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.MapManagerEventsSubscriber;
import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;
import org.lwjgl.openal.AL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

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
	private static final String FOLDER_ASSETS = "core" + File.separator + "assets";
	private static final String ICON_FORMAT = ".png";
	public static final String UI_ASSETS_FOLDER_PATH = FOLDER_ASSETS + File.separator + "%s" + File.separator + "%s"
			+ ICON_FORMAT;
	private final LwjglAWTCanvas lwjgl;
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final File assetsFolderLocation;
	private final Gson gson = new Gson();
	private final Map<EditModes, EntriesDisplayTypes> modeToEntriesDisplayType = Map.of(
			EditModes.CHARACTERS, EntriesDisplayTypes.TREE,
			EditModes.TILES, EntriesDisplayTypes.GALLERY,
			EditModes.ENVIRONMENT, EntriesDisplayTypes.TREE,
			EditModes.LIGHTS, NONE,
			EditModes.PICKUPS, EntriesDisplayTypes.TREE);
	private final Map<String, ButtonGroup> buttonGroups = new HashMap<>();
	private final FileManager fileManager = new FileManager();
	private JPanel entitiesPanel;
	private Map<String, String> settings = new HashMap<>();

	public Gui(final LwjglAWTCanvas lwjgl, final GuiEventsSubscriber guiEventsSubscriber, final Properties properties) {
		super(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		this.lwjgl = lwjgl;
		this.guiEventsSubscriber = guiEventsSubscriber;
		this.assetsFolderLocation = new File(properties.getProperty(DesktopLauncher.PROPERTIES_KEY_ASSETS_PATH));
		try {
			UIManager.setLookAndFeel(new DarculaLaf());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		defineWindow();
	}

	private void addEntitiesDataSelectors() {
		CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
		Arrays.stream(EditModes.values()).forEach(mode -> {
			EntriesDisplayTypes entriesDisplayType = modeToEntriesDisplayType.get(mode);
			if (entriesDisplayType == EntriesDisplayTypes.GALLERY) {
				JScrollPane entitiesGallery = GuiUtils.createEntitiesGallery(assetsFolderLocation, itemEvent -> {
					if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
						Optional.ofNullable(guiEventsSubscriber).ifPresent(sub -> {
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
		EditModes mode = (EditModes) ModesHandler.getMode();
		entitiesLayout.show(entitiesPanel, mode.name());
	}

	private void addMenuBar() {
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
			menuItem.addActionListener((ActionListener) constructor.newInstance(fileManager, guiEventsSubscriber, settings));
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

	private JPanel createEntitiesPanel() {
		CardLayout entitiesLayout = new CardLayout();
		return new JPanel(entitiesLayout);
	}

	private void defineWindow() {
		addWindowComponents();
		defineWindowClose();
		setSize(WIDTH, HEIGHT);
		setLocationByPlatform(true);
		setVisible(true);
		setResizable(false);
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

	private AbstractButton createToolbarButtonOfMenuItem(final ToolbarButtonDefinition button) throws IOException {
		ToolbarButtonProperties props = button.getButtonProperties();
		ImageIcon imageIcon = getButtonIcon(props);
		AbstractButton toolBarButton;
		MenuItemDefinition def = props.getMenuItemDefinition();
		if ((def == null && props.getButtonGroup() == null) || (def != null && def.getMenuItemProperties().getButtonGroup() == null)) {
			toolBarButton = new ToolBarButton(imageIcon, props, fileManager, guiEventsSubscriber, settings);
		} else {
			toolBarButton = createToolbarRadioButtonOfMenuItem(button, props, imageIcon);
		}
		return toolBarButton;
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

	private void addWindowComponents() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		entitiesPanel = createEntitiesPanel();
		JSplitPane splitPane = createSplitPane(lwjgl.getCanvas(), entitiesPanel);
		mainPanel.add(splitPane);
		addMenuBar();
		addToolBar(ToolbarDefinition.values()).add(Box.createHorizontalGlue());
		addEntitiesDataSelectors();
		getContentPane().add(mainPanel);
	}

	private JSplitPane createSplitPane(final Canvas canvas, final JPanel entitiesPanel) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entitiesPanel, canvas);
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(0.4);
		return splitPane;
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

	private void readSettingsFile() {
		File settingsFile = new File(SETTINGS_FILE);
		if (settingsFile.exists()) {
			try {
				Reader json = new FileReader(SETTINGS_FILE);
				//noinspection unchecked
				settings = gson.fromJson(json, HashMap.class);
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
				final String MESSAGE = "Failed to read the editor's settings!";
				JOptionPane.showMessageDialog(this, MESSAGE, PROGRAM_TILE, JOptionPane.ERROR_MESSAGE);
			}
			if (settings.containsKey(SETTINGS_KEY_LAST_OPENED_FILE)) {
				File file = new File(settings.get(SETTINGS_KEY_LAST_OPENED_FILE));
				tryOpeningFile(file);
			}
		} else {
			settings.clear();
		}
	}

	private void updateCurrentlyOpenedFile(final File file) {
		fileManager.setCurrentlyOpenedMap(file);
		setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, file.getName()));
		settings.put(SETTINGS_KEY_LAST_OPENED_FILE, file.getPath());
		fileManager.saveSettings(settings);
	}


	private void tryOpeningFile(final File file) {
		try {
			guiEventsSubscriber.onLoadMapRequested(file.getPath());
			updateCurrentlyOpenedFile(file);
		} catch (final IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to open map file!");
		}
	}

	@Override
	public void onEditorIsReady() {
		readSettingsFile();
	}
}
