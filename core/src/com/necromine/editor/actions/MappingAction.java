package com.necromine.editor.actions;

import com.necromine.editor.MapNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MappingAction {
	final MapNode[][] map;

	protected abstract void execute();

	public abstract boolean isProcess();
}
