package com.jannetta.certify.view;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jannetta.certify.controller.Globals;
import com.jannetta.certify.model.Lesson;
import com.jannetta.certify.model.Lessons;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LessonSelectionPanel extends JPanel implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(getClass());
    Globals globals = Globals.getInstance();
    ArrayList<JCheckBox> lessons = new ArrayList<JCheckBox>();
    ActionListener actionListener;
    JLabel header = new JLabel("Tick lessons completed:");
    int number_of_lessons = globals.getLessons().size();

    public LessonSelectionPanel() {
        super();

        // Set panel layout
        MigLayout migLayout = new MigLayout("fillx", "[]", "[]");
        setLayout(migLayout);
        add(header, "wrap");
        for (int i = 0; i < number_of_lessons; i++) {
            JCheckBox checkbox = new JCheckBox(globals.getLessons().get(i).getDescription());
            checkbox.addActionListener(this);
            checkbox.setActionCommand(globals.getLessons().get(i).getLessonID());
            lessons.add(checkbox);
        }
        for (int i = 0; i < number_of_lessons; i++) {
            add(lessons.get(i), "wrap");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());

    }

    public ArrayList<JCheckBox> getLessons() {
        return lessons;
    }

    public Lessons getSelectedLessons() {
        Lessons newLessons = new Lessons();
        for (int i = 0; i < number_of_lessons; i++) {
            if (lessons.get(i).isSelected()) {
                newLessons.add(new Lesson(globals.getLessons().getLesson(i).getLessonID(),
                        globals.getLessons().getLesson(i).getDescription()));
            }
        }
        return newLessons;
    }

    public void setLessons(ArrayList<JCheckBox> lessons) {
        this.lessons = lessons;
    }

    public void checkBoxes(String str) {
        String[] tokens = str.split(",");
        for (int i = 0; i < number_of_lessons; i++) {
            lessons.get(i).setSelected(false);
            for (int j = 0; j < tokens.length; j++) {
                if (lessons.get(i).getActionCommand().equals(tokens[j]))
                    lessons.get(i).setSelected(true);

            }
        }
    }

}
