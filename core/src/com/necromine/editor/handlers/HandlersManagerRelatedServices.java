package com.necromine.editor.handlers;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.MapManagerEventsNotifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HandlersManagerRelatedServices {
	private final GameAssetsManager assetsManager;
	private final MapManagerEventsNotifier eventsNotifier;

}
