let passwordText = document.getElementById("psw");
let passwordText2 = document.getElementById("psw-repeat");
let usernameText = document.getElementById("nickname");
let emailText = document.getElementById("email");

var stompClient = null;
const dropdown = document.querySelector("#exit");

function connect() {
	loadUsers();
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
	var socket = new SockJS("https://www.web.login.imav.com.br:5000/websocket-server");

	// Localhost
	//var socket = new SockJS("http://localhost:5000/websocket-server");

	stompClient = Stomp.over(socket);
	stompClient.connect({}, function() {
		setConnected(true);

		// Update reload DOM
		stompClient.subscribe("/topic/talks", function(response) {
			console.log(response);
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


passwordText2.addEventListener("input", function() {
	if (this.value === passwordText.value) {
		console.log("Ok");
	}
	//border-color: blue;
	//border-style: solid;
});

function saveUser() {
	let pswLength = passwordText.value.length;
	let psw2Length = passwordText2.value.length;
	let emailLength = emailText.value.length;
	let usernameLength = usernameText.value.length;
	const role = "USER";

	if (
		passwordText.value === passwordText2.value &&
		pswLength > 0 &&
		psw2Length > 0 &&
		emailLength > 0 &&
		usernameLength > 0
	) {
		axios
			.post("/user/registration", {
				username: usernameText.value,
				email: emailText.value,
				password: passwordText.value,
				matchingPassword: passwordText2.value,
				role: role,
			})
			.then(function(response) {
				if (response.status === 201) {
					alert("Usuário criado");
					loadUsers();
				} else {
					alert("Usuário não criado");
				}
			});
	}
}

function loadUsers() {
	axios({
		method: "get",
		url: "/user/users",
	}).then(function(response) {
		if (response.status === 200) {
			showUsers(response.data);
		} else if (response.status === 404) {
			console.log("404 - Pacientes não encontrados...");
		} else {
			console.log(response);
		}
	});
}

function showPassword(data) {
	let idResp = data.substring(4);
	var data = $("#" + idResp + " :input");
	console.log(data);

	var senha = document.getElementById("senha-" + idResp);

	if (senha.type === "password") {
		senha.type = "text";
	} else {
		senha.type = "password";
	}
}

function buttonDelete(clickedId) {
	var username = clickedId.substring(7);

	axios
		.get("/user/delete", {
			params: {
				username: username,
			},
		})
		.then(function(response) {
			if (response.status === 200) {
				alert("Usuário excluído...");
				loadUsers();
			} else if (response.status === 404) {
				alert("404 - Usuário não encontrado...");
			} else {
				console.log(response);
			}
		});
}

function buttonUpdate(clickedId) {
	let idResp = clickedId.substring(6);
	var data = $("#" + idResp + " :input");
	var newPassword = data[0].value;

	axios
		.post("/user/newpassword-from-admin", {
			username: idResp,
			password: newPassword,
		})
		.then(function(response) {
			if (response.status === 200) {
				alert("Senha salva com sucesso...");
				loadUsers();
			} else if (response.status === 404) {
				alert("404 - Usuário não encontrado...");
			} else {
				console.log(response);
			}
		});
}

function showUsers(data) {
	$("#users").html("");

	var table =
		'<table class="table" id="table">' +
		"<thead>" +
		"<tr>" +
		"<th>Nome</th>" +
		"<th>Email</th>" +
		"<th>Nova senha</th>" +
		"<th></th>" +
		"<th></th>" +
		"</tr>" +
		"</thead>" +
		"<tbody>";

	for (var i = 0; i < data.length; i++) {
		if (data[i].username !== "admin") {
			table =
				table +
				'<tr class="tr" id="' +
				data[i].username +
				'" >' +
				'<td class="nome" id="name-' +
				data[i].username +
				'">' +
				data[i].username +
				"</td>" +
				'<td id="email">' +
				data[i].email +
				"</td>" +
				'<td id="senha">' +
				'<input class="senha-input" style="margin:0;" id="senha-' +
				data[i].username +
				'" type="password" placeholder="***********">' +
				'<img class="img-password" id="img-' +
				data[i].username +
				'" src="/img/eye_closed.png" onclick="showPassword(this.id)">' +
				"</td>" +
				'<td id="nome">' +
				'<button class="button-update" id="btnUp-' +
				data[i].username +
				'" onclick="buttonUpdate(this.id)">Atualizar</button>' +
				"</td>" +
				'<td class="nome">' +
				'<button type="submit" class="button-delete" id="btnDel-' +
				data[i].username +
				'" onclick="buttonDelete(this.id)">Excluir</button>' +
				"</td>" +
				"</tr>";
		} else {
			table =
				table +
				'<tr class="tr" id="' +
				data[i].username +
				'" >' +
				'<td class="nome" id="name-' +
				data[i].username +
				'">' +
				data[i].username +
				"</td>" +
				'<td id="email">' +
				data[i].email +
				"</td>" +
				'<td id="senha">' +
				'<input class="senha-input" style="margin:0;" id="senha-' +
				data[i].username +
				'" type="password" placeholder="***********">' +
				'<img class="img-password" id="img-' +
				data[i].username +
				'" src="/img/eye_closed.png" onclick="showPassword(this.id)">' +
				"</td>" +
				'<td id="nome">' +
				'<button class="button-update" id="btnUp-' +
				data[i].username +
				'" onclick="buttonUpdate(this.id)">Atualizar</button>' +
				"</td>" +
				'<td class="nome">' +
				"</td>" +
				"</tr>";
		}
	}

	table = table + "</tbody></table>";
	$("#users").append(table);
}

function validateEmail(emailText) {
	if (
		/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(myForm.emailAddr.value)
	) {
		return true;
	}
	alert("You have entered an invalid email address!");
	return false;
}
