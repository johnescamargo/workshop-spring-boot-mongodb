var select = document.getElementById("select-div");
var cssWrong = "border: 2px solid #ff0000;";
var cssRight = "border: 1px solid #ced4da;";

function onloadInit() {
	getSelectData();
	connect();
}

function getSelectData() {

	axios({
		method: "get",
		url: "/countries/getcountries",
	}).then(function(response) {
		if (response.status === 200) {
			//console.log(response);
			setSelectHtml(response.data);
		} else if (response.status === 404) {
			console.log("404 - Lista de paises não encontrada...");
		} else {
			console.log(response);
		}
	});
}

function sendMessage() {
	document.getElementById("myModal").style.display = "block";
	var countryCodeSelect = document.getElementById("countryCodeSelect").value;
	var phone = document.getElementById("telephone").value;
	var name = document.getElementById("name").value;
	var text = document.getElementById("textarea").value;
	var messages = text.split("\n");

	//console.log(messages);

	axios
		.post("/message/envia", {
			internacionalCode: countryCodeSelect,
			phone: phone,
			name: name,
			messages: messages,
		})
		.then(function(response) {
			if (response.status === 200) {
				document.getElementById("myModal").style.display = "none";
				messageResponse(response.data)
			} else if (response.status === 400) {
				console.log(response);
			} else {
				console.log(response);
			}
		});
}

function messageResponse(data) {

	if (data) {
		snackbarShowSuccess();
	} else {
		snackbarShowError();
	}
}

function setSelectHtml(data) {

	var selectHtml = '<select id="countryCodeSelect">'
		+ '<option value="55">Brasil (+55)</option>';

	for (var i = 0; i < data.length; i++) {
		selectHtml += '<option value="' + data[i].code + '">' + data[i].country + ' (+' + data[i].code + ')</option>';
	}

	selectHtml += '</select>';

	select.innerHTML = selectHtml;
}

function validateMessage() {
	var code = validateCode();
	var phone = validatePhoneNumber();
	var message = validateTextArea();
	var name = validateName();

	if (code && phone && message && name) {
		sendMessage();
	}
}

function validateCode() {
	var countryCodeSelect = document.getElementById("countryCodeSelect");
	var valueSelect = countryCodeSelect.options[countryCodeSelect.selectedIndex].value;

	if (valueSelect) {
		document.getElementById("countryCodeSelect").style = cssRight;
		return true;
	} else {
		document.getElementById("countryCodeSelect").style = cssWrong;
		return false;
	}
}

function validatePhoneNumber() {
	var phone = document.getElementById("telephone").value;

	if (phone) {
		if (!isNaN(phone)) {
			document.getElementById("telephone").style = cssRight;
			return true;
		} else {
			document.getElementById("telephone").style = cssWrong;
			return false
		}
	} else {
		document.getElementById("telephone").style = cssWrong;
		return false;
	}
}

function validateTextArea() {
	var text = document.getElementById("textarea").value;

	if (text) {
		document.getElementById("textarea").style = cssRight;
		return true;
	} else {
		document.getElementById("textarea").style = cssWrong;
		return false;
	}
}

function validateName() {
	var name = document.getElementById("name").value;

	if (name) {
		document.getElementById("name").style = cssRight;
		return true;
	} else {
		document.getElementById("name").style = cssWrong;
		return false;
	}
}

function snackbarShowSuccess() {
	var x = document.getElementById("snackbarOk");
	x.className = "show";
	setTimeout(function() { x.className = x.className.replace("show", ""); }, 3000);
}

function snackbarShowError() {
	var x = document.getElementById("snackbarError");
	x.className = "show";
	setTimeout(function() { x.className = x.className.replace("show", ""); }, 3000);
}






