package com.necromine.editor.model.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Node {
	private final int row;
	private final int col;

	public boolean equals(final int row, final int col) {
		return this.row == row && this.col == col;
	}
}
