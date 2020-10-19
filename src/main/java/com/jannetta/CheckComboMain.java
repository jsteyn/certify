package com.jannetta;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// http://www.java2s.com/Tutorials/Java/Swing_How_to/JComboBox/Add_JCheckBox_components_to_JComboBox.htm
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.jannetta.certify.controller.Globals;
import com.jannetta.certify.model.Lesson;

public class CheckComboMain {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new CheckCombo().getContent());
        f.setSize(300, 160);
        f.setLocation(200, 200);
        f.setVisible(true);
    }
}

class CheckCombo implements ActionListener {
    Globals globals = Globals.getInstance();

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        CheckComboStore store = (CheckComboStore) cb.getSelectedItem();
        CheckComboRenderer ccr = (CheckComboRenderer) cb.getRenderer();
        store.lesson.setPrint(!store.lesson.isPrint());
        ccr.checkBox.setSelected(store.lesson.isPrint());
    }

    public JPanel getContent() {
        CheckComboStore[] stores = new CheckComboStore[globals.getLessons().size()];

        for (int j = 0; j < globals.getLessons().size(); j++)
            stores[j] = new CheckComboStore(globals.getLessons().get(j));

        JComboBox combo = new JComboBox(stores);
        combo.setRenderer(new CheckComboRenderer());
        combo.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(combo);
        return panel;
    }
}

class CheckComboRenderer implements ListCellRenderer {
    JCheckBox checkBox;

    public CheckComboRenderer() {
        checkBox = new JCheckBox();
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        CheckComboStore store = (CheckComboStore) value;
        checkBox.setText(store.lesson.getLessonID());
        checkBox.setSelected(((Boolean) store.lesson.isPrint()).booleanValue());
        checkBox.setBackground(isSelected ? Color.red : Color.white);
        checkBox.setForeground(isSelected ? Color.white : Color.black);
        return checkBox;
    }
}

class CheckComboStore {
    Lesson lesson;

    public CheckComboStore(Lesson lesson) {
        this.lesson = lesson;
    }

}