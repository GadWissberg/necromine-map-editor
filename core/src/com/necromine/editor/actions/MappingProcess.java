package com.necromine.editor.actions;


import com.necromine.editor.Tile;

public abstract class MappingProcess<T extends MappingProcess.FinishProcessParameters> extends MappingAction {

	public MappingProcess(final Tile[][] map) {
		super(map);
	}

	@Override
	abstract void execute();

	abstract void finish(T params);

	public abstract static class FinishProcessParameters {
	}
}
