package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.MapRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

import static com.gadarts.necromine.editor.desktop.gui.Gui.PROGRAM_TILE;
import static javax.swing.SwingUtilities.getWindowAncestor;

public class SaveMapCommand extends MapperCommand {
	public SaveMapCommand(PersistenceManager persistenceManager,
						  MapRenderer mapRenderer,
						  ModesManager modesManager,
						  DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		File file = getPersistenceManager().getCurrentlyOpenedMap();
		Window windowAncestor = getWindowAncestor((Component) e.getSource());
		if (file == null) {
			JFileChooser chooser = new JFileChooser();
			if (chooser.showSaveDialog(windowAncestor) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			}
		}
		Optional.ofNullable(file).ifPresent(f -> {
			try {
				getMapRenderer().onSaveMapRequested(f.getPath());
				getPersistenceManager().updateCurrentlyOpenedFile(f);
				String message = String.format("Map was saved successfully to: %s", f.getPath());
				JOptionPane.showMessageDialog(
						windowAncestor,
						message,
						PROGRAM_TILE,
						JOptionPane.INFORMATION_MESSAGE);
			} catch (NullPointerException error) {
				JOptionPane.showMessageDialog(
						windowAncestor,
						"Failed to save map file!",
						PROGRAM_TILE,
						JOptionPane.ERROR_MESSAGE);
			}
		});

	}
}
