package com.necromine.editor.actions;

import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.model.GameMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MappingAction {
	protected final GameMap map;
	private boolean done;

	protected void actionDone() {
		done = true;
	}

	public abstract void execute(MapEditorEventsNotifier eventsNotifier);

	public abstract boolean isProcess();
}
