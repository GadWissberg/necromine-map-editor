package com.necromine.editor;

import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.node.NodeWallsDefinitions;

import java.util.HashSet;
import java.util.Set;

public class MapManagerEventsNotifier {
    private final Set<MapManagerEventsSubscriber> subscribers = new HashSet<>();

    public void tileSelectedUsingWallTilingTool(final int row, final int col, final NodeWallsDefinitions definitions) {
        subscribers.forEach(subscriber -> subscriber.onTileSelectedUsingWallTilingTool(row, col, definitions));
    }

    public void subscribeForEvents(final MapManagerEventsSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void tilesSelectedForLifting(final Node src, final int dstRow, final int dstCol) {
        subscribers.forEach(subscriber -> subscriber.onTilesSelectedForLifting(src.getRow(), src.getRow(), dstRow, dstCol));
    }
}
