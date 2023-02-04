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
 * A singleton that can be accessed from anywhere
 *
 * @author jannetta
 */
public class Globals {
    private static final Logger logger = LoggerFactory.getLogger(Globals.class);

    private static final String version = "1.0";
    private String[] badges = {"", "swc", "dc", "lc", "pad"};
    private static final String sBadges = ",swc,dc,lc,pad";

    private static Globals globals = null;
    private static Workshops workshops = new Workshops();
    private static Learners all_learners = new Learners();
    private static Learners learners = all_learners;
    private static Lessons lessons = new Lessons();
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private static Properties properties;
    private static final String propertyFile = "system.properties";
    private static String configDirectory = "";
    private static String dataDirectory = "";
    private static String pdfDirectory = "";
    private static String workshopfile = "Workshops.json";
    private static String learnerfile = "Learners.json";
    private static String lessonfile = "Lessons.json";
    private static WorkshopTableModel workshopTableModel = new WorkshopTableModel();
    private static LearnerTableModel learnerTableModel = new LearnerTableModel();
    private static WorkshopComboBoxModel workshopComboBoxModel;
    private static final LessonTableModel lessonTableModel = new LessonTableModel();
    private boolean workshopsSaved = true;
    private boolean learnersSaved = true;
    private boolean lessonsSaved = true;

