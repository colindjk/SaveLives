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
import javax.inject.Named;

/**
 *
 * @author taiwenjin
 */
@SessionScoped
@Named(value = "crimeCaseController")
public class CrimeCaseController implements Serializable {
    @EJB
    private CrimeCaseFacade ejbFacade;
    
    private List<CrimeCase> items = null;
    private CrimeCase selected;
    
    /**
     * Default Constructor
     */
    public CrimeCaseController(){
    }
    
    private CrimeCaseFacade getFacade() {
        return ejbFacade;
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
