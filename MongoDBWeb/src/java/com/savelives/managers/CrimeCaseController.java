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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.map.MapModel;

/**
 *
 * @author taiwenjin
 */
@SessionScoped
@Named(value = "crimeCaseController")
public class CrimeCaseController implements Serializable {

    @EJB
    private final CrimeCaseFacade ejbFacade;
    private MapModel crimeModel;
    private final String mapIcon = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/resources/images/map-icon.png";
    private CrimeCase selected;
    private Date date1;
    private Date date2;
    /**
     * Default Constructor
     */
    public CrimeCaseController() {
        ejbFacade = new CrimeCaseFacade();
        crimeModel = ejbFacade.getCrimesModel();
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
    
    
    /***************************
     * Instance methods        *
     ***************************/
    public void submit(){
        System.out.println("SUBMITTED");
        crimeModel = null;
        crimeModel = getFacade().getCrimesByDateRange(date1, date2);
        
    }
}