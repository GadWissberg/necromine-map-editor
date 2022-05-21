package com.necromine.editor.handlers.action;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.handlers.SelectionHandler;

public record ActionHandlerRelatedServices(CursorHandler cursorHandler,
										   WallCreator wallCreator,
										   MapEditorEventsNotifier eventsNotifier,
										   GameAssetsManager assetsManager,
										   SelectionHandler selectionHandler) {
}
