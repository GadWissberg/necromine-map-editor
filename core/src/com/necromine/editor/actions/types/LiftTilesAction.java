package com.necromine.editor.actions.types;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.Node;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.Optional;
import java.util.stream.IntStream;

@Setter(AccessLevel.PACKAGE)
public class LiftTilesAction extends MappingAction {

	private Node srcNode;
	private Node dstNode;
	private float value;
	private WallCreator wallCreator;

	public LiftTilesAction(final GameMap map) {
		super(map);
	}


	@Override
	public void execute() {
		int minRow = Math.min(srcNode.getRow(), dstNode.getRow());
		int minCol = Math.min(srcNode.getCol(), dstNode.getCol());
		int maxRow = Math.max(srcNode.getRow(), dstNode.getRow());
		int maxCol = Math.max(srcNode.getCol(), dstNode.getCol());
		MapNodeData[][] t = map.getNodes();
		IntStream.rangeClosed(minRow, maxRow).forEach(row ->
				IntStream.rangeClosed(minCol, maxCol).forEach(col ->
						Optional.ofNullable(t[row][col]).ifPresent(n -> {
							n.applyHeight(value);
							adjustWalls(t, row, col, n);
						})));
	}

	private void adjustWalls(final MapNodeData[][] t, final int row, final int col, final MapNodeData n) {
		if (row > 0) {
			Optional.ofNullable(t[row - 1][col]).ifPresent(north -> wallCreator.adjustNorthWall(n, north));
		}
		if (col < t[0].length - 1) {
			Optional.ofNullable(t[row][col + 1]).ifPresent(east -> wallCreator.adjustEastWall(n, east));
		}
		if (row < t.length - 1) {
			Optional.ofNullable(t[row + 1][col]).ifPresent(south -> wallCreator.adjustSouthWall(n, south));
		}
		if (col > 0) {
			Optional.ofNullable(t[row][col - 1]).ifPresent(west -> wallCreator.adjustWestWall(n, west));
		}
	}


	@Override
	public boolean isProcess() {
		return false;
	}
}
