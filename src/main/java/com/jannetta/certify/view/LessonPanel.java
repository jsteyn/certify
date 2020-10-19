package com.jannetta.certify.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import com.jannetta.certify.controller.Globals;
import com.jannetta.certify.model.Lesson;
import com.jannetta.certify.model.Lessons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.miginfocom.swing.MigLayout;

public class LessonPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(getClass());
    Globals globals = Globals.getInstance();

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JLabel lbl_lesson_ID = new JLabel("Lesson ID");
    private JTextField tf_lesson_ID = new JTextField(4);
    private JLabel lbl_lesson_desc = new JLabel("Description");
    private JTextField tf_lesson_desc = new JTextField(50);
    private JButton btn_submit = new JButton("Submit");
    private JButton btn_update = new JButton("Update");
    private JButton btn_cancel = new JButton("Cancel");
    private JButton btn_save = new JButton("Save");
    private JButton btn_delete = new JButton("Delete");
    private JButton btn_clear = new JButton("Clear");
    private JTable tbl_lessons;
    private CheckBoxHeader checkboxHeader;
    private JPanel buttonPanel1 = new JPanel();
    private JPanel buttonPanel2 = new JPanel();
    private int selected_row = -1;

    public LessonPanel() {
        super();
        btn_submit.addActionListener(this);
        btn_update.addActionListener(this);
        btn_cancel.addActionListener(this);
        btn_save.addActionListener(this);
        btn_clear.addActionListener(this);
        btn_delete.addActionListener(this);

        tbl_lessons = new JTable(globals.getLessonTableModel());
        tbl_lessons.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (row != -1) {
                    selected_row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
                    Lesson lesson = globals.getLessonTableModel().getLessons().get(row);
                    if (mouseEvent.getClickCount() == 2 && selected_row != -1) {
                        logger.trace("Double clicked lesson table entry");
                        // Change button to "Update"
                        btn_submit.setEnabled(false);
                        btn_update.setEnabled(true);
                        btn_cancel.setEnabled(true);

                        // Transfer record to form
                        tf_lesson_ID.setText((String) table.getValueAt(selected_row, 0));
                        tf_lesson_desc.setText((String) table.getValueAt(selected_row, 1));

                    } else if (selected_row != -1 && column == globals.getLessonTableModel().getColumnCount() - 1) {
                        lesson.setPrint(!lesson.isPrint());
                        globals.fireTableDataChanged();
                        globals.getLessonTableModel().getValueAt(selected_row, column);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tbl_lessons);
        tbl_lessons.setFillsViewportHeight(true);
        TableColumn tc = tbl_lessons.getColumnModel().getColumn(globals.getLessonTableModel().getColumnCount() - 1);
        tc.setCellEditor(tbl_lessons.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(tbl_lessons.getDefaultRenderer(Boolean.class));
        checkboxHeader = new CheckBoxHeader(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Object source = e.getSource();
                if (source instanceof AbstractButton == false)
                    return;
                boolean checked = e.getStateChange() == ItemEvent.SELECTED;
                globals.getLessons().forEach((lesson) -> {
                    lesson.setPrint(checked);
                });
            }
        });
        tc.setHeaderRenderer(checkboxHeader);

        MigLayout migLayout = new MigLayout("fillx", "[]rel[]", "[]10[]");
        setLayout(migLayout);
        add(lbl_lesson_ID);
        add(tf_lesson_ID, "wrap");
        add(lbl_lesson_desc);
        add(tf_lesson_desc, "wrap");

        buttonPanel1.add(btn_submit);
        buttonPanel1.add(btn_update);
        buttonPanel1.add(btn_cancel);
        buttonPanel1.add(btn_delete);

        add(buttonPanel1, "span, wrap");
        add(scrollPane, "span, grow");

        buttonPanel2.add(btn_save);
        add(buttonPanel2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        if (e.getActionCommand().equals("Submit")) {
            Lesson lesson = new Lesson(tf_lesson_ID.getText(), tf_lesson_desc.getText());
            if (globals.getLessons().exists(lesson.getLessonID())) {
                JOptionPane.showMessageDialog(this, "A lesson with this ID already exists.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (tf_lesson_ID.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "The lesson id field must not be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                globals.getLessons().add(lesson);
                globals.fireTableDataChanged();
                globals.setLessonssaved(false);
                logger.trace("Submit lesson entry to table");
            }
        }
        if (e.getActionCommand().equals("Update")) {
            table2Lessonss(selected_row, tbl_lessons);
            btn_update.setEnabled(false);
            btn_cancel.setEnabled(false);
            btn_submit.setEnabled(true);
            globals.setLessonssaved(false);

        }
        if (e.getActionCommand().equals("Cancel")) {
            btn_update.setEnabled(false);
            btn_cancel.setEnabled(false);
            btn_submit.setEnabled(true);
        }
        if (e.getActionCommand().equals("Save")) {
            logger.trace("Save file");
            globals.saveJSON(globals.getProperty("lessonfile"), globals.getLessons());
            globals.setLessonssaved(true);
        }
        if (e.getActionCommand().equals("Delete")) {
            Lessons lessons = globals.getLessons();
            Globals.delLessons(lessons);
            globals.fireTableDataChanged();
        }

    }

    /**
     * Read records from table and add to form
     * 
     * @param row
     * @param lessonTable
     */
    public void table2Lessonss(int row, JTable lessonTable) {
        Lesson lesson = globals.findLesson(globals.getLessons().get(row).getLessonID());
        lesson.setDescription((String) tf_lesson_desc.getText().strip());
        globals.fireTableDataChanged();
    }
}
