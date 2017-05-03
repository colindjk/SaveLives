/*
 * Created by Pingxin Shang on 2017.05.03  * 
 * Copyright © 2017 Pingxin Shang. All rights reserved. * 
 */
package com.savelives.managers;

import com.mycompany.jsfclasses.util.JsfUtil;
import com.savelives.entityclasses.SearchQuery;
import com.savelives.entityclasses.User;
import com.savelives.sessionbeans.UserFacade;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
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
@Named(value = "preferredSearchController")
@SessionScoped
public class PreferredSearchController implements Serializable {

    private List<SearchQuery> items = null;
    private SearchQuery selected;
    private String name;

    @EJB
    private UserFacade userFacade;

    @Inject
    private CrimeCaseController crimeCaseController;

    @Inject
    private EditorView editorView;

    public PreferredSearchController() {
    }

    public List<SearchQuery> getItems() {
        Map<String, Object> map = FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap();
        String userPrimaryKey = (String) map.get("user_id");
        User u = getUserFacade().findById(userPrimaryKey);
        this.items = u.getPreferredSearch();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changeName() {

        Map<String, Object> map = FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap();
        String userPrimaryKey = (String) map.get("user_id");
        User u = getUserFacade().findById(userPrimaryKey);
        for (int i = 0; i < u.getPreferredSearch().size(); i++) {
            if (u.getPreferredSearch().get(i).getIndex() == selected.getIndex()) {
                u.getPreferredSearch().get(i).setName(name);
                userFacade.edit(u);
                return;
            }
        }

    }

    public void delete() {

        Map<String, Object> map = FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap();
        String userPrimaryKey = (String) map.get("user_id");
        User u = getUserFacade().findById(userPrimaryKey);
        for (int i = 0; i < u.getPreferredSearch().size(); i++) {
            if (u.getPreferredSearch().get(i).getIndex() == selected.getIndex()) {
                u.getPreferredSearch().remove(i);
                userFacade.edit(u);
                JsfUtil.addSuccessMessage("Successfully deleted the seleted search from preferred search");
                return;
            }
        }

    }

    public void searchAgain() {

        crimeCaseController.setDate1(selected.getFrom());
        crimeCaseController.setDate2(selected.getTo());
        crimeCaseController.setSelectedCrimeCodes(selected.getCrimeCodes());
        crimeCaseController.setSelectedCategories(selected.getCategories());
        try {
            crimeCaseController.submitWithoutAddHistory();

            FacesContext.getCurrentInstance().getExternalContext().redirect("CrimeMap.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(PreferredSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareEmailBody() {

        try {
            String imageUrl = "<img src=\"https://data.baltimorecity.gov/api/assets/AF2C3AF6-C1EF-4A09-9B7D-70B8E3C695BE?ob_beta.png\" style=\"width:200px;\">";

            Map<String, Object> map = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap();
            String userPrimaryKey = (String) map.get("user_id");
            User u = getUserFacade().findById(userPrimaryKey);

            String url = "http://localhost:8080/MongoDBWeb/webresources/search/" + u.getUsername() + "/" + selected.getIndex();

            // Compose the email content in HTML format
            String emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><h2>Open Baltimore</h2><br /><br />"
                    + u.getFirstName() + " sends you a search query. If you want to see the search result, please click <a href=\""
                    + url + "\">here</a> to view the search details and search the crime cases. <br /><br /> <p>&nbsp;</p></div>";

            // Set the HTML content to be the body of the email message
            editorView.setText(emailBodyText);

            // Redirect to show the SendMail.xhtml page
            FacesContext.getCurrentInstance().getExternalContext().redirect("SendEmail.xhtml");
        } catch (UnknownHostException ex) {
            Logger.getLogger(PreferredSearchController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PreferredSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}