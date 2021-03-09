package com.necromine.editor.actions;

import com.necromine.editor.GameMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MappingAction {
	protected final GameMap map;

	public abstract void execute();

	public abstract boolean isProcess();
}
