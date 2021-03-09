package com.necromine.editor.actions.types;

import com.gadarts.necromine.WallCreator;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.Node;

public final class ActionBuilder {
	private static final ActionBuilder builder = new ActionBuilder();
	private static MappingAction current;

	public static ActionBuilder begin(final GameMap map) {
		current = new LiftTilesAction(map);
		return builder;
	}

	public MappingAction finish() {
		MappingAction result = ActionBuilder.current;
		current = null;
		return result;
	}

	public ActionBuilder liftTiles(final Node src, final Node dst, final float value, final WallCreator wallCreator) {
		LiftTilesAction action = (LiftTilesAction) current;
		action.setSrcNode(src);
		action.setDstNode(dst);
		action.setValue(value);
		action.setWallCreator(wallCreator);
		return this;
	}
}
