package com.gadarts.necromine.editor.desktop.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.gadarts.necromine.editor.desktop.DesktopLauncher;
import com.gadarts.necromine.editor.desktop.MapperGui;
import com.gadarts.necromine.editor.desktop.gui.managers.ManagersImpl;
import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.Menus;
import com.necromine.editor.MapManagerEventsSubscriber;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;
import org.lwjgl.openal.AL;
import org.pushingpixels.radiance.theming.api.skin.RadianceTwilightLookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


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
	public static final String SETTINGS_FILE = "settings.json";
	private final LwjglAWTCanvas lwjgl;
	private final MapRenderer mapRenderer;
	private final File assetsFolderLocation;
	private final ManagersImpl managers;

	public Gui(final LwjglAWTCanvas lwjgl, final MapRenderer mapRenderer, final Properties properties) {
		super(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		this.managers = new ManagersImpl(mapRenderer, this);
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
			menuItem.addActionListener((ActionListener) constructor.newInstance(
					mapRenderer,
					managers));
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


	private void addWindowComponents() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel entitiesPanel = createEntitiesPanel();
		JSplitPane splitPane = createSplitPane(lwjgl.getCanvas(), entitiesPanel);
		mainPanel.add(splitPane);
		addMenuBar();
		managers.onApplicationStart(mainPanel, this, entitiesPanel, assetsFolderLocation);
		getContentPane().add(mainPanel);
	}


	private JSplitPane createSplitPane(final Canvas canvas, final JPanel entitiesPanel) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entitiesPanel, canvas);
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(0.4);
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		BasicSplitPaneUI flatDividerSplitPaneUI = new BasicSplitPaneUI() {
			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					@Override
					public void setBorder(Border b) {
					}
				};
			}
		};
		splitPane.setUI(flatDividerSplitPaneUI);
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


	@Override
	public void onMapRendererIsReady() {
		managers.onMapRendererIsReady(mapRenderer, this);
	}
}
