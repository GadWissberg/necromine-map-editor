package com.gadarts.necromine.editor.desktop.gui;

import com.gadarts.necromine.model.ElementDefinition;

public record TreeSection(String header, ElementDefinition[] definitions, String entryIcon) {
}
