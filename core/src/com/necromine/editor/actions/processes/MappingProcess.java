package com.necromine.editor.actions.processes;


import com.necromine.editor.MapNode;
import com.necromine.editor.actions.MappingAction;

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
