package com.jannetta.certify.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.jannetta.certify.controller.Globals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Initialise the globals
	 */
	Globals globals = Globals.getInstance();

	/**
	 * Naming convention: pn - pane lbl - lable btn - button
	 * 
	 */
	static MainTabbedPane pn_main = new MainTabbedPane();
	static MenuBar menuBar = new MenuBar();
	private Logger logger = LoggerFactory.getLogger(getClass());

	public MainFrame() {
		super("Certify");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}

		// TODO FIX THIS
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		try {
			Image icon = toolkit.getImage(ClassLoader.getSystemResource("Certify.png"));
			//URL resource = getClass().getClassLoader().getResource("Certify1.png");
			//BufferedImage image = ImageIO.read(resource);
			setIconImage(icon);
		} catch (NullPointerException e) {
			logger.error("Certify.png not found.");
		}
		setJMenuBar(menuBar);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closer();
				System.exit(0);
			}
		});

		setContentPane(pn_main);
		pack();
		setVisible(true);
		setSize(1024, 768);
	}

	private void closer() {
		logger.debug("learners file: " + globals.isLearnersSaved());
		if (!globals.isLearnersSaved()) {
			int result1 = JOptionPane.showConfirmDialog(this, "Do you want to save learners before exiting Certify",
					"Exit Certify?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result1 == JOptionPane.YES_OPTION) {
				logger.trace("Save learner file");
				globals.saveJSON(globals.getProperty("learnerfile"), globals.getAllLearners());
				globals.setLearnersSaved(true);
			} else if (result1 == JOptionPane.NO_OPTION) {
				logger.trace("Quit without saving learners");
			} else {
				logger.trace("Cancel quitting");
			}

		}
		logger.debug("workshops file: " + globals.isWorkshopsSaved());
		if (!globals.isWorkshopsSaved()) {
			int result2 = JOptionPane.showConfirmDialog(this, "Do you want to save workshops before exiting Certify",
					"Exit Certify?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result2 == JOptionPane.YES_OPTION) {
				logger.trace("Save workshop file");
				globals.saveJSON(globals.getProperty("workshopfile"), globals.getWorkshops());
				globals.setWorkshopsSaved(true);
			} else if (result2 == JOptionPane.NO_OPTION) {
				logger.trace("Quit without saving workshops");
			} else {
				logger.trace("Cancel quitting");
			}
		}
		logger.debug("lessons file: " + globals.isLessonsSaved());
		if (!globals.isLessonsSaved()) {
			int result2 = JOptionPane.showConfirmDialog(this, "Do you want to save lessons before exiting Certify",
					"Exit Certify?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result2 == JOptionPane.YES_OPTION) {
				logger.trace("Save lessons file");
				globals.saveJSON(globals.getProperty("lessonfile"), globals.getLessons());
				globals.setLessonsSaved(true);
			} else if (result2 == JOptionPane.NO_OPTION) {
				logger.trace("Quit without saving lessons");
			} else {
				logger.trace("Cancel quitting");
			}

		}
	}
}
