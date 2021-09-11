package com.necromine.editor.actions.processes;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.model.node.FlatNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class SelectTilesForLiftProcess extends MappingProcess<SelectTilesForLiftFinishProcessParameters> {
    private int direction;
    private WallCreator wallCreator;
    private Set<MapNodeData> initializedTiles;

    public SelectTilesForLiftProcess(final GameMap map, final FlatNode src) {
        super(map, src, true);
    }

    @Override
    public boolean isProcess() {
        return false;
    }

	@Override
	public void execute(final MapEditorEventsNotifier eventsNotifier) {

	}

    @Override
    public void finish(final SelectTilesForLiftFinishProcessParameters params) {
        params.getNotifier().tilesSelectedForLifting(srcNode, params.getDstRow(), params.getDstCol());
    }
}
