package com.jannetta.certify.model;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkshopTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Workshops workshops;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public WorkshopTableModel() {
		super();
		logger.trace("Create WorkshopTableModel");
	}

	public int getColumnCount() {
		if (workshops == null)
			return 0;
		else
			return workshops.getColumnCount();
	}

	public int getRowCount() {
		if (workshops == null)
			return 0;
		else
		return workshops.size();
	}

	public Object getValueAt(int row, int col) {
		Workshop workshop = workshops.get(row);
		switch (col) {
		case 0:
			return workshop.getWorkshop_name();
		case 1:
			return workshop.getBadge();
		case 2:
			return workshop.getDate_from();
		case 3:
			return workshop.getDate_to();
		case 4:
			return workshop.getUrl();
		case 5:
			return workshop.isPrint();
		}

		return null;

	}

	@Override
	public String getColumnName(int col) {
		return workshops.getColumnNames()[col];
	}

	public Workshops getWorkshops() {
		return workshops;
	}

	public void setWorkshops(Workshops workshops) {
		logger.trace("Set workshops object");
		this.workshops = workshops;
	}

}
