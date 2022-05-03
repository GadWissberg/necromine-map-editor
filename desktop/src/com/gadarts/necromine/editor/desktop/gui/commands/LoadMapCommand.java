package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import static javax.swing.SwingUtilities.getWindowAncestor;

public class LoadMapCommand extends MapperCommand {
	public LoadMapCommand(PersistenceManager persistenceManager,
						  GuiEventsSubscriber guiEventsSubscriber,
						  ModesHandler modesHandler) {
		super(persistenceManager, guiEventsSubscriber, modesHandler);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(getWindowAncestor((Component) e.getSource())) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			tryOpeningFile(file, e);
		}
	}

	private void tryOpeningFile(final File file, ActionEvent e) {
		try {
			getGuiEventsSubscriber().onLoadMapRequested(file.getPath());
			getPersistenceManager().updateCurrentlyOpenedFile(file);
		} catch (final IOException error) {
			error.printStackTrace();
			Window windowAncestor = getWindowAncestor((Component) e.getSource());
			JOptionPane.showMessageDialog(windowAncestor, "Failed to open map file!");
		}
	}

}
