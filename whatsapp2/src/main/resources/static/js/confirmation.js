const search = document.getElementById("search-bar-text");
var datepicker = document.getElementById("datepicker");
var datepicked = "";


function loadContent(date) {

	var formattedDate = changeformat(date);

	axios
		.get("/confirmation/getdate", {
			params: {
				shippingDate: formattedDate,
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
		"<th>Data do Serviço</th>" +
		"<th>Medico</th>" +
		"<th>Serviço</th>" +
		"<th>Nome</th>" +
		"<th>Telefone</th>" +
		"<th>Hora</th>" +
		"<th>Resposta</th>" +
		"<th>Data do envio</th>" +
		"</tr>" +
		"</thead>" +
		"<tbody>";

	for (var i = 0; i < data.length; i++) {
		table_output +=
			'<tr id="' +
			data[i].id +
			'">' +
			'<th id="data">' +
			data[i].serviceDate +
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
				'<th style="background-color: green;color: white;display: flex;justify-content: center;border-color: green;" id="resposta">' +
				data[i].response +
				"</th>";
		} else if (data[i].response === "NÃO") {
			table_output +=
				'<th style="background-color: red;color: white;display: flex;justify-content: center;border-color: red;" id="resposta">' +
				data[i].response +
				"</th>";
		} else {
			table_output +=
				'<th style="display: flex;justify-content: center;" id="resposta">' +
				data[i].response +
				"</th>";
		}
		table_output += '<th id="data-envio">' + data[i].shippingDate + "</th>" + "</tr>";
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
		console.log(this.value);
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
