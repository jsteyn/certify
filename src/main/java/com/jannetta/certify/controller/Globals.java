package com.jannetta.certify.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Scanner;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.jannetta.certify.model.Learner;
import com.jannetta.certify.model.LearnerTableModel;
import com.jannetta.certify.model.Learners;
import com.jannetta.certify.model.Lesson;
import com.jannetta.certify.model.LessonTableModel;
import com.jannetta.certify.model.Workshop;
import com.jannetta.certify.model.Lessons;
import com.jannetta.certify.model.WorkshopComboBoxModel;
import com.jannetta.certify.model.WorkshopTableModel;
import com.jannetta.certify.model.Workshops;

/**
 * A singleton that can be access from anywhere
 * 
 * @author jannetta
 *
 */
public class Globals {
	private static Logger logger = LoggerFactory.getLogger(Globals.class);
	private String badges[] = { "swc", "dc", "lc" };

	private static Globals globals = null;
	private static Workshops workshops = new Workshops();
	private static Learners all_learners = new Learners();
	private static Learners learners = all_learners;
	private static Lessons lessons = new Lessons();
	private static GsonBuilder gsonBuilder = new GsonBuilder();
	private static Gson gson = gsonBuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
	private static Properties properties;
	private static String propertyfile = "system.properties";
	private static String workshopfile = "Workshops.json";
	private static String learnerfile = "Learners.json";
	private static String lessonfile = "Lessons.json";
	private static WorkshopTableModel workshopTableModel = new WorkshopTableModel();
	private static LearnerTableModel learnerTableModel = new LearnerTableModel();
	private static WorkshopComboBoxModel workshopComboBoxModel;
	private static LessonTableModel lessonTableModel = new LessonTableModel();
	private boolean workshopssaved = true;
	private boolean learnerssaved = true;
	private boolean lessonssaved = true;

	public static Globals getInstance() {
		if (globals == null) {
			globals = new Globals();

			checkFile(propertyfile); // If the file doesn't exist create it

			properties = loadProperties();

			// Load data from files
			workshops = loadWorkshops(workshops);
			lessons = loadLessons(lessons);
			all_learners = loadLearners(learners);
			learners = all_learners;

			workshopComboBoxModel = new WorkshopComboBoxModel(globals.getWorkshops().getWorkshopNames());
			workshopTableModel.setWorkshops(workshops);
			learnerTableModel.setLearners(learners);
			lessonTableModel.setLessons(lessons);
		}
		return globals;
	}

	public void learnersSetView(String view) {
		learners = new Learners();
		if (view.equals("All")) {
			learners = all_learners;
		} else {
			if (!(all_learners.getLearners() == null))
			all_learners.getLearners().forEach(learner -> {
				if (learner.getWorkshop().equals(view)) {
					learners.add(learner);
				}
			});
		}
		learnerTableModel.setLearners(learners);
		fireTableDataChanged();
	}

