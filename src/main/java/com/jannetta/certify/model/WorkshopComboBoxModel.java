package com.jannetta.certify.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jannetta.certify.controller.Globals;

public class WorkshopComboBoxModel extends DefaultComboBoxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Globals globals = Globals.getInstance();
	private List<String> workshops;
	private String selection = null;

	private WorkshopComboBoxModel() {
		super();
	}

	public WorkshopComboBoxModel(String[] items) {
		logger.debug("init workshopcombomodel");
		workshops = Arrays.asList(globals.getWorkshops().getWorkshopNames());
		setSelectedItem(items[0]);
	}

	public WorkshopComboBoxModel(Vector<String> items) {
		Collections.sort(items);
		int size = items.size();
		for (int i = 0; i < size; i++) {
			super.addElement(items.elementAt(i));
		}
		setSelectedItem(items.elementAt(0));
	}

	@Override
	public String getElementAt(int index) {
		return workshops.get(index);
	}

	@Override
	public int getSize() {
		return workshops.size();
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
	
	public String[] getWorkshops() {
		return workshops.toArray(new String[0]);
	}

	public void setWorkshops(String[] workshops) {
		this.workshops = Arrays.asList(workshops);
	}

	@Override
	public void insertElementAt(Object element, int index) {
		logger.trace("Add element " + (String) element + " to ComboBox");
		workshops = Arrays.asList(globals.getWorkshops().getWorkshopNames());
		super.insertElementAt(element, index);
	}
}
