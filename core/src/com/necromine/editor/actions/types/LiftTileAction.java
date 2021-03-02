package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.Node;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.Optional;

@Setter(AccessLevel.PACKAGE)
public class LiftTileAction extends MappingAction {
	public static final float STEP = 0.1F;

	private Node node;
	private int direction;
	private WallCreator wallCreator;

	@Setter(AccessLevel.NONE)
	private GameAssetsManager assetsManager;

	public LiftTileAction(GameMap map, GameAssetsManager assetsManager) {
		super(map);
		this.assetsManager = assetsManager;
	}


	@Override
	protected void execute() {
		int row = node.getRow();
		int col = node.getCol();
		MapNodeData[][] tiles = map.getNodes();
		Optional.ofNullable(tiles[row][col]).ifPresent(selectedNode -> {
			selectedNode.lift(direction * STEP);
			Optional.ofNullable(tiles[row - 1][col]).ifPresent(north -> wallCreator.adjustNorthWall(selectedNode, north));
			Optional.ofNullable(tiles[row][col + 1]).ifPresent(east -> wallCreator.adjustEastWall(selectedNode, east));
			Optional.ofNullable(tiles[row + 1][col]).ifPresent(south -> wallCreator.adjustSouthWall(selectedNode, south));
			Optional.ofNullable(tiles[row][col - 1]).ifPresent(west -> wallCreator.adjustWestWall(selectedNode, west));
		});
	}


	@Override
	public boolean isProcess() {
		return false;
	}
}
