package com.necromine.editor.actions.types;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.Node;

public final class ActionBuilder {
	private static final ActionBuilder builder = new ActionBuilder();
	private static MappingAction current;

	public static ActionBuilder begin(final GameMap map, final GameAssetsManager assetsManager) {
		current = new LiftTileAction(map, assetsManager);
		return builder;
	}

	public ActionBuilder liftTile(final Node node,
								  final int direction,
								  final WallCreator wallCreator) {
		LiftTileAction current = (LiftTileAction) ActionBuilder.current;
		current.setNode(node);
		current.setDirection(direction);
		current.setWallCreator(wallCreator);
		return builder;
	}

	public MappingAction finish() {
		MappingAction result = ActionBuilder.current;
		current = null;
		return result;
	}
}
