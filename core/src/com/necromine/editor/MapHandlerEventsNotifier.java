package com.necromine.editor;

import java.util.HashSet;
import java.util.Set;

public class MapHandlerEventsNotifier {
	private final Set<MapHandlerEventsSubscriber> subscribers = new HashSet<>();

	public void tileSelectedUsingWallTilingTool(final int row, final int col) {
		subscribers.forEach(subscriber -> subscriber.onTileSelectedUsingWallTilingTool(row, col));
	}

	public void subscribeForEvents(MapHandlerEventsSubscriber subscriber) {
		subscribers.add(subscriber);
	}
}
