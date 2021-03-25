package com.necromine.editor.actions.types;

import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.Node;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.stream.IntStream;

@Setter(AccessLevel.PACKAGE)
public class LiftTilesAction extends MappingAction {


	private final Parameters params;

	public LiftTilesAction(final GameMap map, final Parameters params) {
		super(map);
		this.params = params;
	}


	@Override
	public void execute(final MapManagerEventsNotifier eventsNotifier) {
		int minRow = Math.min(params.getSrcNode().getRow(), params.getDstNode().getRow());
		int minCol = Math.min(params.getSrcNode().getCol(), params.getDstNode().getCol());
		int maxRow = Math.max(params.getSrcNode().getRow(), params.getDstNode().getRow());
		int maxCol = Math.max(params.getSrcNode().getCol(), params.getDstNode().getCol());
		MapNodeData[][] t = map.getNodes();
		IntStream.rangeClosed(minRow, maxRow).forEach(row ->
				IntStream.rangeClosed(minCol, maxCol).forEach(col ->
						Optional.ofNullable(t[row][col]).ifPresent(n -> n.applyHeight(params.getValue()))));
		IntStream.rangeClosed(minRow, maxRow).forEach(row ->
				IntStream.rangeClosed(minCol, maxCol).forEach(col ->
						Optional.ofNullable(t[row][col]).ifPresent(n -> adjustWalls(t, row, col, n))));
	}

	private void adjustWalls(final MapNodeData[][] t, final int row, final int col, final MapNodeData n) {
		if (row > 0) {
			Optional.ofNullable(t[row - 1][col]).ifPresent(north -> params.getWallCreator().adjustNorthWall(n, north));
		}
		if (col < t[0].length - 1) {
			Optional.ofNullable(t[row][col + 1]).ifPresent(east -> params.getWallCreator().adjustEastWall(n, east));
		}
		if (row < t.length - 1) {
			Optional.ofNullable(t[row + 1][col]).ifPresent(south -> params.getWallCreator().adjustSouthWall(n, south));
		}
		if (col > 0) {
			Optional.ofNullable(t[row][col - 1]).ifPresent(west -> params.getWallCreator().adjustWestWall(n, west));
		}
	}


	@Override
	public boolean isProcess() {
		return false;
	}

	@RequiredArgsConstructor
	@Getter
	public static class Parameters {
		private final Node srcNode;
		private final Node dstNode;
		private final float value;
		private final WallCreator wallCreator;
	}
}
