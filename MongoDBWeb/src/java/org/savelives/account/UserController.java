/*
 * Created by Pingxin Shang on 2017.03.13  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package org.savelives.account;

/*
 * @author ping
 */
import org.savelives.sessionbeans.UserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

@Named(value = "userController")
@SessionScoped
public class UserController implements Serializable {

    public static final String[] QUESTIONS = {
        "In what city were you born?",
        "What is your mother's maiden name?",
        "What elementary school did you attend?",
        "What was the make of your first car?",
        "What is your father's middle name?",
        "What is the name of your most favorite pet?",
        "What street did you grow up on?"
    };

    private final String[] listOfStates = {"AK", "AL", "AR", "AZ", "CA", "CO", "CT",
        "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA",
        "MD", "ME", "MH", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM",
        "NV", "NY", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT",
        "VA", "VI", "VT", "WA", "WI", "WV", "WY"};

    private User user = new User();
    private User currentUser;
    private String currentUserSecurityQuestion;
    private String enteredSecurityAnswer;
    private String filter = "";
    private String createAccountMessage = "";
    private String editAccountMessage = "";
    private String answerSecurityQuestionMessage = "";
    private String signInMessage = "";
    private Map<String, Object> security_questions;
    private String username;
    private String password;

    @EJB
    private UserFacade userEJB;
    private List<User> list = new ArrayList<>();

    public UserController() {
    }

    @PostConstruct
    private void init() {
        find();
    }

    public String createAccount() {

        User aUser = getUserEJB().findByUsername(user.getUsername());

        if (aUser != null) {
            user.setUsername("");
            createAccountMessage = "Username already exists! Please select a different one!";
            return "";
        }

        if (createAccountMessage == null || createAccountMessage.isEmpty()) {
            try {
                getUserEJB().create(user);
            } catch (EJBException e) {
                user.setUsername("");
                System.out.println(e);
                createAccountMessage = "Cannot add to database";
                return "";
            }
            initializeSessionMap();
            user = new User();

            return "SignIn.xhtml?faces-redirect=true";
        }
        return "";
    }

    public String editAccount() {
        
        if (!isLoggedIn()){
            editAccountMessage = "You are not logged in. Please log in first!";
            return "SignIn.xhtml?faces-redirect=true";
        }

        User u = getCurrentUser();       
        getUserEJB().update(u);        
        return "Profile.xhtml?faces-redirect=true";
    }

    public String loginUser() {

        User u = getUserEJB().findByUsername(getUsername());

        if (u == null) {
            signInMessage = "Entered username " + getUsername() + " does not exist!";
            return "";

        } else {
            String actualUsername = u.getUsername();
            String enteredUsername = getUsername();
            String actualPassword = u.getPassword();
            String enteredPassword = getPassword();

            if (!actualUsername.equals(enteredUsername)) {
                signInMessage = "Invalid Username!";
                return "";
            }
            if (!actualPassword.equals(enteredPassword)) {
                signInMessage = "Invalid Password!";
                return "";
            }
            signInMessage = "";
            initializeSessionMap(user);

            return "Profile.xhtml?faces-redirect=true";
        }
    }
    
    public String checkEnterAnswer() {
        
        String answer = getCurrentUser().getSecurityAnswer();
        
        if(enteredSecurityAnswer.equals(answer)){
            return "ResetPassword.xhtml?faces-redirect=true";
        }
        
        answerSecurityQuestionMessage = "The answer you entered is incorrect!";
        return "";
    }

    public boolean isLoggedIn() {

        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null;
    }

    public String logout() {

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        user = new User();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        currentUser = null;
        return "index.xhtml?faces-redirect=true";
    }

    public void initializeSessionMap() {

        User u = getUserEJB().findByUsername(user.getUsername());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user", u);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", u.getId());
    }

    public void initializeSessionMap(User user) {
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("first_name", user.getFirstName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("last_name", user.getLastName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("username", username);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

    public void validateInformation(ComponentSystemEvent event) {

        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String entered_password = uiInputPassword.getLocalValue()
                == null ? "" : uiInputPassword.getLocalValue().toString();
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String entered_confirm_password = uiInputConfirmPassword.getLocalValue()
                == null ? "" : uiInputConfirmPassword.getLocalValue().toString();

        if (entered_password.isEmpty() || entered_confirm_password.isEmpty()) {
            return;
        }

        if (!entered_password.equals(entered_confirm_password)) {
            createAccountMessage = "Password and Confirm Password must match!";
        } else {
            createAccountMessage = "";
        }
    }

    public void find() {
        list = userEJB.find(filter);
    }

    public void delete() {
        userEJB.delete(user);
        find();
    }

    public String getCreateAccountMessage() {
        return createAccountMessage;
    }

    public void setCreateAccountMessage(String createAccountMessage) {
        this.createAccountMessage = createAccountMessage;
    }

    public String getSignInMessage() {
        return signInMessage;
    }

    public void setSignInMessage(String signInMessage) {
        this.signInMessage = signInMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCurrentUser() {

        if (currentUser == null) {

            String un = (String) FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("username");

            currentUser = getUserEJB().find(un).get(0);
        }
        // Return the object reference of the selected User object
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public UserFacade getUserEJB() {
        return userEJB;
    }

    public void setUserEJB(UserFacade userEJB) {
        this.userEJB = userEJB;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getSecurity_questions() {

        if (security_questions == null) {
            security_questions = new LinkedHashMap<>();

            for (int i = 0; i < QUESTIONS.length; i++) {
                security_questions.put(QUESTIONS[i], i);
            }
        }
        return security_questions;
    }

    public void setSecurity_questions(Map<String, Object> security_questions) {
        this.security_questions = security_questions;
    }

    public String[] getListOfStates() {
        return listOfStates;
    }

    public String getEditAccountMessage() {
        return editAccountMessage;
    }

    public void setEditAccountMessage(String editAccountMessage) {
        this.editAccountMessage = editAccountMessage;
    }

    public String getAnswerSecurityQuestionMessage() {
        return answerSecurityQuestionMessage;
    }

    public void setAnswerSecurityQuestionMessage(String answerSecurityQuestionMessage) {
        this.answerSecurityQuestionMessage = answerSecurityQuestionMessage;
    }

    public String getCurrentUserSecurityQuestion() {
        User u = getCurrentUser();
        int i = u.getSecurityQuestion();
        return QUESTIONS[i];
    }

    public void setCurrentUserSecurityQuestion(String currentUserSecurityQuestion) {
        this.currentUserSecurityQuestion = currentUserSecurityQuestion;
    }

    public String getEnteredSecurityAnswer() {
        return enteredSecurityAnswer;
    }

    public void setEnteredSecurityAnswer(String enteredSecurityAnswer) {
        this.enteredSecurityAnswer = enteredSecurityAnswer;
    }

    
    
}
