package com.necromine.editor.handlers.action;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.MapEditorEventsNotifier;
import com.necromine.editor.handlers.CursorHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ActionHandlerRelatedServices {
	private final CursorHandler cursorHandler;
	private final WallCreator wallCreator;
	private final MapEditorEventsNotifier eventsNotifier;
	private final GameAssetsManager assetsManager;

}
