package com.gadarts.necromine.editor.desktop.gui.managers;

import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.editor.desktop.dialogs.DialogPane;
import com.necromine.editor.MapRenderer;

import javax.swing.*;

public class DialogsManager extends BaseManager {
	private final JFrame parentWindow;

	public DialogsManager(MapRenderer mapRenderer, JFrame parentWindow) {
		super(mapRenderer);
		this.parentWindow = parentWindow;
	}

	public void openDialog(DialogPane dialog) {
		GuiUtils.openNewDialog(parentWindow, dialog);
	}
}