    public static Globals getInstance() {
        if (globals == null) {
            globals = new Globals();
            pdfDirectory = System.getProperty("user.home").concat("/").concat(".certify");
            configDirectory = System.getProperty("user.home").concat("/").concat(".certify");
            dataDirectory = System.getProperty("user.home");

            checkFile(configDirectory, propertyFile); // If the file doesn't exist create it

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
        logger.trace("View: " + view);
        learners = new Learners();
        if (view.equals("All") || view == null) {
            learners = all_learners;
        } else {
            if (!(all_learners.getLearners() == null))
                all_learners.getLearners().forEach(learner -> {
                    if (learner.getWorkshopID().equals(view)) {
                        learners.add(learner);
                    }
                });
        }
        learnerTableModel.setLearners(learners);
        fireTableDataChanged();
    }

    private static Workshops loadWorkshops(Workshops ws) {
        try {
            logger.trace("Loading " + globals.getProperty("directory.data").concat("/").concat(globals.getProperty("file.workshop")));
            checkFile(globals.getProperty("directory.data"), globals.getProperty("file.workshop")); // If the file doesn't exist create it
            Reader reader = Files.newBufferedReader(Paths.get(globals.getProperty("directory.data").concat("/").concat(globals.getProperty("file.workshop"))));
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
            logger.trace("Loading " + globals.getProperty("directory.data").concat("/").concat(globals.getProperty("file.learner")));
            checkFile(globals.getProperty("directory.data"), globals.getProperty("file.learner")); // If the file doesn't exist create it
            Reader reader = Files.newBufferedReader(Paths.get(globals.getProperty("directory.data").concat("/").concat(globals.getProperty("file.learner"))));
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
            logger.trace("Loading " + globals.getProperty("directory.data").concat("/").concat(globals.getProperty("file.lesson")));
            checkFile(globals.getProperty("directory.data"), globals.getProperty("file.lesson")); // If the file doesn't exist create it
            Reader reader = Files.newBufferedReader(Paths.get(globals.getProperty("directory.data").concat("/").concat(globals.getProperty("file.lesson"))));
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

    private static void checkFile(String dir, String filename) {
        logger.debug("Check if directory " + dir + " exists.");
        File directory = new File(dir);
        if (!directory.equals(""))
            if (!directory.exists()) {
                directory.mkdirs();
            }
        try {
            filename = dir.concat("/").concat(filename);
            logger.debug("Check if file ".concat(filename));
            File file = new File(filename);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Load properties from system.properties file
     */
    public static Properties loadProperties() {
        String propertiesfile = System.getProperty("user.home").concat("/.certify/").concat(propertyFile);
        logger.trace("Load properties from " + propertiesfile);
        properties = new Properties();
        try {
            File f = new File(propertiesfile);
            // If the file doesn't exist, create it

            if (!(f.exists())) {
                OutputStream out = new FileOutputStream(f);
                logger.trace("Create ".concat(f.getAbsolutePath()));
                out.close();
            }
            InputStream is = new FileInputStream(f);
            properties.load(is);
            if (properties.size() == 0) {
                logger.trace("Add basic properties");
                properties.setProperty("file.learner", learnerfile);
                properties.setProperty("file.workshop", workshopfile);
                properties.setProperty("file.lesson", lessonfile);
                properties.setProperty("directory.data", dataDirectory);
                properties.setProperty("directory.pdf", pdfDirectory);
                properties.setProperty("directory.config", configDirectory);
                properties.setProperty("badges", sBadges);
                properties.setProperty("mail.smtp.auth", "");
                properties.setProperty("mail.smtp.host", "");
                properties.setProperty("mail.smtp.port", "");
                properties.setProperty("mail.smtp.starttls.enable", "");
                properties.setProperty("mail.smtp.from", "");
                properties.setProperty("mail.smtp.password", "");
                properties.setProperty("mail.smtp.to", "");
            }
            FileOutputStream out = new FileOutputStream(propertiesfile);
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
    public static String getProperty(String property) {
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
        for (Learner allLearner : all_learners) {
            if (allLearner.getUser_id().equals(userID)) {
                return allLearner;
            }
        }
        return null;
    }

    public Lesson findLesson(String lessonID) {
        for (Lesson lesson : lessons) {
            if (lesson.getLessonID().equals(lessonID)) {
                return lesson;
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

    public void saveJSON(String filenameProperty, Object object) {
        String filename = getProperty("directory.data").concat("/").concat(getProperty(filenameProperty));
        try {
            Writer writer = new FileWriter(filename);
            gson.toJson(object, writer);
            logger.debug("Saving " + filename);
            writer.close();
        } catch (JsonIOException e) {
            logger.trace("File " + filename + " not found");
        } catch (IOException e) {
            logger.trace("IOError while trying to write to " + filename);
        }
    }

    public boolean isWorkshopsSaved() {
        return workshopsSaved;
    }

    public void setWorkshopsSaved(boolean workshopsSaved) {
        this.workshopsSaved = workshopsSaved;
    }

    public boolean isLearnersSaved() {
        return learnersSaved;
    }

    public void setLearnersSaved(boolean learnersSaved) {
        this.learnersSaved = learnersSaved;
    }

    public void setLessonsSaved(boolean lessonsSaved) {
        logger.debug("Set lessons saved to " + lessonsSaved);
        this.lessonsSaved = lessonsSaved;
    }

    public boolean isLessonsSaved() {
        return lessonsSaved;
    }

    public String[] getBadges() {
        return sBadges.split(",");
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

    public static void svg2pdf(String pdfPath, String certificate, String userID) {
        File outputFile;
        File f_svgFile = new File(certificate);
        try {
            outputFile = File.createTempFile(pdfPath.concat("/").concat("result-"), ".pdf");
            SVGConverter converter = new SVGConverter();
            converter.setDestinationType(DestinationType.PDF);
            converter.setSources(new String[]{f_svgFile.toString()});
            converter.setDst(outputFile);
            logger.trace("Converting to pdf - outputFile " + outputFile);
            converter.execute();
            logger.trace("Converting to pdf");
            Path copied = Paths.get(pdfPath.concat("/").concat(userID).concat(".pdf"));
            Files.copy(outputFile.toPath(), copied);
            Files.delete(outputFile.toPath());
            Files.delete(f_svgFile.toPath());
        } catch (IOException | SVGConverterException e) {
            logger.error(e.getMessage());
        }

    }

    private boolean inUse(String workshop_id, Learners learners) {
        for (Learner learner : learners) {
            if (learner.getWorkshopID().equals(workshop_id))
                return true;
        }

        return false;
    }

    public int delWorkshops(Learners learners) {
        Workshops workshops = globals.getWorkshops();
        ArrayList<Workshop> delWorkshops = new ArrayList<Workshop>();
        for (Workshop workshop : workshops) {
            if (workshop.isPrint()) {
                if (inUse(workshop.getWorkshop_id(), learners))
                    return 1;
                delWorkshops.add(workshop);
            }
        }
        for (Workshop delWorkshop : delWorkshops) {
            workshops.remove(delWorkshop);
            globals.setWorkshopsSaved(false);
        }
        return 0;
    }

    public Learners delLearners(Learners learners) {
        ArrayList<Learner> delLearners = new ArrayList<Learner>();
        for (Learner learner : learners) {
            if (learner.isPrint()) {
                delLearners.add(learner);
            }
        }
        for (Learner delLearner : delLearners) {
            learners.remove(delLearner);
            globals.setLearnersSaved(false);
        }
        return learners;
    }

    public static Lessons delLessons(Lessons lessons) {
        ArrayList<Lesson> delLessons = new ArrayList<Lesson>();
        for (Lesson lesson : lessons) {
            if (lesson.isPrint()) {
                delLessons.add(lesson);
            }
        }
        for (Lesson delLesson : delLessons) {
            lessons.remove(delLesson);
            globals.setLessonsSaved(false);
        }
        return lessons;
    }

    /**
     * @param learners An ArrayList of type Learner
     * @param filename File handle of the CSV file to add the leaners from
     * @param workshop The workshop ID
     * @return An ArrayList of type Learner populated from the csv file
     */
    public Learners readCSVLearnersFile(Learners learners, File filename, String workshop, String workshopID) {
        try {
            Scanner sc = new Scanner(filename);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                logger.debug("Read line: " + line);
                String[] tokens = line.split(",");
                // workshop, instructor, user_id, name, email, date
                Learner learner = new Learner(workshopID, tokens[0].split("-")[0], tokens[1], tokens[2], tokens[3], "",
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

    /**
     * Reads records from a CSV file. Each line in the file should be in the format:
     * "User ID", "Firstname","Initials", "Lastname", "Email"
     *
     * @param learners
     * @param workshop
     * @param badge
     * @param instructor
     * @param date
     * @param file
     * @return
     */
    public Learners importTextFile(Learners learners, String workshopID, String workshop, String badge, String instructor, String date, File file) {
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String line = sc.nextLine().strip();
                String[] tokens = line.split(",");
                date = (date.strip().equals("") ? now() : date);
                Learner learner = new Learner(workshopID, badge, instructor, (tokens[0] + "_" + tokens[1]).replace(" ", "_"), tokens[0], "",
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

    public static Properties getProperties() {
        return  properties;
    }

    public static String getVersion() {
        return version;
    }

}
