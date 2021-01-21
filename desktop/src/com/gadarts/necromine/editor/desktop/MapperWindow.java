package com.gadarts.necromine.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.gadarts.necromine.Assets;
import com.gadarts.necromine.editor.desktop.toolbar.RadioToolBarButton;
import com.gadarts.necromine.editor.desktop.toolbar.ToolBarButton;
import com.gadarts.necromine.editor.desktop.toolbar.ToolbarButtonOfMenuItem;
import com.gadarts.necromine.editor.desktop.toolbar.ToolbarButtonsDefinitions;
import com.necromine.editor.EditorModes;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.NecromineMapEditor;
import org.lwjgl.openal.AL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;

public class MapperWindow extends JFrame implements PropertyChangeListener {
	public static final String FOLDER_TOOLBAR_BUTTONS = "toolbar_buttons";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final String ICON_FORMAT = ".png";
	private static final String FOLDER_ASSETS = "core" + File.separator + "assets";
	public static final String UI_ASSETS_FOLDER_PATH = FOLDER_ASSETS + File.separator + "%s" + File.separator + "%s" + ICON_FORMAT;

	private final LwjglAWTCanvas lwjgl;
	private final Map<String, ButtonGroup> buttonGroups = new HashMap<>();
	private final File assetsFolderLocation = new File(NecromineMapEditor.TEMP_ASSETS_FOLDER);
	private final GuiEventsSubscriber guiEventsSubscriber;
	private final ModesHandler modesHandler;
	private JPanel entitiesPanel;


	public MapperWindow(final String header, final LwjglAWTCanvas lwjgl, final GuiEventsSubscriber guiEventsSubscriber) {
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
		addToolBar(ToolbarButtonsDefinitions.values()).add(Box.createHorizontalGlue());
		defineMapperWindow(lwjgl.getCanvas());
	}

	protected JToolBar addToolBar(final ToolbarButtonsDefinitions[] toolbarOptions) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		Arrays.stream(toolbarOptions).forEach(option -> {
			AbstractButton jButton;
			try {
				jButton = createToolbarButtonOfMenuItem(option);
				toolBar.add(jButton);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
		Container contentPane = getContentPane();
		contentPane.add(toolBar, BorderLayout.PAGE_START);
		return toolBar;
	}

	private AbstractButton createToolbarButtonOfMenuItem(final ToolbarButtonsDefinitions button) throws IOException {
		ToolbarButtonOfMenuItem buttonProperties = button.getButtonProperties();
		ImageIcon imageIcon = getButtonIcon(buttonProperties);
		AbstractButton toolBarButton;
		if (button.getButtonProperties().getButtonGroup() == null) {
			toolBarButton = new ToolBarButton(imageIcon, buttonProperties, modesHandler);
		} else {
			toolBarButton = createToolbarRadioButtonOfMenuItem(button, buttonProperties, imageIcon);
		}
		toolBarButton.addPropertyChangeListener(modesHandler);
		modesHandler.addPropertyChangeListener(this);
		return toolBarButton;
	}

	private AbstractButton createToolbarRadioButtonOfMenuItem(final ToolbarButtonsDefinitions button,
															  final ToolbarButtonOfMenuItem buttonProperties,
															  final ImageIcon imageIcon) {
		AbstractButton toolBarButton;
		String groupName = button.getButtonProperties().getButtonGroup();
		if (!buttonGroups.containsKey(groupName)) {
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroups.put(groupName, buttonGroup);
		}
		toolBarButton = new RadioToolBarButton(imageIcon, buttonProperties);
		ButtonGroup buttonGroup = buttonGroups.get(groupName);
		buttonGroup.add(toolBarButton);
		return toolBarButton;
	}

	private ImageIcon getButtonIcon(final ToolbarButtonOfMenuItem buttonProperties) throws IOException {
		String path = String.format(UI_ASSETS_FOLDER_PATH, FOLDER_TOOLBAR_BUTTONS, buttonProperties.getIcon());
		return new ImageIcon(ImageIO.read(new File(path)), buttonProperties.getToolTip());
	}

	private void defineMapperWindow(final Canvas canvas) {
		defineMapperWindowAttributes();
		JPanel mainPanel = new JPanel(new BorderLayout());
		entitiesPanel = createEntitiesPanel();
		addEntitiesGallery(entitiesPanel);
		JSplitPane splitPane = createSplitPane(canvas, entitiesPanel);
		mainPanel.add(splitPane);
		getContentPane().add(mainPanel);
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

	private DefaultMutableTreeNode createSectionNodeForTree(final String header) {
		DefaultMutableTreeNode sectionNode = new DefaultMutableTreeNode(header);
//			sectionNode.add(assetEntry);
		return sectionNode;
	}

	private EditorTree createResourcesTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Characters");
		top.add(createSectionNodeForTree("Enemies"));
		EditorTree tree = new EditorTree(top);
		tree.setCellRenderer(new ResourcesTreeCellRenderer());
		tree.addMouseListener(new MouseAdapter() {
			private void runAction(final DefaultMutableTreeNode node) {
			}

			@Override
			public void mouseClicked(final MouseEvent e) {
				super.mouseClicked(e);
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				tree.setSelectionPath(path);
				if (path != null && e.getClickCount() > 1) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
					if (node.isLeaf()) {
						runAction(node);
					}
				}
			}
		});
		return tree;
	}

	private void addEntitiesGallery(final JPanel entitiesPanel) {
		try {
			entitiesPanel.add(createResourcesTree(), EntitiesPanelCards.TREE.name());
			entitiesPanel.add(createEntitiesGallery(), EntitiesPanelCards.GALLERY.name());
			CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
			entitiesLayout.next(entitiesPanel);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private JScrollPane createEntitiesGallery() throws IOException {
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
		String path = assetsFolderLocation.getAbsolutePath() + File.separator + texture.getFilePath();
		FileInputStream inputStream = new FileInputStream(path);
		GalleryButton button = new GalleryButton(texture, new ImageIcon(ImageIO.read(inputStream)));
		button.addItemListener(itemEvent -> {
			if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
				guiEventsSubscriber.onTileSelected(texture);
			}
		});
		inputStream.close();
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		if (evt.getPropertyName().equals(Events.MODE_CHANGED.name())) {
			int newModeIndex = (int) evt.getNewValue();
			EditorModes mode = EditorModes.values()[newModeIndex];
			guiEventsSubscriber.onModeChanged(mode);
			CardLayout cardLayout = (CardLayout) entitiesPanel.getLayout();
			if (mode == EditorModes.TILES) {
				cardLayout.show(entitiesPanel, EntitiesPanelCards.GALLERY.name());
			} else {
				cardLayout.show(entitiesPanel, EntitiesPanelCards.TREE.name());
			}
		}
	}

	private enum EntitiesPanelCards {TREE, GALLERY}
}
