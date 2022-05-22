package com.gadarts.necromine.editor.desktop.gui.commands.mode;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.ViewModes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public abstract class SetModeCommand extends MapperCommand {
	protected SetModeCommand(com.necromine.editor.MapRenderer mapRenderer,
							 Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		applyMode();
		getManagers().getToolbarsManager().onSetModeCommandInvoked(this);
	}

	private void applyMode() {
		if (ModesManager.getSelectedMode() == getMode()) return;
		EditorMode mode = getMode();
		getManagers().getModesManager().applyMode(mode);
		getManagers().getToolbarsManager().updateSubToolbar(mode);
		getManagers().getEntitiesSelectionPanelManager().changeEntitiesSelectionMode(mode);
		updateTool(mode);
		notifyRenderer(mode);
	}

	private void notifyRenderer(EditorMode mode) {
		if (mode instanceof EditModes editModes) {
			getMapRenderer().onEditModeSet(editModes);
		} else if (mode instanceof ViewModes viewModes) {
			getMapRenderer().onViewModeSet(viewModes);
		}
	}

	private void updateTool(EditorMode mode) {
		Managers managers = getManagers();
		ButtonGroup buttonGroup = managers.getToolbarsManager().getButtonGroups().get(mode.name());
		Optional.ofNullable(buttonGroup).ifPresent(b ->
				getMapRenderer().onToolSet(managers.getToolbarsManager().getLatestSelectedToolPerMode(mode)));
	}

	protected abstract EditorMode getMode();
}
