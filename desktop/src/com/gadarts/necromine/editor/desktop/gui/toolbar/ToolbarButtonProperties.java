package com.gadarts.necromine.editor.desktop.gui.toolbar;

import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static com.gadarts.necromine.editor.desktop.gui.Gui.FOLDER_TOOLBAR_BUTTONS;

@Getter
public class ToolbarButtonProperties {
	private static final String FOLDER_ASSETS = "core" + File.separator + "assets";
	private static final String ICON_FORMAT = ".png";

	public static final String UI_ASSETS_FOLDER_PATH = FOLDER_ASSETS + File.separator + "%s" + File.separator + "%s"
			+ ICON_FORMAT;

	private final String icon;
	private final String toolTip;
	private final MenuItemDefinition menuItemDefinition;
	private String buttonGroup;
	private MapperCommand mapperCommand;

	public ToolbarButtonProperties(String icon,
								   String toolTip,
								   MapperCommand mapperCommand) {
		this(icon, toolTip, mapperCommand, null);
	}

	public ToolbarButtonProperties(final String icon,
								   final String toolTip,
								   final MapperCommand mapperCommand,
								   final String buttonGroup) {
		this(icon, toolTip, (MenuItemDefinition) null);
		this.mapperCommand = mapperCommand;
		this.buttonGroup = buttonGroup;
	}

	public ToolbarButtonProperties(final String icon,
								   final String toolTip,
								   final MenuItemDefinition mapperMenuItemDefinition) {
		this.toolTip = toolTip;
		this.menuItemDefinition = mapperMenuItemDefinition;
		this.buttonGroup = null;
		this.icon = icon;
	}

	static ImageIcon createButtonIcon(ToolbarButtonProperties properties) throws IOException {
		String path = String.format(UI_ASSETS_FOLDER_PATH, FOLDER_TOOLBAR_BUTTONS, properties.getIcon());
		return new ImageIcon(ImageIO.read(new File(path)), properties.getToolTip());
	}
}
