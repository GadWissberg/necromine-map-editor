package com.necromine.editor.actions.processes;


import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.FlatNode;
import lombok.Getter;

@Getter
public abstract class MappingProcess<T extends MappingProcess.FinishProcessParameters> extends MappingAction {
    final FlatNode srcNode;

    public MappingProcess(final GameMap map, final FlatNode srcNode) {
        super(map);
        this.srcNode = srcNode;
    }

    @Override
	public abstract void execute(MapManagerEventsNotifier eventsNotifier);

    abstract void finish(T params);

    public abstract static class FinishProcessParameters {
    }
}
