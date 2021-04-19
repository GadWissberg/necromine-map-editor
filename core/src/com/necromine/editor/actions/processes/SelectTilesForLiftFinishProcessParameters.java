package com.necromine.editor.actions.processes;

import com.necromine.editor.MapManagerEventsNotifier;
import lombok.Getter;

@Getter
public class SelectTilesForLiftFinishProcessParameters extends SelectTilesFinishProcessParameters {
	private final MapManagerEventsNotifier notifier;

	public SelectTilesForLiftFinishProcessParameters(final int dstRow,
													 final int dstCol,
													 final MapManagerEventsNotifier notifier) {
		super(dstRow, dstCol);
		this.notifier = notifier;
	}
}

