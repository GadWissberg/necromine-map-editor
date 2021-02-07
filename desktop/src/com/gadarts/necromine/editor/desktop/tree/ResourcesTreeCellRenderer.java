package com.gadarts.necromine.editor.desktop.tree;

import com.gadarts.necromine.editor.desktop.MapperWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ResourcesTreeCellRenderer extends EditorTreeCellRenderer {

	private static final String ICON_UNDEFINED = "undefined";
	private static final String ICON_SECTION = "section";
	private static final String ICON_FOLDER = "folder";
	private static final String FOLDER_NAME = "icons";
	private final String entryIcon;

	public ResourcesTreeCellRenderer(final String entryIcon) {
		this.entryIcon = entryIcon;
	}

	@Override
	public Component getTreeCellRendererComponent(final JTree tree,
												  final Object value,
												  final boolean selected,
												  final boolean expanded,
												  final boolean leaf,
												  final int row,
												  final boolean hasFocus) {
		JLabel result = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		final String UNDEFINED = String.format(MapperWindow.UI_ASSETS_FOLDER_PATH, FOLDER_NAME, ICON_UNDEFINED);
		String text = node.getUserObject().toString();
		String icon;
		if (tree.getModel().getRoot().equals(node)) {
			icon = ICON_SECTION;
		} else if (!node.isLeaf()) {
			icon = ICON_FOLDER;
		} else {
			icon = entryIcon;
		}
		result.setText(text);
		try {
			String iconPath = String.format(MapperWindow.UI_ASSETS_FOLDER_PATH, FOLDER_NAME, icon);
			result.setIcon(new ImageIcon(ImageIO.read(new File(iconPath))));
		} catch (final IOException e) {
			try {
				result.setIcon(new ImageIcon(ImageIO.read(new File(UNDEFINED))));
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
