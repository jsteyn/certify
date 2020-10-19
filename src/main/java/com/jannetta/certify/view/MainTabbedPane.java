package com.jannetta.certify.view;

import javax.swing.JTabbedPane;

public class MainTabbedPane extends JTabbedPane{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainTabbedPane() {
		super();
		addTab("Workshops", new WorkshopPanel());
		addTab("Learners", new LearnerPanel());
		addTab("Lessons", new LessonPanel());
	}

}
