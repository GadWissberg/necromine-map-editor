package com.gadarts.necromine.editor.desktop.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.DesktopLauncher;
import com.gadarts.necromine.editor.desktop.GalleryButton;
import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.Menus;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static com.necromine.editor.EntriesDisplayTypes.NONE;


public class Gui extends JFrame implements MapManagerEventsSubscriber {
	private static final String FOLDER_ASSETS = "core" + File.separator + "assets";
	private static final String ICON_FORMAT = ".png";

	public static final String UI_ASSETS_FOLDER_PATH = FOLDER_ASSETS + File.separator + "%s" + File.separator + "%s"
			+ ICON_FORMAT;
	private static final String DEFAULT_MAP_NAME = "Unnamed map";
	private static final String WINDOW_HEADER = "%s - %s";
	private static final String PROGRAM_TILE = "Necronemes Map Editor";
	private static final String SETTINGS_FILE = "settings.json";
	private static final String SETTINGS_KEY_LAST_OPENED_FILE = "last_opened_file";
	private final LwjglAWTCanvas lwjgl;
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final File assetsFolderLocation;
	private final Gson gson = new Gson();
	private JPanel entitiesPanel;
	private Map<String, String> settings;
	private File currentlyOpenedMap;
	private final Map<EditModes, EntriesDisplayTypes> modeToEntriesDisplayType = Map.of(
			EditModes.CHARACTERS, EntriesDisplayTypes.TREE,
			EditModes.TILES, EntriesDisplayTypes.GALLERY,
			EditModes.ENVIRONMENT, EntriesDisplayTypes.TREE,
			EditModes.LIGHTS, NONE,
			EditModes.PICKUPS, EntriesDisplayTypes.TREE);

	public Gui(final LwjglAWTCanvas lwjgl, final GuiEventsSubscriber guiEventsSubscriber, final Properties properties) {
		super(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		this.lwjgl = lwjgl;
		this.guiEventsSubscriber = guiEventsSubscriber;
		this.assetsFolderLocation = new File(properties.getProperty(DesktopLauncher.PROPERTIES_KEY_ASSETS_PATH));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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

	private JPanel createEntitiesPanel() {
		CardLayout entitiesLayout = new CardLayout();
		return new JPanel(entitiesLayout);
	}

	private void defineWindow() {
		addWindowInternals();
		defineWindowClose();
		setSize(WIDTH, HEIGHT);
		setLocationByPlatform(true);
		setVisible(true);
		setResizable(false);
	}

//	private ImageIcon getButtonIcon(final ToolbarButtonProperties buttonProperties) throws IOException {
//		String path = String.format(UI_ASSETS_FOLDER_PATH, FOLDER_TOOLBAR_BUTTONS, buttonProperties.getIcon());
//		return new ImageIcon(ImageIO.read(new File(path)), buttonProperties.getToolTip());
//	}

//	private AbstractButton createToolbarButtonOfMenuItem(final ToolbarButtonDefinition button) throws IOException {
//		ToolbarButtonProperties buttonProperties = button.getButtonProperties();
//		ImageIcon imageIcon = getButtonIcon(buttonProperties);
//		AbstractButton toolBarButton;
//		MenuItemDefinition menuItemDefinition = buttonProperties.getMenuItemDefinition();
//		if ((menuItemDefinition == null && buttonProperties.getButtonGroup() == null) || (menuItemDefinition != null && menuItemDefinition.getMenuItemProperties().getButtonGroup() == null)) {
//			toolBarButton = new ToolBarButton(imageIcon, buttonProperties);
//		} else {
//			toolBarButton = createToolbarRadioButtonOfMenuItem(button, buttonProperties, imageIcon);
//		}
//		toolBarButton.addPropertyChangeListener(modesHandler);
//		toolBarButton.addPropertyChangeListener(this);
//		modesHandler.addPropertyChangeListener(this);
//		return toolBarButton;
//	}

//	protected JToolBar addToolBar(final ToolbarButtonDefinition[] toolbarOptions) {
//		JToolBar toolBar = new JToolBar();
//		toolBar.setFloatable(false);
//		Arrays.stream(toolbarOptions).forEach(option -> {
//			if (option.getButtonProperties() != null) {
//				AbstractButton jButton;
//				try {
//					jButton = createToolbarButtonOfMenuItem(option);
//					toolBar.add(jButton);
//					toolBar.setName(BorderLayout.PAGE_START);
//				} catch (final IOException e) {
//					e.printStackTrace();
//				}
//			} else {
//				toolBar.addSeparator();
//			}
//		});
//		add(toolBar, BorderLayout.PAGE_START);
//		return toolBar;
//	}

	private void addWindowInternals() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		entitiesPanel = createEntitiesPanel();
		JSplitPane splitPane = createSplitPane(lwjgl.getCanvas(), entitiesPanel);
		mainPanel.add(splitPane);
		addMenuBar();
//		addToolBar(ToolbarDefinition.values()).add(Box.createHorizontalGlue());
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
			settings = new HashMap<>();
		}
	}

	private void updateCurrentlyOpenedFile(final File file) {
		currentlyOpenedMap = file;
		setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, file.getName()));
		settings.put(SETTINGS_KEY_LAST_OPENED_FILE, file.getPath());
		saveSettings();
	}

	private void saveSettings() {
		String serialized = gson.toJson(settings);
		try (PrintWriter out = new PrintWriter(SETTINGS_FILE)) {
			out.println(serialized);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
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
