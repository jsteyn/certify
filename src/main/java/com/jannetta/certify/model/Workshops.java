package com.jannetta.certify.model;

import java.util.ArrayList;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workshops extends ArrayList<Workshop> {
    Logger logger = LoggerFactory.getLogger(getClass());

    @SerializedName("workshops")
    @Expose
    Workshops workshops = this;
    private static final long serialVersionUID = 1L;

    // TODO	There should be a better way of doing this, but in the meantime ..
    Hashtable<String, Integer> id_index = new Hashtable<>();

    /**
     * The number of attributes (columns) in the Workshop class.
     *
     * @return an integer containing the number attributes in the workshop class to be used as columns in a table
     */
    public int getColumnCount() {
        return Workshop.getColumnCount();
    }

    /**
     * Get the names of the attributes in the Workshop class to be used column names in a table
     *
     * @return an array of string containing the names of the columns
     */
    public String[] getColumnNames() {
        return Workshop.getColumnNames();
    }

    /**
     * Get the names of all the workshops
     *
     * @return an array of string containing the names of all the workshops
     */
    public String[] getWorkshopNames() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("All");
        this.forEach((ws) -> {
            names.add(ws.getWorkshop_name());
        });
        String[] ret = names.toArray(new String[0]);
        logger.trace("Workshop names: " + ret.length);
        return ret;
    }

    /**
     * Find the name of a workshop given its ID
     *
     * @param id a string containing the ID of the workshop
     * @return the name of the workshop given the ID
     */
    public String getWorkshopName(String id) {
        return get(id_index.get(id)).getWorkshop_name();
    }

    /**
     * A list of all the workshop IDs
     *
     * @return an array of string containing all the workshop IDs
     */
    public String[] getWorkshopIDs() {
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("All");
        this.forEach((ws) -> {
            ids.add(ws.getWorkshop_id());
        });
        String[] ret = ids.toArray(new String[0]);
        logger.trace("Workshop ids: " + ret.length);
        return ret;
    }

    /**
     * Check whether a workshop exists for the given ID
     *
     * @param workshopID a string containing the ID of the workshop
     * @return true if a workshop with the given ID was found, otherwise false
     */
    public boolean exists(String workshopID) {
        for (int i = 0; i < this.size(); i++) {
            if (get(i).getWorkshop_id().equals(workshopID))
                return true;
        }
        return false;
    }

    /**
     * Override the add method in order to update a hashtable that contains the
     * workshopID as the key and the index of the workshop in the array as the value
     *
     * @param workshop element whose presence in this collection is to be ensured
     * @return true if the workshop was successfully added
     */
    @Override
    public boolean add(Workshop workshop) {
        logger.trace("Add workshop: " + workshop.getWorkshop_name());
        boolean ret = super.add(workshop);
        id_index.put(workshop.getWorkshop_id(), this.size() - 1);
        return ret;
    }
}
