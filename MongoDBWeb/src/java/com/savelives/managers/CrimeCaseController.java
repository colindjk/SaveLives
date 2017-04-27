/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

import com.mycompany.jsfclasses.util.JsfUtil;
import com.savelives.entityclasses.CrimeCase;
import com.savelives.entityclasses.SearchQuery;
import com.savelives.sessionbeans.CrimeCaseFacade;
import com.savelives.sessionbeans.UserFacade;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
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

    private final CrimeCaseFacade ejbFacade;
    private MapModel crimeModel;
    private CrimeCase selected;
    private final List<String> weapons;
    private List<String> selectedWeapons;
    private final List<String> neighborhoods;
    private List<String> selectedNeighborhoods;
    private Date date1;
    private Date date2;
    private final List<String> crimeCategories;
    private final List<String> crimeCodes;
    private List<String> selectedCategories;
    private List<String> selectedCrimeCodes;
    private final int NUMB_OF_CRIMES = 500;

    @Inject
    private AccountManager accountManager;

    @EJB
    private UserFacade userFacade;

    /**
     * Default Constructor
     */
    public CrimeCaseController() {
        ejbFacade = new CrimeCaseFacade();

        crimeModel = ejbFacade.getCrimesModel(NUMB_OF_CRIMES);
        crimeCategories = ejbFacade.getDistinct("description");
        crimeCodes = ejbFacade.getDistinct("crimecode");
        weapons = ejbFacade.getDistinct("weapon");
        neighborhoods = ejbFacade.getDistinct("neighborhood");
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

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public List<String> getWeapons() {
        return weapons;
    }

    public List<String> getSelectedWeapons() {
        return selectedWeapons;
    }

    public void setSelectedWeapons(List<String> selectedWeapons) {
        this.selectedWeapons = selectedWeapons;
    }

    public void setSelectedNeighborhoods(List<String> selectedNeighborhoods) {
        this.selectedNeighborhoods = selectedNeighborhoods;
    }

    public List<String> getNeighborhoods() {
        return neighborhoods;
    }

    public List<String> getSelectedNeighborhoods() {
        return selectedNeighborhoods;
    }

    //============== INSTANCE METHODS =====================//
    public void onMarkerSelect(OverlaySelectEvent event) {
        selected = (CrimeCase) event.getOverlay();
    }

    public void submit() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        crimeModel = null;
        crimeModel = getFacade().filterCrimes(date1, date2, selectedCrimeCodes, selectedCategories, selectedWeapons, selectedNeighborhoods);

        if (accountManager.isLoggedIn()) {
            SearchQuery sq = new SearchQuery(LocalDateTime.now(), date1, date2,
                    (ArrayList<String>) selectedCrimeCodes, (ArrayList<String>) selectedCategories);
            accountManager.getSelected().addHistorySearch(sq);
            getUserFacade().edit(accountManager.getSelected());
            //User u = getUserFacade().findById(accountManager.getSelected().getId());
            //System.out.println(u.toDocument());
        }
    }

    public void submitWithoutAddHistory() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        crimeModel = null;
        crimeModel = getFacade().filterCrimes(date1, date2, selectedCrimeCodes, selectedCategories, selectedWeapons, selectedNeighborhoods);
    }

}
