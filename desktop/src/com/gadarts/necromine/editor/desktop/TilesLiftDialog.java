package com.gadarts.necromine.editor.desktop;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TilesLiftDialog extends DialogPane {
    private final int srcRow;
    private final int srcCol;
    private final int dstRow;
    private final int dstCol;

    @Override
    String getDialogTitle() {
        return "Lift Tiles";
    }
}
