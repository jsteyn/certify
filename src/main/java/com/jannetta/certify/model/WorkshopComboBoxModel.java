package com.jannetta.certify.model;

import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jannetta.certify.controller.Globals;

public class WorkshopComboBoxModel extends DefaultComboBoxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private Logger logger = LoggerFactory.getLogger(getClass());
	final private Globals globals = Globals.getInstance();
	private List<String> workshopIDs;
	private String selection = null;

	public WorkshopComboBoxModel(String[] items) {
		logger.trace("init workshopcombomodel");
		workshopIDs = Arrays.asList(globals.getWorkshops().getWorkshopIDs());
		setSelectedItem(items[0]);
	}

	@Override
	public String getElementAt(int index) {
		return workshopIDs.get(index);
	}

	@Override
	public int getSize() {
		return workshopIDs.size();
	}

	@Override
	public Object getSelectedItem() {
		return selection;
	}

	@Override
	public void setSelectedItem(Object object) {
		selection = (String) object;
	}

	@Override
	public void addElement(Object element) {
		insertElementAt(element, 0);
	}
	
	public String[] getWorkshopIDs() {
		return workshopIDs.toArray(new String[0]);
	}

	public void setWorkshopIDs(String[] workshopIDs) {
		this.workshopIDs = Arrays.asList(workshopIDs);
	}

	@Override
	public void insertElementAt(Object element, int index) {
		logger.trace("Add element " + (String) element + " to ComboBox");
		workshopIDs = Arrays.asList(globals.getWorkshops().getWorkshopIDs());
		super.insertElementAt(element, index);
	}
}
