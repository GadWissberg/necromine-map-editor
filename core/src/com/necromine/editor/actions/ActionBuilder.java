package com.necromine.editor.actions;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.types.LiftTileAction;

public final class ActionBuilder {
	private static final ActionBuilder builder = new ActionBuilder();
	private static MappingAction current;

	public static ActionBuilder begin(final GameMap map, final GameAssetsManager assetsManager) {
		current = new LiftTileAction(map, assetsManager);
		return builder;
	}

	public MappingAction finish() {
		MappingAction result = ActionBuilder.current;
		current = null;
		return result;
	}
}
