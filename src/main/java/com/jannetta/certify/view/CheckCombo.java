package com.jannetta.certify.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import com.jannetta.certify.controller.Globals;

class CheckCombo extends JComboBox implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Globals globals = Globals.getInstance();

    public JComboBox getContent() {
        CheckComboStore[] stores = new CheckComboStore[globals.getLessons().size()];

        for (int j = 0; j < globals.getLessons().size(); j++)
            stores[j] = new CheckComboStore(globals.getLessons().get(j));

        JComboBox combo = new JComboBox(stores);
        combo.setRenderer(new CheckComboRenderer());
        combo.addActionListener(this);
        
        return combo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        CheckComboStore store = (CheckComboStore) cb.getSelectedItem();
        CheckComboRenderer ccr = (CheckComboRenderer) cb.getRenderer();
        store.lesson.setPrint(!store.lesson.isPrint());
        ccr.checkBox.setSelected(store.lesson.isPrint());
    }
}