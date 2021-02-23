package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapNode;
import com.necromine.editor.Node;
import com.necromine.editor.actions.MappingAction;

import java.util.Optional;

public class LiftTileAction extends MappingAction {
	public static final float STEP = 0.1F;
	private final Node node;
	private final int direction;
	private final Model tileModel;

	public LiftTileAction(final Model tileModel, final GameMap map, final Node node, final int direction) {
		super(map);
		this.node = node;
		this.direction = direction;
		this.tileModel = tileModel;
	}

	@Override
	protected void execute() {
		int row = node.getRow();
		int col = node.getCol();
		MapNode[][] tiles = map.getTiles();
		Optional.ofNullable(tiles[row][col]).ifPresent(n -> {
			n.lift(direction * STEP);
			MapNode northMapNode = tiles[row - 1][col];
			if (northMapNode != null) {
				if (northMapNode.getSouthWall() == null) {
					ModelInstance northWall = new ModelInstance(tileModel);
					n.setNorthWall(northWall);
				}
			}
		});
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
