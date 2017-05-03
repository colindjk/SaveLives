/*
 * Created by Pingxin Shang on 2017.04.26  * 
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
@Named(value = "historySearchController")
@SessionScoped
public class HistorySearchController implements Serializable {

    private List<SearchQuery> items = null;
    private SearchQuery selected;

    @EJB
    private UserFacade userFacade;

    @Inject
    private CrimeCaseController crimeCaseController;

    @Inject
    private PreferredSearchController preferredSearchController;

    @Inject
    private EditorView editorView;

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

    public void searchAgain() throws Exception {

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

    public void addToPreferred() {

        Map<String, Object> map = FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap();
        String userPrimaryKey = (String) map.get("user_id");
        User u = getUserFacade().findById(userPrimaryKey);

        for (int i = 0; i < u.getPreferredSearch().size(); i++) {
            if (u.getPreferredSearch().get(i).getIndex() == selected.getIndex()) {
                JsfUtil.addErrorMessage("Selected Search is already in preferred search!");
                return;
            }
        }
        u.addPreferredSearch(selected);
        System.out.println(u.toDocument());
        userFacade.edit(u);
        JsfUtil.addSuccessMessage("Successfully saved selected search to preferred search");
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
            Logger.getLogger(HistorySearchController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HistorySearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public void prepareEmailBodyForLastSearch() {

        try {
            String imageUrl = "<img src=\"https://data.baltimorecity.gov/api/assets/AF2C3AF6-C1EF-4A09-9B7D-70B8E3C695BE?ob_beta.png\" style=\"width:200px;\">";

            Map<String, Object> map = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap();
            String userPrimaryKey = (String) map.get("user_id");
            User u = getUserFacade().findById(userPrimaryKey);

            String url = "http://localhost:8080/MongoDBWeb/webresources/search/" + u.getUsername() + "/" + u.getHistorySearch().get(0).getIndex();

            // Compose the email content in HTML format
            String emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><h2>Open Baltimore</h2><br /><br />"
                    + u.getFirstName() + " sends you a search query. If you want to see the search result, please click <a href=\""
                    + url + "\">here</a> to view the search details and search the crime cases. <br /><br /> <p>&nbsp;</p></div>";

            // Set the HTML content to be the body of the email message
            editorView.setText(emailBodyText);

            // Redirect to show the SendMail.xhtml page
            FacesContext.getCurrentInstance().getExternalContext().redirect("SendEmail.xhtml");
        } catch (UnknownHostException ex) {
            Logger.getLogger(HistorySearchController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HistorySearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
