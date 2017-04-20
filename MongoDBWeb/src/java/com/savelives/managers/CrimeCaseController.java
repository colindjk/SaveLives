/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

import com.savelives.entityclasses.CrimeCase;
import com.savelives.sessionbeans.CrimeCaseFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author taiwenjin
 */
@SessionScoped
@Named(value = "crimeCaseController")
public class CrimeCaseController implements Serializable {

    @EJB
    private final CrimeCaseFacade ejbFacade;
    private final MapModel crimeModel;
    private final String mapIcon = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/resources/images/map-icon.png";
    private List<CrimeCase> items = null;
    private CrimeCase selected;

    /**
     * Default Constructor
     */
    public CrimeCaseController() {
        ejbFacade = new CrimeCaseFacade();
        crimeModel = new DefaultMapModel();
        List<CrimeCase> crimeList = ejbFacade.getAll();
        crimeList.forEach((CrimeCase crime) -> {
            if (crime.hasLocation()) {
                crimeModel.addOverlay(new Marker(new LatLng(crime.getCoorY(), crime.getCoorX()), crime.getDescription(), crime, mapIcon));
            }
        });
    }

    /**
     * **************************
     * Getters and Setters *
     ***************************
     */
    private CrimeCaseFacade getFacade() {
        return ejbFacade;
    }

    public MapModel getCrimeModel() {
        return crimeModel;
    }

    public CrimeCase getSelected() {
        return selected;
    }

    /**
     * Get a list of every crime case
     *
     * @return list of crime cases
     */
    public List<CrimeCase> getItems() {
        if (items == null) {
            items = getFacade().getAll();
        }
        return items;
    }
}
