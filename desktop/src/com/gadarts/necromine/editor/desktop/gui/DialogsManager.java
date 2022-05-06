package com.gadarts.necromine.editor.desktop.gui;

import com.gadarts.necromine.editor.desktop.GuiUtils;
import com.gadarts.necromine.editor.desktop.dialogs.DialogPane;

import javax.swing.*;

public class DialogsManager {
	private final JFrame parentWindow;

	public DialogsManager(JFrame parentWindow) {
		this.parentWindow = parentWindow;
	}

	public void openDialog(DialogPane dialog) {
		GuiUtils.openNewDialog(parentWindow, dialog);
	}
}
