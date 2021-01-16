package com.necromine.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.necromine.editor.desktop.toolbar.RadioToolBarButton;
import com.necromine.editor.desktop.toolbar.ToolBarButton;
import com.necromine.editor.desktop.toolbar.ToolbarButtonOfMenuItem;
import com.necromine.editor.desktop.toolbar.ToolbarButtonsDefinitions;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.openal.AL;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapperWindow extends JFrame {
	public static final String FOLDER_TOOLBAR_BUTTONS = "toolbar_buttons";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final String ICON_FORMAT = "png";
	private static final String FOLDER_ASSETS = "core" + File.separator + "assets";

	@Getter
	@Setter
	private static EditorModes mode;

	private final LwjglAWTCanvas lwjgl;
	private final Map<String, ButtonGroup> buttonGroups = new HashMap<>();


	public MapperWindow(final String header, final LwjglAWTCanvas lwjgl) {
		super(header);
		this.lwjgl = lwjgl;
		addToolBar(ToolbarButtonsDefinitions.values()).add(Box.createHorizontalGlue());
		defineMapperWindow(lwjgl.getCanvas());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
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
			toolBarButton = new ToolBarButton(imageIcon, buttonProperties);
		} else {
			toolBarButton = createToolbarRadioButtonOfMenuItem(button, buttonProperties, imageIcon);
		}
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
		final String FOLDER_PATH = FOLDER_ASSETS + File.separator + FOLDER_TOOLBAR_BUTTONS + File.separator;
		String pathname = FOLDER_PATH + buttonProperties.getIcon() + "." + ICON_FORMAT;
		return new ImageIcon(ImageIO.read(new File(pathname)), buttonProperties.getToolTip());
	}

	private void defineMapperWindow(final Canvas canvas) {
		defineMapperWindowAttributes();
		JPanel mainPanel = new JPanel(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), canvas);
		mainPanel.add(splitPane);
		getContentPane().add(mainPanel);
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
}
