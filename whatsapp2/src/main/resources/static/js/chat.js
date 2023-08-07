var stompClient = null;
let phoneNumber = null;
let conversationNumber = null;
let customerObject = null;

let customerArray = [];
let messageArray = [];

var messageBody = document.querySelector("#chat-box");
const dropdown = document.querySelector("#exit");
const search = document.getElementById("search-bar-text");

// Get the modal
var modal = document.getElementById("myModal");
// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

var modalImg = document.getElementById("img01");
var captionText = document.getElementById("caption");


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

function connect() {
	websocketFunction();
	loadCustomers();
	loadWantsToTalk();
}

function websocketFunction() {
	// HTTPS for TLS conncetions VPS
	//var socket = new SockJS("https://www.web.login.imav.com.br:5000/websocket-server");

	// Localhost
	var socket = new SockJS("http://localhost:5000/websocket-server");

	stompClient = Stomp.over(socket);
	stompClient.connect({}, function() {
		setConnected(true);

		stompClient.subscribe("/topic/message", function(message) {

			var data = JSON.parse(message.body);
			if (data !== "" && conversationNumber != null) {
				if (conversationNumber === data.phone_from) {
					if (data.ownerOfMessage === 0) {
						if (data.type === "text") {
							appendImavMessage(data);
						} else {
							showButtonMessage(data);
						}
					} else if (data.ownerOfMessage === 1) {
						showCustomerMessage(data);
					} else {
						alert("Mensagem sem id de destinatário...");
					}
				}
			}
		});

		// Update statuses and reload DOM
		stompClient.subscribe("/topic/messages-customers", function(response) {
			console.log(response);
			loadCustomerMessages(conversationNumber);
		});

		// Update customers
		stompClient.subscribe("/topic/customers", function(response) {
			console.log(response);
			loadCustomers();
		});

		// Update reload DOM
		stompClient.subscribe("/topic/talks", function(response) {
			console.log(response);
			loadWantsToTalk();
		});

	});
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

function putCustomersIntoArray(customers) {
	customerArray = [];

	if (customerObject !== null) {
		for (var i = 0; i < customers.length; i++) {

			// Update set as head
			if (customers[i].phone_number === customerObject.phone_number) {
				customerObject = customers[i];
			}

			if (customers[i].phone_number !== customerObject.phone_number) {
				customerArray.push(customers[i]);
			}
		}
		loopShowCustomers();
	} else {
		if (customers !== "") {
			for (var i = 0; i < customers.length; i++) {
				customerArray.push(customers[i]);
			}
			loopShowCustomers();
		}
	}

}

function sendMessageChat() {
	var text = document.getElementById("insert-text-text").value;
	var textFilter = text.trim();
	var textLength = textFilter.length;

	if (phoneNumber !== null && textLength > 0) {
		axios
			.post("/message/send-chat", {
				messaging_product: "whatsapp",
				to: phoneNumber,
				text: {
					body: $("#insert-text-text").val(),
				},
			})
			.then(function(response) {
				if (response.status === 200) {
					document.getElementById("insert-text-text").value = "";
				} else {
					alert("Mensagem não enviada...");
				}
			});
	}
}

function loopShowCustomers() {
	$("#box-customers").html("");

	if (customerObject !== null) {
		showCustomer(customerObject);
	}

	for (var i = 0; i < customerArray.length; i++) {
		if (customerObject !== customerArray[i]) {
			showCustomer(customerArray[i]);
		}
	}
}

function loopShowMessages() {
	$("#chat-box").html("");

	for (var i = 0; i < messageArray.length; i++) {
		if (messageArray[i].ownerOfMessage === 0) {
			appendImavMessage(messageArray[i]);
		} else {
			showCustomerMessage(messageArray[i]);
		}
	}
	phoneNumber = null;
	phoneNumber = messageArray[0].phone_from;
}

//Show in the chat box only
function sendMessage() {
	stompClient.send(
		"/app/process-message",
		{},
		JSON.stringify({
			name: $("#insert-text-text").val(),
			timestamp: $(Math.floor(Date.now() / 1000)),
		})
	);
}

function appendImavMessage(message) {
	var dateFormat = unixTimestampToDate(message.timestamp);
	var status = setTicksIntoMessage(message.status);

	if (message.type === "text") {
		$("#chat-box").append(
			'<div class="div-box-dialogue">' +
			'<div class="message-dialogue">' +
			'<div class="my-message">' +
			"<p>" +
			message.text +
			"</p>" +
			'<div class="time" id="time">' +
			'<div id="time-conversation">' +
			dateFormat +
			"</div>" +
			'<div id="ticks">' +
			status +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>"
		);
	} else {
		showButtonMessage(message);
	}
	messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;
}

function showButtonMessage(message) {
	var dateFormat = unixTimestampToDate(message.timestamp);
	var buttons = setButtons(message.buttons);
	var status = setTicksIntoMessage(message.status);

	$("#chat-box").append(
		'<div class="div-box-dialogue">' +
		'<div class="message-dialogue">' +
		'<div class="my-message">' +
		"<p>" +
		message.text +
		"</p>" +
		'<div class="time" id="time">' +
		'<div id="time-conversation">' +
		dateFormat +
		"</div>" +
		'<div id="ticks">' +
		status +
		"</div>" +
		"</div>" +
		"</div>" +
		'<div class="button-dialogue">' +
		buttons +
		"</div>" +
		"</div>" +
		"</div>"
	);
	messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;
}

function setButtons(data) {
	var str = "";

	for (let i = 0; i < data.length; i++) {
		str = str + '<div class="my-message-button">' + data[i].title + "</div>";
	}
	return str;
}

function showCustomerMessage(message) {
	var dateFormat = unixTimestampToDate(message.timestamp);

	if (message.type === "text" || message.type === "interactive" || message.type === "button" ) {
		$("#chat-box").append(
			'<div class="div-box-dialogue">' +
			'<div class="message-dialogue-other">' +
			'<div class="other-message">' +
			"<p>" +
			message.text +
			"</p>" +
			'<div class="time" id="time">' +
			'<div id="time-conversation">' +
			dateFormat +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>"
		);
	} else if (message.type === "image") {
		
		var caption = message.text;
	
		if(message.text === null){
			caption = " ";
		}
		
		$("#chat-box").append(
			'<div class="div-box-dialogue">' +
			'<div class="message-dialogue-img">' +
			'<div class="img-message">' +
			'<img class="myImg" id="' + message.id +
			'" onclick="getImage(this)" src="data:' + message.mimeType + ';base64, ' +
				message.content +
			'" width="340" height="240">' +
			'<p>' +
			caption +
			"</p>" +
			'<div class="time" id="time">' +
			'<div id="time-conversation">' +
			dateFormat +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>"
		);
	} else if (message.type === "sticker") {
		
		var caption = "";
		caption += message.text;
		
		$("#chat-box").append(
			'<div class="div-box-dialogue">' +
			'<div class="message-dialogue-img">' +
			'<div class="img-message">' +
			'<img class="myImg" id="' + message.id +
			'" onclick="getImage(this)" src="data:' + message.mimeType + ';base64, ' +
				message.content +
			'" width="90" height="90">' +
			'<p>' +
			caption +
			"</p>" +
			'<div class="time" id="time">' +
			'<div id="time-conversation">' +
			dateFormat +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>"
		);
	}

	messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;
}

function showCustomer(customer) {
	var dateFormat = unixTimestampToDate(customer.timestamp);

	// /* background-color: #f5b4b4; */
	if (customer.talk) {
		$("#box-customers").append(
			'<div class="contact" style="background-color: #f5b4b4;" onClick="replyClick(this.id)" id="' +
			customer.phone_number +
			'">' +
			'<div class="picture"></div>' +
			'<div class="div-contact">' +
			'<span dir="auto" class="name-contact">' +
			customer.name +
			'</span><div class="time">' +
			'<span dir="auto" class="phone-contact" id="' +
			customer.phone_number +
			'">' +
			customer.phone_number +
			"</span><span>" +
			dateFormat +
			"</span></div>" +
			"</div>" +
			"</div>"
		);
	} else {
		$("#box-customers").append(
			'<div class="contact" onClick="replyClick(this.id)" id="' +
			customer.phone_number +
			'">' +
			'<div class="picture"></div>' +
			'<div class="div-contact">' +
			'<span dir="auto" class="name-contact">' +
			customer.name +
			'</span><div class="time">' +
			'<span dir="auto" class="phone-contact" id="' +
			customer.phone_number +
			'">' +
			customer.phone_number +
			"</span><span>" +
			dateFormat +
			"</span></div>" +
			"</div>" +
			"</div>"
		);
	}
	//customerBody.scrollTop = customerBody.scrollHeight - customerBody.clientHeight;
}

function showCustomerHeader(customer) {
	$(".div-header").html("");

	$(".div-header").append(
		'<div class="contact-conversation">' +
		'<div class="picture-conversation"></div>' +
		'<div class="div-contact">' +
		'<span dir="auto" class="name-contact">' +
		customer.name +
		"</span>" +
		'<span dir="auto" class="phone-contact" id="' +
		customer.phone_number +
		'">' +
		customer.phone_number +
		"</span>" +
		"</div>" +
		"</div>"
	);
}

function setTicksIntoMessage(data) {
	var str = "";

	switch (data) {
		case 1:
			str = '<div class="check"></div>';
			break;
		case 2:
			str = '<div class="check"></div><div class="check2"></div>';
			break;
		case 3:
			str = '<div class="check3"></div><div class="check4"></div>';
			break;
		default:
			console.log("Status de entrega desconhecido: " + data);
	}
	return str;
}

$(function() {
	$("form").on("submit", function(e) {
		e.preventDefault();
	});
	// $("#send-message").click(function() {
	// 	sendMessage();
	// });
});

function sendPhoneCustomer(id) {
	axios
		.get("/message/messages", {
			params: {
				id: id,
			},
		})
		.then(function(response) {
			if (response.status === 200) {
				conversationNumber = id;
			} else if (response.status === 404) {
				console.log("404 - Pacientes não encontrados...");
			} else {
				console.log(response);
			}
		});
}

function loadCustomers() {
	axios({
		method: "get",
		url: "/customer/customers",
	}).then(function(response) {
		if (response.status === 200) {
			putCustomersIntoArray(response.data);
			console.log("Ok...");
		} else if (response.status === 404) {
			console.log("404 - Pacientes não encontrados...");
		} else {
			console.log(response);
		}
	});
}

function loadCustomerMessages(phoneNumber) {
	var number = phoneNumber;

	axios
		.get("/message/messages", {
			params: {
				id: number,
			},
		})
		.then(function(response) {
			if (response.status === 200) {
				showCustomerHeader(customerObject);
				loadMessages(response.data);
				loadCustomers();
				console.log("Ok...");
			} else if (response.status === 404) {
				console.log("404 - Mensagens não encontradas...");
			} else {
				console.log(response);
			}
		});

}




function loadMessages(message) {
	messageArray = [];

	if (message !== null) {
		for (var i = 0; i < message.length; i++) {
			messageArray.push(message[i]);
		}

		if (customerObject.phone_number === message[0].phone_from) {
			loopShowMessages();
		}
	}
}

function replyClick(clickedId) {
	axios
		.get("/customer/customer", {
			params: {
				id: clickedId,
			},
		})
		.then(function(response) {
			if (response.status === 200) {
				customerObject = response.data;
				conversationNumber = clickedId;
				loadCustomerMessages(clickedId);
			} else if (response.status === 404) {
				console.log("404 - Paciente não encontrado...");
			} else {
				console.log(response);
			}
		});
}

function findCustomer(search) {
	if (search !== " ") {
		axios
			.get("/customer/livesearch", {
				params: {
					input: search,
				},
			})
			.then(function(response) {
				if (response.status === 200) {
					customerArray = response.data;
					loopShowCustomers();
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
		findCustomer(this.value);
	} else {
		loadCustomers();
	}
});

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

function getImage(data) {
	modal.style.display = "block";
	modalImg.src = data.src;
	// captionText.innerHTML = this.alt;
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
	modal.style.display = "none";
};


