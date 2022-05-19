package com.gadarts.necromine.editor.desktop.gui.managers;

import com.gadarts.necromine.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.necromine.editor.desktop.gui.toolbar.RadioToolBarButton;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolBarButton;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonDefinition;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarDefinitions;
import com.gadarts.necromine.editor.desktop.gui.toolbar.sub.SubToolbarsDefinitions;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.mode.EditorMode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ToolbarsManager extends BaseManager {
	private final Map<String, ButtonGroup> buttonGroups = new HashMap<>();
	private final Managers managers;
	private JPanel subToolbarPanel;

	public ToolbarsManager(MapRenderer mapRenderer, Managers managers) {
		super(mapRenderer);
		this.managers = managers;
	}

	public void onApplicationStart(JPanel mainPanel, JFrame parent) {
		JToolBar toolbar = createToolBar(ToolbarDefinitions.values(), parent, BorderLayout.PAGE_START);
		mainPanel.add(toolbar.add(Box.createHorizontalGlue()), BorderLayout.PAGE_START);
		addSubToolbars(mainPanel);
	}

	private AbstractButton createToolbarRadioButtonOfMenuItem(ToolbarButtonDefinition button,
															  ToolbarButtonProperties buttonProperties) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
				getMapRenderer(),
				managers);
		ButtonGroup buttonGroup = buttonGroups.get(groupName);
		buttonGroup.add(toolBarButton);
		if (isNew) {
			toolBarButton.setSelected(true);
		}
		return toolBarButton;
	}

	private AbstractButton createToolbarButtonOfMenuItem(final ToolbarButtonDefinition buttonDefinition) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
		ToolbarButtonProperties props = buttonDefinition.getButtonProperties();
		AbstractButton button;
		MenuItemDefinition def = props.getMenuItemDefinition();
		if (isRegularToolbarButton(props, def)) {
			button = new ToolBarButton(props, getMapRenderer(), managers);
		} else {
			button = createToolbarRadioButtonOfMenuItem(buttonDefinition, props);
		}
		return button;
	}

	private boolean isRegularToolbarButton(ToolbarButtonProperties props, MenuItemDefinition def) {
		return (def == null && props.getButtonGroup() == null)
				|| (def != null && def.getMenuItemProperties().getButtonGroup() == null);
	}

	protected JToolBar createToolBar(final ToolbarButtonDefinition[] toolbarOptions,
									 final Container container,
									 final String name) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		Arrays.stream(toolbarOptions).forEach(option -> {
			if (option.getButtonProperties() != null) {
				AbstractButton jButton;
				try {
					jButton = createToolbarButtonOfMenuItem(option);
					toolBar.add(jButton);
					toolBar.setName(name);
				} catch (final IOException e) {
					e.printStackTrace();
				} catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
					throw new RuntimeException(e);
				}
			} else {
				toolBar.addSeparator();
			}
		});
		container.add(toolBar, name);
		return toolBar;
	}

	void addSubToolbars(final JPanel mainPanel) {
		CardLayout subToolbarsCardLayout = new CardLayout();
		subToolbarPanel = new JPanel(subToolbarsCardLayout);
		mainPanel.add(subToolbarPanel, BorderLayout.PAGE_START);
		Arrays.stream(SubToolbarsDefinitions.values()).forEach(sub ->
				createToolBar(sub.getButtons(), subToolbarPanel, sub.name()).add(Box.createHorizontalGlue()));
		subToolbarsCardLayout.show(subToolbarPanel, SubToolbarsDefinitions.TILES.name());
	}

	public void updateSubToolbar(final EditorMode mode) {
		CardLayout subToolbarLayout = (CardLayout) subToolbarPanel.getLayout();
		Optional.ofNullable(SubToolbarsDefinitions.findByMode(mode))
				.ifPresentOrElse(sub -> subToolbarLayout.show(subToolbarPanel, sub.name()),
						() -> subToolbarLayout.show(subToolbarPanel, SubToolbarsDefinitions.EMPTY.name()));
	}
}
