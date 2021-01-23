package com.necromine.editor.actions;

import com.necromine.editor.Tile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MappingAction {
	final Tile[][] map;

	abstract void execute();

	public abstract boolean isProcess();
}
