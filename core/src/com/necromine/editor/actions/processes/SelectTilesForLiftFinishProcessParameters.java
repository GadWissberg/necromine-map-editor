package com.necromine.editor.actions.processes;

import com.necromine.editor.MapEditorEventsNotifier;
import lombok.Getter;

@Getter
public class SelectTilesForLiftFinishProcessParameters extends SelectTilesFinishProcessParameters {
	private final MapEditorEventsNotifier notifier;

	public SelectTilesForLiftFinishProcessParameters(final int dstRow,
													 final int dstCol,
													 final MapEditorEventsNotifier notifier) {
		super(dstRow, dstCol);
		this.notifier = notifier;
	}
}

