package com.necromine.editor.actions.processes;

import com.necromine.editor.MapManagerEventsNotifier;
import lombok.Getter;

@Getter
public class SelectTilesForWallTilingFinishProcessParameters extends SelectTilesFinishProcessParameters {
	private final MapManagerEventsNotifier notifier;

	public SelectTilesForWallTilingFinishProcessParameters(final int dstRow,
														   final int dstCol,
														   final MapManagerEventsNotifier notifier) {
		super(dstRow, dstCol);
		this.notifier = notifier;
	}
}

