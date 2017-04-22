/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

import com.savelives.entityclasses.CrimeCase;
import com.savelives.sessionbeans.CrimeCaseFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author TaiwenJin
 */
@SessionScoped
@Named(value = "crimeCaseController")
public class CrimeCaseController implements Serializable {

    @EJB
    private final CrimeCaseFacade ejbFacade;
    private MapModel crimeModel;
    private CrimeCase selected;
    private Date date1;
    private Date date2;
    private final List<String> crimeCategories;
    private final List<String> crimeCodes;
    private List<String> selectedCategories;
    private List<String> selectedCrimeCodes;
    private final int NUMB_OF_CRIMES = 500;

    /**
     * Default Constructor
     */
    public CrimeCaseController() {
        ejbFacade = new CrimeCaseFacade();

        crimeModel = ejbFacade.getCrimesModel(NUMB_OF_CRIMES);
        crimeCategories = ejbFacade.getDistinct("description");
        crimeCodes = ejbFacade.getDistinct("crimecode");
    }

    /*========== Getters and Setters ==============*/
    private CrimeCaseFacade getFacade() {
        return ejbFacade;
    }

    public MapModel getCrimeModel() {
        return crimeModel;
    }

    public Marker getSelected() {
        return selected;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public List<String> getCrimeCategories() {
        return crimeCategories;
    }

    public List<String> getCrimeCodes() {
        return crimeCodes;
    }

    public void setSelectedCategories(List<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public List<String> getSelectedCategories() {

        return selectedCategories;
    }

    public List<String> getSelectedCrimeCodes() {
        return selectedCrimeCodes;
    }

    public void setSelectedCrimeCodes(List<String> selectedCrimeCodes) {
        this.selectedCrimeCodes = selectedCrimeCodes;
    }

    //============== INSTANCE METHODS =====================//
    public void onMarkerSelect(OverlaySelectEvent event) {
        selected = (CrimeCase) event.getOverlay();
    }
    public void submit() {
        crimeModel = null;
        if (selectedCategories == null || selectedCategories.isEmpty()) {
            // set this to all categories since the user has not chosen any category
            // otherwise the result will be an empty list of crimes. A workaround
            // would be to make category selection mandatory
            selectedCategories = this.getCrimeCategories();
        }
        if (selectedCrimeCodes == null || selectedCrimeCodes.isEmpty()) {
            selectedCrimeCodes = crimeCodes;
        }
        try {
            //Perform the filtering
            crimeModel = getFacade().filterCrimes(date1, date1, selectedCategories, selectedCrimeCodes);
        } catch (UnsupportedOperationException ex) {
            // only catch this type of exception as it is the one returned when the date
            // range is longer than a year. the message contained in the exception can
            // be displayed to the UI to inform the user
            //TODO: handle this exception
        }

    }
}
