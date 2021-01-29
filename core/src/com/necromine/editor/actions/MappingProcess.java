package com.necromine.editor.actions;


import com.necromine.editor.MapNode;

public abstract class MappingProcess<T extends MappingProcess.FinishProcessParameters> extends MappingAction {

	public MappingProcess(final MapNode[][] map) {
		super(map);
	}

	@Override
	protected abstract void execute();

	abstract void finish(T params);

	public abstract static class FinishProcessParameters {
	}
}
