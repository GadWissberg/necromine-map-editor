package com.gadarts.necromine.editor.desktop.gui.commands.mode;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.gadarts.necromine.editor.desktop.gui.managers.ToolbarsManager;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.ViewModes;
import com.necromine.editor.mode.tools.EditorTool;

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
		getManagers().getModesManager().applyMode(getMode());
		ToolbarsManager toolbarsManager = getManagers().getToolbarsManager();
		toolbarsManager.updateSubToolbar(getMode());
		EditorTool latestTool = toolbarsManager.getLatestSelectedToolPerMode(getMode());
		getManagers().getEntitiesSelectionPanelManager().changeEntitiesSelectionModePerTool(getMode(), latestTool);
		updateTool();
		notifyRenderer();
	}

	private void notifyRenderer() {
		if (getMode() instanceof EditModes editModes) {
			getMapRenderer().onEditModeSet(editModes);
		} else if (getMode() instanceof ViewModes viewModes) {
			getMapRenderer().onViewModeSet(viewModes);
		}
	}

	private void updateTool() {
		Managers managers = getManagers();
		ButtonGroup buttonGroup = managers.getToolbarsManager().getButtonGroups().get(getMode().name());
		Optional.ofNullable(buttonGroup).ifPresent(b ->
				getMapRenderer().onToolSet(managers.getToolbarsManager().getLatestSelectedToolPerMode(getMode())));
	}

	protected abstract EditorMode getMode();
}
