function getStudentData() {
	// This must be implemented by you. The json variable should be fetched
	// from the server, not initiated with a static value as below. 
	// You must first download the student json data from the server
	// then call populateStudentTable(json);
	// and then populateStudentLocationForm(json);

	//   call from index

	$.getJSON("http://localhost:8080/assignment2-gui/api/student.json",
			function(json) {
				console.log("from getStudentData()  " + json);
				populateStudentTable(json);
				populateStudentLocationForm(json);
			}).done(function(json) {
		console.log("from  getStudentData() .done ");
	}).fail(function(jqxgr, textStatus, error) {
		var err = textStatus + " , " + error;
		console.log("request failed getStudentData() : " + err);
	});
}
function populateStudentTable(json) {
	// for each student make a row in the student location table
	// and show the name, all courses and location.
	// if there is no location print "No location" in the <td> instead
	// tip: see populateStudentLocationForm(json) og google how to insert html from js with jquery. 
	// Also search how to make rows and columns in a table with html

	// the table can you see in index.jsp with id="studentTable"
	$('#studentTable tbody').remove();
	console.log(" from popStudenTable 1 " + json);
	var formStringTable = [];
	for (var s = 0; s < json.length; s++) {
		formStringTable[s] = '<tr>';
		var data;
		data = json[s];
		data = explodeJSON(data);
		var courses = "";

		for (var i = 0; i < data.courses.length; i++) {

			courses += data.courses[i].courseCode + "  ";
		}
		var latLong;
		if (data.latitude == null && data.longitude == null)
			latLong = "no location ";
		else
			latLong = data.latitude + " / " + data.longitude;

		formStringTable[s] += '<td>' + data.name + '</td>' + '<td>' + " "
				+ courses + " " + '</td>' + '<td>' + latLong + '</td>'
				+ '</tr>';

		var myLatlng = new google.maps.LatLng(data.latitude, data.longitude);
		var marker = new google.maps.Marker({
			position : myLatlng,
			map : map,
			title : data.name
		});
	}

	console.log("from popStudentTable 2 " + formStringTable);

	$('#studentTable').append(formStringTable.join(''));
}

function populateStudentLocationForm(json) {
	// console.log("from popStudentLocationForm 1 : "+json);
	var formString = '<tr><td><select id="selectedStudent" name="students">';
	for (var s = 0; s < json.length; s++) {
		var student = json[s];
		student = explodeJSON(student);
		formString += '<option value="' + student.id + '">' + student.name
				+ '</option>';
	}
	formString += '</select></td></tr>';
	// console.log("from popStudentLocationForm 2 :"+formString);

	$('#studentLocationTable').append(formString);

}

$('#locationbtn').on('click', function(e) {
	e.preventDefault();
	get_location();
});

// This function gets called when you press the Set Location button
function get_location() {
	if (Modernizr.geolocation) {
		navigator.geolocation.getCurrentPosition(location_found);
	} else {
		// no native support; maybe try a fallback?
	}
}

// Call this function when you've succesfully obtained the location.
function location_found(position) {
	// Extract latitude and longitude and save on the server using an AJAX call.
	// When you've updated the location, call populateStudentTable(json); again
	// to put the new location next to the student on the page. .
	var selector = document.getElementById('selectedStudent');
	var st = selector[selector.selectedIndex].value;
	var lat = position.coords.latitude;
	var long = position.coords.longitude;
	alert("FUNCTION  location_found   CALLED!");
	$.getJSON(
			"http://localhost:8080/assignment2-gui/api/student/" + st
					+ "?latitude=" + lat + "&longitude=" + long,
			function(json) {
				populateStudentTable(json);
			}).done(function(json) {
		console.log("from  location found() .done ");
	}).fail(function(jqxgr, textStatus, error) {
		var err = textStatus + " , " + error;
		console.log("request failed location found()  : " + err);
	});

}

var objectStorage = new Object();

function explodeJSON(object) {
	if (object instanceof Object == true) {
		objectStorage[object['@id']] = object;
		console.log("from explodeJSON 1 : " + 'Object is object');
	} else {
		console.log('Object is not object');
		object = objectStorage[object];
		console.log(object);
	}
	console.log("from explodeJSON 2 :  " + object);
	return object;
}
var map;
function initialize_map() {
	var mapOptions = {
		zoom : 10,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	// Try HTML5 geolocation
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var pos = new google.maps.LatLng(position.coords.latitude,
					position.coords.longitude);
			map.setCenter(pos);
		}, function() {
			handleNoGeolocation(true);
		});
	} else {
		// Browser doesn't support Geolocation
		// Should really tell the user…
	}
}
