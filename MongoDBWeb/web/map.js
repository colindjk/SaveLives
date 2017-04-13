/* 
 * Created by Jessica Sun on 2017.04.13  * 
 * Copyright © 2017 Jessica Sun. All rights reserved. * 
 */


/* 
 * Created by Osman Balci on 2016.07.29  * 
 * Copyright © 2016 Osman Balci. All rights reserved. * 
 */

/******************************************************************** 
 We use Google Maps JavaScript API for Maps and Directions: 
 https://developers.google.com/maps/documentation/javascript/
 Here, we use the Google Maps JavaScript API without a developer key.
 Developer Key can be obtained at the above URL when needed.
 *******************************************************************/

/* Global variables */

var google;

// Object reference 'map' to point to a Google Map object
var map;

// Object reference 'currentMarker' to point to a VT building location on map
var currentMarker = null;

/*
You can obtain directions via driving, bicycling, bus, or walking by using the DirectionsService object.
This object communicates with the Google Maps API Directions Service which receives direction requests
and returns computed results. You can handle these directions results by using the DirectionsRenderer
object to render these results. [https://developers.google.com/maps/documentation/javascript/directions]
 */

// Instantiate a DirectionsService object and store its object reference into directionsService.
var directionsService = new google.maps.DirectionsService();

// Instantiate a DirectionsRenderer object and store its object reference into directionsDisplay.
var directionsDisplay = new google.maps.DirectionsRenderer();

// Create and display a Virginia Tech campus map
function initializeMap() {

    /*
     Instantiate a new Virginia Tech campus map object and set its properties.
     document.getElementById('map') --> Obtains the Google Map style definition
     from the div element with id="map" in ShowOnMap.xhtml 
     */
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: {lat: 39.2903848, lng: -76.6121893},
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.BOTTOM_LEFT
        },
        mapTypeId: google.maps.MapTypeId.HYBRID
    });
    
    // Show the desired map using the map created above by calling the display() function.
    display();
    
}

/*
 The Baltimore map created in the initializeMap() function above is used to show:
     the location of a single Crime, or
     locations of Crimes in a given date.
 */
function display() {
    if (document.getElementById("crimeCode").value !== '') {
        /*
         If buildingName has a value, the user asked for the location of a single Baltimore.
         Show the location of the Baltimore on the VT campus map created in the initializeMap() function.
         */
        displaySingleCrime();
    }
    else {
        /*
         Show the locations of Baltimore in a given building category on
         the Baltimore map created in the initializeMap() function.
         */
        displayCrimesByDate();
    }
    
}


// Displays the geolocation of the selected VT building on the VT campus map created in the initializeMap() function.
function displaySingleCrime() {
    
    // Obtain the selected VT building's name from the hidden input element with id="buildingName" in ShowOnMap.xhtml 
    var crimeCode = document.getElementById("crimeCode").value;
    
    // Obtain the selected VT building's Latitude value from the hidden input element with id="buildingLat" in ShowOnMap.xhtml 
    var crimeCoorX = document.getElementById("crimeCoorX").value;
    
    // Obtain the selected VT building's Longitude value from the hidden input element with id="buildingLong" in ShowOnMap.xhtml
    var crimeCoorY = document.getElementById("crimeCoorY").value;
    
    // Determine the geolocation of the selected VT building
    var crimeCoor = new google.maps.LatLng(crimeCoorX, crimeCoorY);

    // Set the center of the map to the geolocation coordinates of the selected VT building
    map.setCenter(crimeCoor);

    // Instantiate a new pin marker and dress it up with the selected VT building's properties
    currentMarker = new google.maps.Marker({
        title: crimeLocation,
        position: crimeCoor,
        map: map
    });

    // Place the newly created pin marker on the VT campus map
    currentMarker.setMap(map);

    // Instantiate a new InfoWindow object to display the VT building's name when the pin marker is clicked
    var infoWindow = new google.maps.InfoWindow();

    // Attach an event handler to currentMarker to display the VT building's name when the pin marker is clicked
    google.maps.event.addListener(currentMarker, "click", function() {

        infoWindow.setContent(this.get('title'));  // Show the VT building's name
        
        infoWindow.open(map, this);   // Use the map created here (map is a global variable)
    });

}

// Displays the geolocations of all VT buildings in the given category
function displayCrimesByDate() {
    
    var jsonData = JSON.parse(document.getElementById("jsonCategoryResult").value);
    
    // Obtain the number of VT buildings (JavaScript objects) in the given category
    var numberOfBuildings = jsonData.length;

    // Instantiate a new InfoWindow object to display the VT building's name when the pin marker is clicked
    var infoWindow = new google.maps.InfoWindow();

    j = 0;

    // Iterate for all VT buildings in the given category (e.g., Academic, Athletic, Research)
    while (j < numberOfBuildings) {
        
        var marker = null;
        
        // Instantiate a new pin marker and dress it up with the VT building's properties
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(jsonData[j].coorX, jsonData[j].coorY),
            map: map,
            title: jsonData[j].name
        });

        // Place the newly created pin marker on the VT campus map
        marker.setMap(map);

        // Attach an event handler to the pin marker to display the VT building's name when the pin marker is clicked
        google.maps.event.addListener(marker, "click", function () {
            infoWindow.setContent(this.get('title'));
            infoWindow.open(map, this);
        });

        j++;
    }

}
