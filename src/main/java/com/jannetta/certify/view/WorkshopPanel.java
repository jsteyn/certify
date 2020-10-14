package com.jannetta.certify.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jannetta.certify.controller.Globals;
import com.jannetta.certify.model.Learner;
import com.jannetta.certify.model.LearnerTableModel;
import com.jannetta.certify.model.Learners;
import com.jannetta.certify.model.Workshop;
import com.jannetta.certify.model.WorkshopTableModel;
import com.jannetta.certify.model.Workshops;

import net.miginfocom.swing.MigLayout;

public class WorkshopPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	private Globals globals = Globals.getInstance();

	private JLabel lbl_workshopname = new JLabel("Workshop Name");
	private JLabel lbl_badge = new JLabel("Badge");
	private JLabel lbl_fromDate = new JLabel("Date From");
	private JLabel lbl_toDate = new JLabel("Date to");
	private JLabel lbl_URL = new JLabel("Website URL");
	private JTextField tf_workshopname = new JTextField(20);
	private JComboBox<String> cb_badge = new JComboBox<String>(globals.getBadges());
	private JTextField tf_fromDate = new JTextField(20);
	private JTextField tf_toDate = new JTextField(20);
	private JTextField tf_URL = new JTextField(80);
	private JTable tbl_workshops;
	private JButton btn_clear = new JButton("Clear");
	private JButton btn_submit = new JButton("Submit");
	private JButton btn_update = new JButton("Update");
	private JButton btn_cancel = new JButton("Cancel");
	private JButton btn_save = new JButton("Save");
	private JButton btn_delete = new JButton("Delete");
	private JPanel pnl_buttonholder = new JPanel();
	private int selected_row = -1;
	private CheckBoxHeader checkboxHeader;

	WorkshopPanel() {
		super();
		logger.trace("Create workshop panel");
		btn_clear.addActionListener(this);
		btn_submit.addActionListener(this);
		btn_update.addActionListener(this);
		btn_cancel.addActionListener(this);
		btn_save.addActionListener(this);
		btn_submit.setEnabled(true);
		btn_cancel.setEnabled(false);
		btn_update.setEnabled(false);
		btn_delete.addActionListener(this);
		tbl_workshops = new JTable(globals.getWorkshopTableModel());
		TableColumn tc = tbl_workshops.getColumnModel().getColumn(globals.getWorkshopTableModel().getColumnCount() - 1);
		tc.setCellEditor(tbl_workshops.getDefaultEditor(Boolean.class));
		tc.setCellRenderer(tbl_workshops.getDefaultRenderer(Boolean.class));
		checkboxHeader = new CheckBoxHeader(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Object source = e.getSource();
				if (source instanceof AbstractButton == false)
					return;
				boolean checked = e.getStateChange() == ItemEvent.SELECTED;
				globals.getWorkshops().forEach((workshop) -> {
					workshop.setPrint(checked);
				});
			}
		});
		tc.setHeaderRenderer(checkboxHeader);
		tbl_workshops.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (row != -1) {
					selected_row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					Workshop workshop = ((WorkshopTableModel) tbl_workshops.getModel()).getWorkshops().get(row);
					if (mouseEvent.getClickCount() == 2 && selected_row != -1) {
						logger.trace("Double clicked workshop table entry");

						// Transfer record to form
						tf_workshopname.setText((String) table.getValueAt(selected_row, 0));
						cb_badge.setSelectedItem((String) table.getValueAt(selected_row, 1));
						tf_fromDate.setText((String) table.getValueAt(selected_row, 2));
						tf_toDate.setText((String) table.getValueAt(selected_row, 3));
						tf_URL.setText((String) table.getValueAt(selected_row, 4));

						// Change button to "Update"
						btn_submit.setEnabled(false);
						btn_update.setEnabled(true);
						btn_cancel.setEnabled(true);
					} else if (selected_row != -1 && column == globals.getWorkshopTableModel().getColumnCount() - 1) {
						workshop.setPrint(!workshop.isPrint());
						globals.fireTableDataChanged();
						globals.getWorkshopTableModel().getValueAt(selected_row, column);
					}
				}
			}

		});

		MigLayout migLayout = new MigLayout("fillx", "[fill]rel[]rel[]rel[]", "[]10[]10[]10[]");
		setLayout(migLayout);

		JScrollPane scrollPane = new JScrollPane(tbl_workshops);
		tbl_workshops.setFillsViewportHeight(true);

		pnl_buttonholder.add(btn_clear);
		pnl_buttonholder.add(btn_submit);
		pnl_buttonholder.add(btn_update);
		pnl_buttonholder.add(btn_cancel);
		pnl_buttonholder.add(btn_delete);

		add(lbl_workshopname, "");
		add(tf_workshopname, "wrap");
		add(lbl_badge);
		add(cb_badge, "wrap");
		add(lbl_fromDate);
		add(tf_fromDate, "wrap");
		add(lbl_toDate);
		add(tf_toDate, "wrap");
		add(lbl_URL);
		add(tf_URL, "span, wrap");
		add(pnl_buttonholder, "span, wrap");
		add(scrollPane, "span, wrap");
		add(btn_save, "wrap");

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Clear")) {
			tf_workshopname.setText("");
			cb_badge.setSelectedItem(globals.getBadges()[0]);
			tf_fromDate.setText("");
			tf_toDate.setText("");
			tf_URL.setText("");
			btn_update.setEnabled(false);
			btn_cancel.setEnabled(false);
			btn_submit.setEnabled(true);
		}
		if (e.getActionCommand().equals("Submit")) {
			Workshop workshop = new Workshop(tf_workshopname.getText().strip(), (String) cb_badge.getSelectedItem(),
					tf_fromDate.getText().strip(), tf_toDate.getText().strip(), tf_URL.getText().strip());
			((WorkshopTableModel) tbl_workshops.getModel()).getWorkshops().add(workshop);
			globals.getWorkshopComboBoxModel().addElement(tf_workshopname.getText());
			globals.fireTableDataChanged();
			globals.setWorkshopssaved(false);
			logger.trace("Submit workshop entry to table");
		}
		if (e.getActionCommand().equals("Update")) {
			table2Workshop(selected_row, tbl_workshops);
			globals.fireTableDataChanged();
			btn_update.setEnabled(false);
			btn_cancel.setEnabled(false);
			btn_submit.setEnabled(true);
			globals.setWorkshopssaved(false);
		}
		if (e.getActionCommand().equals("Cancel")) {
			btn_update.setEnabled(false);
			btn_cancel.setEnabled(false);
			btn_submit.setEnabled(true);
		}
		if (e.getActionCommand().equals("Save")) {
			logger.trace("Save file");
			globals.saveJSON("Workshops.json", globals.getWorkshops());
			globals.setWorkshopssaved(true);
		}
		if (e.getActionCommand().equals("Delete")) {
			int ret = globals.delWorkshops(globals.getAllLearners());
			if (ret == 1) {
				JOptionPane.showMessageDialog(this,
						"One or more of the workshops you selected are still in use. "
								+ "Delete all learners associated with the workshop/s and try again",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				globals.fireTableDataChanged();
				globals.setWorkshopssaved(false);
			}
			logger.trace("Delete workshops returned " + ret);

		}
	}

	public void table2Workshop(int row, JTable workshoptable) {
		Workshop workshop = ((WorkshopTableModel) workshoptable.getModel()).getWorkshops().get(row);
		workshop.setWorkshop_name(tf_workshopname.getText().strip());
		workshop.setBadge((String) cb_badge.getSelectedItem());
		workshop.setDate_from(tf_fromDate.getText().strip());
		workshop.setDate_to(tf_toDate.getText().strip());
		workshop.setUrl(tf_URL.getText().strip());
		globals.getWorkshopComboBoxModel().setWorkshops((globals.getWorkshops().getWorkshopNames()));

	}
}