	private static Workshops loadWorkshops(Workshops ws) {
		try {
			logger.trace("Loading " + globals.getProperty("workshopfile"));
			checkFile(globals.getProperty("workshopfile")); // If the file doesn't exist create it
			Reader reader = Files.newBufferedReader(Paths.get(globals.getProperty("workshopfile")));
			ws = gson.fromJson(reader, Workshops.class);
			if (ws == null) {
				ws = new Workshops();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ws;
	}

	private static Learners loadLearners(Learners lrns) {
		try {
			logger.trace("Loading " + globals.getProperty("learnerfile"));
			checkFile(globals.getProperty("learnerfile")); // If the file doesn't exist create it
			Reader reader = Files.newBufferedReader(Paths.get(globals.getProperty("learnerfile")));
			lrns = gson.fromJson(reader, Learners.class);
			if (lrns == null) {
				lrns = new Learners();
			}
			reader.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return lrns;
	}

	private static Lessons loadLessons(Lessons lessons) {
		try {
			logger.trace("Loading " + globals.getProperty("lessonfile"));
			checkFile(globals.getProperty("lessonfile")); // If the file doesn't exist create it
			Reader reader = Files.newBufferedReader(Paths.get(globals.getProperty("lessonfile")));
			lessons = gson.fromJson(reader, Lessons.class);
			if (lessons == null) {
				lessons = new Lessons();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lessons;
	}

	private static void checkFile(String filename) {
		try {
			logger.debug("Check if file " + filename + " exists.");
			File f1 = new File(filename);
			f1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Load properties from system.properties file
	 */
	public static Properties loadProperties() {
		logger.trace("Load properties from " + propertyfile);
		properties = new Properties();
		try {
			File f = new File(propertyfile);
			// If the file doesn't exist, create it

			if (!(f.exists())) {
				OutputStream out = new FileOutputStream(f);
				logger.trace("Create system.properties file");
				out.close();
			}
			InputStream is = new FileInputStream(f);
			properties.load(is);
			// If there are no properties yet, set STORAGE to the default value of /upload
			if (properties.size() == 0) {
				logger.trace("Add basic properties");
				properties.setProperty("learnerfile", learnerfile);
				properties.setProperty("workshopfile", workshopfile);
				properties.setProperty("lessonfile", lessonfile);
			}
			FileOutputStream out = new FileOutputStream(propertyfile);
			properties.store(out, "");
			is.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Set the specified property with the specified value
	 * 
	 * @param property The property to set
	 * @param value    The value to set the property to
	 */
	public void setProperty(String property, String value) {
		properties.setProperty(property, value);
		File f = new File("system.properties");
		try {
			OutputStream out = new FileOutputStream(f);
			properties.store(out, "This is an optional header comment string");
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Get the specified property and return its value
	 * 
	 * @param property property to return
	 * @return property
	 */
	public String getProperty(String property) {
		if (properties == null) {
			loadProperties();
		}
		return properties.getProperty(property);
	}

	public Workshops getWorkshops() {
		return workshops;
	}

	public void setWorkshops(Workshops workshops) {
		Globals.workshops = workshops;
	}

	public Learners getLearners() {
		return learners;
	}

	public Learners getAllLearners() {
		if (all_learners == null)
		all_learners = new Learners();
		learners = all_learners;
		return all_learners;
	}

	public Learner findLearner(String userID) {
		for (int i = 0; i < all_learners.size(); i++) {
			if (all_learners.get(i).getUser_id().equals(userID)) {
				return all_learners.get(i);
			}
		}
		return null;
	}

	public Lesson findLesson(String lessonID) {
		for (int i = 0; i < lessons.size(); i++) {
			if (lessons.get(i).getLessonID().equals(lessonID)) {
				return lessons.get(i);
			}
		}
		return null;
	}

	public void setLearners(Learners learners) {
		Globals.learners = learners;
	}

	public void setAllLearners(Learners learners) {
		all_learners = learners;
	}

	public WorkshopTableModel getWorkshopTableModel() {
		return workshopTableModel;
	}

	public void setWorkshopTableModel(WorkshopTableModel workshopTableModel) {
		Globals.workshopTableModel = workshopTableModel;
	}

	public LearnerTableModel getLearnerTableModel() {
		return learnerTableModel;
	}

	public void setLearnerTableModel(LearnerTableModel learnerTableModel) {
		Globals.learnerTableModel = learnerTableModel;
	}

	public WorkshopComboBoxModel getWorkshopComboBoxModel() {
		return workshopComboBoxModel;
	}

	public static void setWorkshopComboBoxModel(WorkshopComboBoxModel workshopComboBoxModel) {
		Globals.workshopComboBoxModel = workshopComboBoxModel;
	}

	public void saveJSON(String filename, Object object) {
		try {
			Writer writer = new FileWriter(filename);
			gson.toJson(object, writer);
			logger.debug("Saving " + filename);
			writer.close();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isWorkshopssaved() {
		return workshopssaved;
	}

	public void setWorkshopssaved(boolean workshopssaved) {
		this.workshopssaved = workshopssaved;
	}

	public boolean isLearnerssaved() {
		return learnerssaved;
	}

	public void setLearnerssaved(boolean learnerssaved) {
		this.learnerssaved = learnerssaved;
	}

	public void setLessonssaved(boolean lessonssaved) {
		logger.debug("Set lessons saved to " + lessonssaved);
		this.lessonssaved = lessonssaved;
	}

	public boolean isLessonssaved() {
		return lessonssaved;
	}

	public String[] getBadges() {
		return badges;
	}

	public void setBadges(String[] badges) {
		this.badges = badges;
	}

	public void fireTableDataChanged() {
		workshopTableModel.fireTableDataChanged();
		logger.trace("Update workshop table model");
		learnerTableModel.fireTableDataChanged();
		logger.trace("Update learner table model");
		lessonTableModel.fireTableDataChanged();
		logger.trace("Update lesson table model");
	}

	public static void svg2pdf(String certificate, String userID) {
		File outputFile;
		File f_svgFile = new File(certificate);
		try {
			outputFile = File.createTempFile("result-", ".pdf");
			SVGConverter converter = new SVGConverter();
			converter.setDestinationType(DestinationType.PDF);
			converter.setSources(new String[] { f_svgFile.toString() });
			converter.setDst(outputFile);
			converter.execute();
			Path copied = Paths.get(userID + ".pdf");
			Files.copy(outputFile.toPath(), copied);
			Files.delete(outputFile.toPath());
			Files.delete(f_svgFile.toPath());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (SVGConverterException e) {
			logger.error(e.getMessage());
		}

	}

	private boolean inUse(String workshop, Learners learners) {
		for (int l = 0; l < learners.size(); l++) {
			if (learners.get(l).getWorkshop().equals(workshop))
				return true;
		}

		return false;
	}

	public int delWorkshops(Learners learners) {
		Workshops workshops = globals.getWorkshops();
		ArrayList<Workshop> delWorkshops = new ArrayList<Workshop>();
		for (int l = 0; l < workshops.size(); l++) {
			if (workshops.get(l).isPrint()) {
				if (inUse(workshops.get(l).getWorkshop_name(), learners) )
					return 1;
				delWorkshops.add(workshops.get(l));
			}
		}
		for (int l = 0; l < delWorkshops.size(); l++) {
			workshops.remove(delWorkshops.get(l));
			globals.setWorkshopssaved(false);
		}
		return 0;
	}

	public Learners delLearners(Learners learners) {
		ArrayList<Learner> delLearners = new ArrayList<Learner>();
		for (int l = 0; l < learners.size(); l++) {
			if (learners.get(l).isPrint()) {
				delLearners.add(learners.get(l));
			}
		}
		for (int l = 0; l < delLearners.size(); l++) {
			learners.remove(delLearners.get(l));
			globals.setLearnerssaved(false);
		}
		return learners;
	}

	public static Lessons delLessons(Lessons lessons) {
		ArrayList<Lesson> delLessons = new ArrayList<Lesson>();
		for (int i = 0; i < lessons.size(); i++) {
			if (lessons.get(i).isPrint()) {
				delLessons.add(lessons.get(i));
			}
		}
		for (int i = 0; i < delLessons.size(); i++) {
			lessons.remove(delLessons.get(i));
			globals.setLessonssaved(false);
		}
		return lessons;
	}

	
	public Learners readCSVLearnersFile(Learners learners, File filename, String workshop) {
		try {
			Scanner sc = new Scanner(filename);
			while (sc.hasNext()) {
				String line = sc.nextLine();
				logger.debug("Read line: " + line);
				String[] tokens = line.split(",");
				// workshop, instructor, user_id, name, email, date
				Learner learner = new Learner(workshop, tokens[0].split("-")[0], tokens[1], tokens[2], tokens[3], "",
						"", tokens[4], tokens[5], new Lessons());
				learners.add(learner);
			}
			sc.close();
			fireTableDataChanged();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return learners;
	}

	public Learners importTextFile(Learners learners, String workshop, String badge, String instructor, String date, File file) {
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				String line = sc.nextLine().strip();
				String[] tokens = line.split(",");
				date = (date.strip().equals("") ? now() : date);
				Learner learner = new Learner(workshop, badge, instructor, (tokens[0] + "_" + tokens[1]).replace(" ", "_"), tokens[0], "",
						tokens[1], tokens[2], date, new Lessons());
				learners.add(learner);
			}
			sc.close();
			fireTableDataChanged();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
		return learners;
	}


	public Lessons getLessons() {
		return lessons;
	}

	public void setLessons(Lessons lessons) {
		Globals.lessons = lessons;
	}

	public LessonTableModel getLessonTableModel() {
		return lessonTableModel;
	}
	
	public static String now() {
		String DATE_FORMAT_NOW = "dd MMM yyyy";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

}
