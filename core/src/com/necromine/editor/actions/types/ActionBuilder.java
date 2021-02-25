package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.GameMap;
import com.necromine.editor.Node;
import com.necromine.editor.actions.MappingAction;

public final class ActionBuilder {
	private static final ActionBuilder builder = new ActionBuilder();
	private static MappingAction current;

	public static ActionBuilder begin(final GameMap map) {
		current = new LiftTileAction(map);
		return builder;
	}

	public ActionBuilder liftTile(final Node node,
								  final int direction,
								  final Model wallModel,
								  final GameAssetsManager assetsManager) {
		LiftTileAction current = (LiftTileAction) ActionBuilder.current;
		current.setNode(node);
		current.setDirection(direction);
		current.setWallModel(wallModel);
		current.setAssetsManager(assetsManager);
		return builder;
	}

	public MappingAction finish() {
		MappingAction result = ActionBuilder.current;
		current = null;
		return result;
	}
}
