package com.gadarts.necromine.editor.desktop.tree;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public abstract class EditorTreeCellRenderer extends DefaultTreeCellRenderer {
	public EditorTreeCellRenderer() {
		setBackgroundSelectionColor(Color.BLUE);
	}
}
