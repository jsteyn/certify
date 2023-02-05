package com.jannetta.certify.view;

import com.jannetta.certify.controller.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationPanel.class);
    private static Globals globals = Globals.getInstance();

    JTextArea jTextArea = new JTextArea();

    public ConfigurationPanel() {
        jTextArea.setEditable(false);
        add(jTextArea);
        Globals.getProperties().forEach((key, value) -> {
            jTextArea.append(key + ": " + value + "\n");
        });
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
