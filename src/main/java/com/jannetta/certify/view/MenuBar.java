package com.jannetta.certify.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.jannetta.certify.controller.Globals;
import com.jannetta.certify.model.Workshops;

public class MenuBar extends JMenuBar implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(getClass());
    Globals globals = Globals.getInstance();

    public MenuBar() {
        JMenu file;
        JMenu help;
        JMenuItem newfile;
        JMenuItem openFile;
        JMenuItem saveFile;
        JMenuItem saveAsFile;
        JMenuItem exit;
        JMenuItem about;

        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        // File menu items
        newfile = new JMenuItem("New", KeyEvent.VK_N);
        newfile.addActionListener(this);
        openFile = new JMenuItem("Open", KeyEvent.VK_O);
        openFile.addActionListener(this);
        saveFile = new JMenuItem("Save", KeyEvent.VK_S);
        saveFile.addActionListener(this);
        saveAsFile = new JMenuItem("Save As", KeyEvent.VK_A);
        saveAsFile.addActionListener(this);
        exit = new JMenuItem("Exit", KeyEvent.VK_X);
        exit.addActionListener(this);
        help = new JMenu("Help");
        help.setMnemonic('H');

        // Help menu items
        about = new JMenuItem("About", 'A');
        about.addActionListener(this);

        // Add menu items to file
        //file.add(newfile);
        //file.add(openFile);
        //file.add(saveFile);
        //file.add(saveAsFile);
        file.add(exit);

        // Add menu items to help
        help.add(about);

        this.add(file);
        this.add(help);
    }

    private ImageIcon createImageIcon(String path, String description) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            Image icon = toolkit.getImage(ClassLoader.getSystemResource("Certify200.png"));
            return new ImageIcon(icon);
        } catch (NullPointerException e) {
            logger.error("Logo.png not found.");
            return null;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("About")) {
            ImageIcon icon = createImageIcon("Certify200.png", "a pretty but meaningless splat");
            JOptionPane.showMessageDialog(this,
                    "Certify is a program for taking the register during a Carpentries course\n"
                            + "and producing certificates of attendance afterwards.\n"
                            + "Copyright: Jannetta S Steyn, 2020\n"
                            + "Version: " + Globals.getVersion(),
                    "About Certify", JOptionPane.PLAIN_MESSAGE, icon);
        }
        if (e.getActionCommand().equals("Exit")) {
            if (!globals.isLearnersSaved() || !globals.isLessonsSaved() || !globals.isWorkshopsSaved()) {
                if (JOptionPane.showConfirmDialog(this, "Some of your modifications have not been saved. Are you sure you want to exit?", "Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_NO_OPTION)
                    System.exit(0);
            }
        }
        if (e.getActionCommand().equals("New")) {
            logger.info("Open a new file");
        }
        if (e.getActionCommand().equals("Open")) {
            Gson gson = new Gson();
            try {
                Reader reader = Files.newBufferedReader(Paths.get("Workshops.json"));
                globals.setWorkshops(gson.fromJson(reader, Workshops.class));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        if (e.getActionCommand().equals("Save")) {
        }
    }

}
