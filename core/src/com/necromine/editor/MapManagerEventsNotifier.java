package com.necromine.editor;

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
}
