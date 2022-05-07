package com.necromine.editor.actions.processes;


import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.GameMap;
import com.necromine.editor.model.node.FlatNode;
import lombok.Getter;

@Getter
public abstract class MappingProcess<T extends MappingProcess.FinishProcessParameters> extends MappingAction {
	final FlatNode srcNode;
	final boolean requiresRegionSelectionCursor;

	public MappingProcess(final GameMap map,
						  final FlatNode srcNode,
						  final boolean requiresRegionSelectionCursor) {
		super(map);
		this.srcNode = srcNode;
		this.requiresRegionSelectionCursor = requiresRegionSelectionCursor;
	}

	@Override
	public abstract void execute(MapEditorEventsNotifier eventsNotifier);

	abstract void finish(T params);

	public abstract static class FinishProcessParameters {
	}
}
