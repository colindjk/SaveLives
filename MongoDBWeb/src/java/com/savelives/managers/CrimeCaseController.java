/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

import com.savelives.entityclasses.CrimeCase;
import com.savelives.sessionbeans.CrimeCaseFacade;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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

    
    private List<CrimeCase> items = null;
    private CrimeCase selected;
    
    /**
     * Default Constructor
     */
    public CrimeCaseController(){
        ejbFacade = new CrimeCaseFacade();
        crimeModel = new DefaultMapModel();
        List<CrimeCase> crimeList = ejbFacade.getAll();
        
        //no filter
        crimeList.forEach((CrimeCase crime) -> {
            crimeModel.addOverlay(new Marker(new LatLng(crime.getCoorY() , crime.getCoorX()), crime.getDescription()));
        });
    }
    
    public void populateMap() {
        
        crimeModel.getMarkers().clear();
        
        //List<CrimeCase> crimeList
        //call method returnCrimeLists
            //(string startDate, string endDate, string crimeType, string weapon, string district)
        
        //crimeList.forEach((CrimeCase crime) -> {
        //    crimeModel.addOverlay(new Marker(new LatLng(crime.getCoorY() , crime.getCoorX()), crime.getDescription()));
        //});
        
        
    }
    
    /****************************
     * Getters and Setters      *
     ****************************/
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
     * @return list of crime cases
     */
    public List<CrimeCase> getItems(){
        if(items == null) {
            items = getFacade().getAll();
        }
        return items;
    }
}
