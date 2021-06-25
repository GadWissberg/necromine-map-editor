package com.necromine.editor.handlers;

import com.necromine.editor.handlers.action.ActionsHandler;

public interface HandlersManager {
	CursorHandler getCursorHandler( );

	BatchHandler getBatchHandler( );

	ViewAuxHandler getViewAuxHandler( );

	ActionsHandler getActionsHandler( );
}
