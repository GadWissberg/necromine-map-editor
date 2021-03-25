package com.necromine.editor.model.node;

import com.gadarts.necromine.model.MapNodeData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Node {
	private final int row;
	private final int col;
	private final float height;

	public Node(final int row, final int col) {
		this.row = row;
		this.col = col;
		this.height = 0;
	}

	public Node(final MapNodeData node) {
		this.row = node.getRow();
		this.col = node.getCol();
		this.height = node.getHeight();
	}

	public boolean equals(final int row, final int col) {
		return this.row == row && this.col == col;
	}

	public boolean equals(final Node node) {
		return equals(node.getRow(), node.getCol());
	}

	public boolean equals(final MapNodeData node) {
		return equals(node.getRow(), node.getCol());
	}
}
