var stompClient = null;
const dropdown = document.querySelector("#exit");

function connect() {
	websocketFunction();
	loadWantsToTalk();
}

// ********** Websocket connection
function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function websocketFunction() {
	// HTTPS for TLS conncetions VPS
	//var socket = new SockJS("https://www.web.login.imav.com.br:5000/websocket-server");

	// Localhost
	var socket = new SockJS("http://localhost:5000/websocket-server");

	stompClient = Stomp.over(socket);
	stompClient.connect({}, function() {
		setConnected(true);

		// Update reload DOM
		stompClient.subscribe("/topic/talks", function(response) {
			//console.log(response);
			loadWantsToTalk();
		});
	});
}

function loadWantsToTalk() {
	axios({
		method: "get",
		url: "/talk/all",
	}).then(function(response) {
		if (response.status === 200) {
			//console.log(response);
			wantsTotalkToHtml(response.data);
		} else if (response.status === 404) {
			console.log("404 - Pacientes não encontrados...");
		} else {
			console.log(response);
		}
	});
}

function wantsTotalkToHtml(data) {

	var dataLength = data.length;
	var dataHtml = "";
	document.getElementById("exit").innerHTML = dataHtml;

	if (dataLength > 0) {
		dataHtml = '<button class="dropbtn">'
			+ '<div id="alert-div">'
			+ '<div id="alert-number">' + dataLength + '</div>'
			+ '</div>'
			+ '</button>'
			+ '<div class="dropdown-content" id="myDropdown">';

		for (var i = 0; i < data.length; i++) {
			dataHtml += '<p>' + data[i].name + ' - ' + data[i].phone + '</p>';
		}

		dataHtml += '</div>';
	}
	document.getElementById("exit").innerHTML = dataHtml;
}


/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */
function myDropdown() {
	document.getElementById("myDropdown").classList.toggle("show");
}

// add a click event listener to the div
dropdown.addEventListener("click", function() {
	myDropdown();
});
