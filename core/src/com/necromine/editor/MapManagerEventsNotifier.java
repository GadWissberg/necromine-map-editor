package com.necromine.editor;

import java.util.HashSet;
import java.util.Set;

public class MapManagerEventsNotifier {
	private final Set<MapManagerEventsSubscriber> subscribers = new HashSet<>();

	public void tileSelectedUsingWallTilingTool(final int row, final int col) {
		subscribers.forEach(subscriber -> subscriber.onTileSelectedUsingWallTilingTool(row, col));
	}

	public void subscribeForEvents(final MapManagerEventsSubscriber subscriber) {
		subscribers.add(subscriber);
	}
}
