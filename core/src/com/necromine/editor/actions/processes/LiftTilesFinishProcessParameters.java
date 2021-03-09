package com.necromine.editor.actions.processes;

import com.necromine.editor.MapManagerEventsNotifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LiftTilesFinishProcessParameters extends MappingProcess.FinishProcessParameters {
    private final int dstRow;
    private final int dstCol;
    private final MapManagerEventsNotifier notifier;
}

