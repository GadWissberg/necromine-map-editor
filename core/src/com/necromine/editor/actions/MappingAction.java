package com.necromine.editor.actions;

import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MappingAction {
	protected final GameMap map;

	public abstract void execute(MapManagerEventsNotifier eventsNotifier);

	public abstract boolean isProcess();
}
