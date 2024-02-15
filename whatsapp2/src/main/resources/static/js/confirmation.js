import Websocket from "./websocket.js";
const websocket = new Websocket();

const search = document.getElementById("search-bar-text");
var datepicker = document.getElementById("datepicker");
var datepicked = "";

function onloadInit(){
	websocket.connect;
}

function loadContent(date) {

	var formattedDate = changeformat(date);

	axios
		.get("/confirmation/getdate", {
			params: {
				appointmentDate: formattedDate,
			},
		})
		.then(function(data) {
			if (data.status === 200) {
				//console.log(data);
				datepicked = date;
				htmlDisplay(data.data);
			} else {
				console.log(data);
			}
		});
}

// Display Data int html
function htmlDisplay(data) {
	var table_output =
		'<table class="table table-striped table-bordered">' +
		'<thead class="thead-dark">' +
		"<tr>" +
		"<th>Data Envio</th>" +
		"<th>Medico</th>" +
		"<th>Serviço</th>" +
		"<th>Nome</th>" +
		"<th>Telefone</th>" +
		"<th>Hora</th>" +
		"<th>Resposta</th>" +
		"<th>Data Consulta/Exame</th>" +
		"</tr>" +
		"</thead>" +
		"<tbody>";

	for (var i = 0; i < data.length; i++) {
		table_output +=
			'<tr id="' +
			data[i].id +
			'">' +
			'<th id="data-envio">' +
			data[i].shippingDate +
			"</th>" +
			'<th id="medico">' +
			data[i].doctor +
			"</th>" +
			'<th id="service">' +
			data[i].service +
			"</th>" +
			'<th id="name">' +
			data[i].name +
			"</th>" +
			'<th id="telefone">' +
			data[i].phoneNumber +
			"</th>" +
			'<th id="hora">' +
			data[i].hour +
			"</th>";
		if (data[i].response === "SIM") {
			table_output +=
				'<th style="background-color: green;color: white;display: flex;justify-content: center;border-color: #014900;" id="resposta">' +
				data[i].response +
				"</th>";
		} else if (data[i].response === "REMARCAR") {
			table_output +=
				'<th style="background-color: yellow;color: black;display: flex;justify-content: center;border-color: #d7b004;" id="resposta">' +
				data[i].response +
				"</th>";
		} else if (data[i].response === "VISUALIZADO") {
			table_output +=
				'<th style="background-color: #0066ff;color: white;display: flex;justify-content: center;border-color: #1800ff;" id="resposta">' +
				data[i].response +
				"</th>";		
		} else if (data[i].response === "CANCELADO") {
			table_output +=
				'<th style="background-color: red;color: white;display: flex;justify-content: center;border-color: #850000;" id="resposta">' +
				data[i].response +
				"</th>";
		} else if (data[i].response === "RECEBIDO") {
			table_output +=
				'<th style="background-color: #1200ff6e;color: white;display: flex;justify-content: center;border-color: #0052ff66;" id="resposta">' +
				data[i].response +
				"</th>";
		} else {
			table_output +=
				'<th style="display: flex;justify-content: center;" id="resposta">' +
				data[i].response +
				"</th>";
		}
		table_output += '<th id="data">' + data[i].serviceDate + "</th>" + "</tr>";
	}

	table_output += "</tbody>" + "</table>";

	document.getElementById("main-div").innerHTML = table_output;
}

function unixTimestampToDate(unix_timestamp) {
	var timeStamp = unix_timestamp * 1000;
	var dateFormat = new Date(timeStamp);

	var day = addZeroToDate(dateFormat.getDate());
	var month = addZeroToDate(dateFormat.getMonth() + 1);
	var year = addZeroToDate(dateFormat.getFullYear());
	var hours = addZeroToDate(dateFormat.getHours());
	var minutes = addZeroToDate(dateFormat.getMinutes());

	var date = day + "/" + month + "/" + year + " " + hours + ":" + minutes;
	return date;
}

function addZeroToDate(data) {
	if (data.toString().length <= 1) {
		data = "0" + data;
	}
	return data;
}

function findCustomer(search) {
	if (search !== " ") {
		axios
			.get("/confirmation/livesearch", {
				params: {
					input: search,
				},
			})
			.then(function(response) {
				if (response.status === 200) {
					document.getElementById("main-div").innerHTML = "";
					htmlDisplay(response.data);
				} else if (response.status === 404) {
					console.log("404 - Paciente não encontrado...");
				} else {
					console.log(response);
				}
			});
	}
}

search.addEventListener("input", function() {
	if (this.value !== "") {
		//console.log(this.value);
		findCustomer(this.value);
	} else {
		loadContent(datepicked);
	}
});

datepicker.addEventListener("input", function() {
	loadContent(this.value);
});



function changeformat(val) {
	
	const myArray = val.split("-");
	let year = myArray[0];
	let month = myArray[1];
	let day = myArray[2];
	let formatteddate = day + "/" + month + "/" + year;
	
	return formatteddate;
}

window.addEventListener('onload', onloadInit());
window.addEventListener('onload', onloadInit);
window.loadContent = loadContent;
window.findCustomer = findCustomer;
window.unixTimestampToDate = unixTimestampToDate;
