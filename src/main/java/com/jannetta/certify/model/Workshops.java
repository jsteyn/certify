package com.jannetta.certify.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workshops extends ArrayList<Workshop>{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@SerializedName("workshops")
	@Expose
	Workshops workshops = this;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The number of attributes (columns) in the Workshop class.
	 * @return
	 */
	public int getColumnCount() {
		return Workshop.getColumnCount();
	}
	
	public String[] getColumnNames() {
		return Workshop.getColumnNames();
	}
	
	public String[] getWorkshopNames() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("All");
		this.forEach((ws) -> {
			names.add(ws.getWorkshop_name());
		});
		String[] ret =  names.toArray(new String[0]);
		logger.debug("Workshop names: " + ret.length);
		return ret;
	}


}
