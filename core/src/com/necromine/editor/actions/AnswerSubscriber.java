package com.necromine.editor.actions;

public interface AnswerSubscriber<T> {
	void onAnswerGiven(T data);
}
