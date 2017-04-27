/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

import com.savelives.entityclasses.CrimeCase;
import com.savelives.entityclasses.SearchQuery;
import com.savelives.sessionbeans.CrimeCaseFacade;
import com.savelives.sessionbeans.UserFacade;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import java.util.Calendar;
import java.util.Date;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;

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

    private BarChartModel barModel;

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

        createBarModel();
    }

    public void populateMap() {

        crimeModel.getMarkers().clear();
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

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel model) {
        this.barModel = model;
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

    /*========== Creating Charts ==============*/
    private void createBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries crimes = new ChartSeries();
        crimes.setLabel("Crimes");

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        if (date1 == null && date2 == null) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2015);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            date1 = cal.getTime();

            cal.set(Calendar.YEAR, 2016);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            date2 = cal.getTime();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        int month1 = cal.get(Calendar.MONTH);
        int year1 = cal.get(Calendar.YEAR);
        cal.setTime(date2);
        int month2 = cal.get(Calendar.MONTH);
        int year2 = cal.get(Calendar.YEAR);

        int totalMonths = 0;

        if (year2 == year1) {
            totalMonths = month2 - month1 + 1;
        } else if (year2 - year1 < 2) {
            totalMonths = (12 - month1 + 1) + month2;
        } else {
            totalMonths = (year2 - year1 - 1) * 12 + (12 - month1 + 1) + month2;
        }

        int multiple = totalMonths / 12;

        //beginning of x-axis
        cal.setTime(date1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        /*
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date1);
        switch(cal2.get(Calendar.MONTH)) {
            case 0:
                cal2.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 1:
                cal2.set(Calendar.DAY_OF_MONTH, 28);
                break;
            case 2:
                cal2.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 3:
                cal2.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 4:
                cal2.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 5:
                cal2.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 6:
                cal2.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 7:
                cal2.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 8:
                cal2.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 9:
                cal2.set(Calendar.DAY_OF_MONTH, 31);
                break; 
            case 10:
                cal2.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 11:
                cal2.set(Calendar.DAY_OF_MONTH, 31);
                break;    
            default:
                break;
        }
        
        Calendar cal5 = Calendar.getInstance(); //first day of month
        cal5.setTime(date1);
        cal5.set(Calendar.DAY_OF_MONTH, 1);
        
        crimes.set(f.format(cal.getTime()),    
                getFacade().getCrimesByDateRange(cal5.getTime(), cal2.getTime()).getMarkers().size()); //return number of crimes within first month
         */
        for (int i = 1; i < 12; i++) {
            cal.add(Calendar.MONTH, multiple);

            Calendar cal3 = Calendar.getInstance(); //last day of month
            cal3.setTime(cal.getTime());

            Calendar cal4 = Calendar.getInstance(); //first day of month
            cal4.setTime(cal.getTime());
            cal4.set(Calendar.DAY_OF_MONTH, 1);

            switch (cal3.get(Calendar.MONTH)) {
                case 0:
                    cal3.set(Calendar.DAY_OF_MONTH, 31);
                    break;
                case 1:
                    cal3.set(Calendar.DAY_OF_MONTH, 28);
                    break;
                case 2:
                    cal3.set(Calendar.DAY_OF_MONTH, 31);
                    break;
                case 3:
                    cal3.set(Calendar.DAY_OF_MONTH, 30);
                    break;
                case 4:
                    cal3.set(Calendar.DAY_OF_MONTH, 31);
                    break;
                case 5:
                    cal3.set(Calendar.DAY_OF_MONTH, 30);
                    break;
                case 6:
                    cal3.set(Calendar.DAY_OF_MONTH, 31);
                    break;
                case 7:
                    cal3.set(Calendar.DAY_OF_MONTH, 31);
                    break;
                case 8:
                    cal3.set(Calendar.DAY_OF_MONTH, 30);
                    break;
                case 9:
                    cal3.set(Calendar.DAY_OF_MONTH, 31);
                    break;
                case 10:
                    cal3.set(Calendar.DAY_OF_MONTH, 30);
                    break;
                case 11:
                    cal3.set(Calendar.DAY_OF_MONTH, 31);
                    break;
                default:
                    break;
            }

            //return number of crimes within this month
            //String temp = f.format(cal.getTime());
            crimes.set(f.format(cal.getTime()),
                    getFacade().filterCrimes(cal4.getTime(), cal3.getTime(), selectedCrimeCodes, selectedCategories, selectedWeapons, selectedNeighborhoods).getMarkers().size());
        }

        model.addSeries(crimes);

        barModel = model;

        barModel.getAxis(AxisType.Y).setLabel("Crime Count");
        barModel.getAxis(AxisType.Y).setTickFormat("%d");
        barModel.getAxis(AxisType.Y).setMin(0);
        barModel.getAxis(AxisType.Y).setMax(6000);

        DateAxis axis = new DateAxis("Date");
        axis.setTickAngle(-50);
        axis.setMin(f.format(date1));
        axis.setMax(f.format(date2));
        axis.setTickFormat("%b, %Y");
        barModel.getAxes().put(AxisType.X, axis);

        barModel.setBarWidth(45);

        barModel.setTitle("Crimes in Baltimore");
        barModel.setLegendPosition("ne");
    }
}
