package com.necromine.editor.actions.processes;


import com.necromine.editor.GameMap;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.Node;
import lombok.Getter;

@Getter
public abstract class MappingProcess<T extends MappingProcess.FinishProcessParameters> extends MappingAction {
    final Node srcNode;

    public MappingProcess(final GameMap map, final Node srcNode) {
        super(map);
        this.srcNode = srcNode;
    }

    @Override
	public abstract void execute();

    abstract void finish(T params);

    public abstract static class FinishProcessParameters {
    }
}
