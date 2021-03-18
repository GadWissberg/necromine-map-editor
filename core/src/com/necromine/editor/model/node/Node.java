package com.necromine.editor.model.node;

import com.gadarts.necromine.model.MapNodeData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Node {
	private final int row;
	private final int col;
	private final float height;

	public Node(int row, int col) {
		this.row = row;
		this.col = col;
		this.height = 0;
	}

	public Node(MapNodeData node) {
		this.row = node.getRow();
		this.col = node.getCol();
		this.height = node.getHeight();
	}

	public boolean equals(final int row, final int col) {
		return this.row == row && this.col == col;
	}
}
