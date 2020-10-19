package com.jannetta.certify.view;

import java.awt.Component;
import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class CheckComboRenderer implements ListCellRenderer {
    JCheckBox checkBox;

    public CheckComboRenderer() {
        checkBox = new JCheckBox();
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        CheckComboStore store = (CheckComboStore) value;
        checkBox.setText(store.lesson.getLessonID() + ": " + store.lesson.getDescription());
        checkBox.setSelected(((Boolean) store.lesson.isPrint()).booleanValue());
        checkBox.setBackground(isSelected ? Color.red : Color.white);
        checkBox.setForeground(isSelected ? Color.white : Color.black);
        return checkBox;
    }
}
