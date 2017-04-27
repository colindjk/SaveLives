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
import javax.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import javax.faces.bean.ManagedBean;
 
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import java.util.Calendar;
import java.util.Date;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
/**
 *
 * @author TaiwenJin
 */
@SessionScoped
@Named(value = "crimeCaseController")
public class CrimeCaseController implements Serializable {

    @EJB
    private final CrimeCaseFacade ejbFacade;
    private MapModel crimeModel;
    private CrimeCase selected;
    private Date date1;
    private Date date2;
    private final List<String> crimeCategories;
    private final List<String> crimeCodes;
    private List<String> selectedCategories;
    private List<String> selectedCrimeCodes;
    private final int NUMB_OF_CRIMES = 500;
    
    private BarChartModel barModel;
    /**
     * Default Constructor
     */
    public CrimeCaseController() {
        ejbFacade = new CrimeCaseFacade();

        crimeModel = ejbFacade.getCrimesModel(NUMB_OF_CRIMES);
        crimeCategories = ejbFacade.getDistinct("description");
        crimeCodes = ejbFacade.getDistinct("crimecode");
        
        createBarModel();
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
    
    /*========== Creating Charts ==============*/
    
    
    public BarChartModel getBarModel() {
        return barModel;
    }
    
    public void setBarModel(BarChartModel model) {
        this.barModel = model;
    }
    
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
            
            switch(cal3.get(Calendar.MONTH)) {
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
                    getFacade().getCrimesByDateRange(cal4.getTime(), cal3.getTime()).getMarkers().size()); 
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

    //============== INSTANCE METHODS =====================//
    public void onMarkerSelect(OverlaySelectEvent event) {
        selected = (CrimeCase) event.getOverlay();
    }
    public void submit() {
        crimeModel = null;
        if (selectedCategories == null || selectedCategories.isEmpty()) {
            // set this to all categories since the user has not chosen any category
            // otherwise the result will be an empty list of crimes. A workaround
            // would be to make category selection mandatory
            selectedCategories = this.getCrimeCategories();
        }
        if (selectedCrimeCodes == null || selectedCrimeCodes.isEmpty()) {
            selectedCrimeCodes = crimeCodes;
        }
        try {
            //Perform the filtering
            crimeModel = getFacade().filterCrimes(date1, date1, selectedCategories, selectedCrimeCodes);
        } catch (UnsupportedOperationException ex) {
            // only catch this type of exception as it is the one returned when the date
            // range is longer than a year. the message contained in the exception can
            // be displayed to the UI to inform the user
            //TODO: handle this exception
        }

    }
}
