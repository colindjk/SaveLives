/*
 * Created by Pingxin Shang on 2017.04.26  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.savelives.managers;

import com.savelives.entityclasses.SearchQuery;
import com.savelives.entityclasses.User;
import com.savelives.sessionbeans.UserFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ping
 */
@Named(value = "historySearchController")
@SessionScoped
public class HistorySearchController implements Serializable {

    private List<SearchQuery> items = null;
    private SearchQuery selected;

    @EJB
    private UserFacade userFacade;

    @Inject
    private CrimeCaseController crimeCaseController;

    public HistorySearchController() {
    }

    public List<SearchQuery> getItems() {
        Map<String, Object> map = FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap();
        String userPrimaryKey = (String) map.get("user_id");
        User u = getUserFacade().findById(userPrimaryKey);
        this.items = u.getHistorySearch();

        return items;
    }

    public void setItems(List<SearchQuery> items) {
        this.items = items;
    }

    public SearchQuery getSelected() {
        return selected;
    }

    public void setSelected(SearchQuery selected) {
        this.selected = selected;

    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void searchAgain() {

        crimeCaseController.setDate1(selected.getFrom());
        crimeCaseController.setDate2(selected.getTo());
        crimeCaseController.setSelectedCrimeCodes(selected.getCrimeCodes());
        crimeCaseController.setSelectedCategories(selected.getCategories());
        crimeCaseController.submitWithoutAddHistory();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("CrimeMap.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(HistorySearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
