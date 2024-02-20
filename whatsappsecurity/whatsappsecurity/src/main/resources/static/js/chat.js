var stompClient = null;
let phoneNumber = null;
let conversationNumber = null;
let customerObject = null;
let searchCustomersObject = [];

let customerArray = [];
let messageArray = [];

var messageBody = document.querySelector("#chat-box");
var customerBody = document.querySelector("#box-customer");
const search = document.getElementById("search-bar-text");


function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
}

function connect() {
	var socket = new SockJS("/websocket-server");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function() {
		setConnected(true);
		loadCustomers();
		console.log("Connected...");
		stompClient.subscribe("/topic/message", function(message) {
			var data = JSON.parse(message.body);
			//console.log(data);
			if (data !== "" && conversationNumber != null) {

				//console.log(data);

				if (conversationNumber === data.phone_from) {

					if (data.ownerOfMessage === 0) {
						if (data.type === "text") {
							showImavMessage(data);
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

		stompClient.subscribe("/topic/messages", function(message) {
			messageArray = [];
			var data = JSON.parse(message.body);
			if (data !== "") {
				showCustomerConversation(customerObject);
				for (var i = 0; i < data.length; i++) {
					messageArray.push(data[i]);
				}
				loopShowMessages();

				if (customerObject.phone_number === data[0].phone_from) {
					loopShowMessages();
				}
			}
		});

		stompClient.subscribe("/topic/customers", function(message) {
			customerArray = [];
			var data = JSON.parse(message.body);
			//console.log(data);
			if (data !== "") {
				for (var i = 0; i < data.length; i++) {
					customerArray.push(data[i]);
				}
				loopShowCustomers();
			}
		});

	});
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
	for (var i = 0; i < customerArray.length; i++) {
		showCustomers(customerArray[i]);
	}
}

function loopShowMessages() {
	$("#chat-box").html("");

	//console.log(messageArray);
	for (var i = 0; i < messageArray.length; i++) {
		if (messageArray[i].ownerOfMessage === 0) {
			showImavMessage(messageArray[i]);
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

function showImavMessage(message) {
	var dateFormat = unixTimestampToDate(message.timestamp);
	var status = setTicksIntoMessage(message.status);

	if (message.type === "text") {
		$("#chat-box").append(
			'<div class="div-box-dialogue">' +
			'<div class="message-dialogue">' +
			'<div class="my-message">' +
			"<p>" + message.text + "</p>" +
			'<div class="time" id="time">' +
			'<div id="time-conversation">' +
			dateFormat +
			'</div>' +
			'<div id="ticks">' +
			status +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>'
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
		'<p>' + message.text + '</p>' +
		'<div class="time" id="time">' +
		'<div id="time-conversation">' +
		dateFormat +
		'</div>' +
		'<div id="ticks">' +
		status +
		'</div>' +
		'</div>' +
		'</div>' +
		'<div class="button-dialogue">' +
		buttons +
		'</div>' +
		'</div>' +
		'</div>'
	);
	messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;

}

function setButtons(data) {
	var str = "";

	for (let i = 0; i < data.length; i++) {
		str = str + '<div class="my-message-button">' + data[i].title + '</div>';
	}
	return str;
}

function showCustomerMessage(message) {
	//console.log(message);
	var dateFormat = unixTimestampToDate(message.timestamp);

	$("#chat-box").append(
		'<div class="div-box-dialogue">' +
		'<div class="message-dialogue-other">' +
		'<div class="other-message">' +
		"<p>" + message.text + "</p>" +
		'<div class="time" id="time">' +
		'<div id="time-conversation">' +
		dateFormat +
		'</div>' +
		"</div>" +
		"</div>" +
		"</div>"
	);
	messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;
}

function showCustomers(customer) {
	var dateFormat = unixTimestampToDate(customer.timestamp);

	$("#box-customers").append(
		'<div class="contact" onClick="reply_click(this.id)" id="' + customer.phone_number + '">' +
		'<div class="picture"></div>' +
		'<div class="div-contact">' +
		'<span dir="auto" class="name-contact">' +
		customer.name +
		'</span><div class="time">' +
		'<span dir="auto" class="phone-contact" id="' +
		customer.phone_number +
		'">' +
		customer.phone_number +
		"</span><span>" + dateFormat + "</span></div>" +
		"</div>" +
		"</div>"
	);
	//customerBody.scrollTop = customerBody.scrollHeight - customerBody.clientHeight;
}

function showCustomerConversation(customer) {
	$(".conversation-box").html("");

	$(".conversation-box").append(
		'<div class="contact-conversation">' +
		'<div class="picture"></div>' +
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
		.get("/message/messages/", {
			params: {
				id: id,
			},
		})
		.then(function(response) {
			if (response.status === 200) {
					conversationNumber = id;
					console.log("Ok...");
			} else if (response.status === 404){
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
					console.log("Ok...");
			} else if (response.status === 404){
					console.log("404 - Pacientes não encontrados...");
			} else {
				console.log(response);
			}
	});
}

function loadCustomerMessages(phoneNumber) {
	axios
		.get("/message/messages/", {
			params: {
				id: phoneNumber,
			},
		})
		.then(function(response) {
			if (response.status === 200) {
					console.log("Ok...");
			} else if (response.status === 404){
					console.log("404 - Mensagens não encontradas...");
			} else {
				console.log(response);
			}
		});
}

function reply_click(clickedId) {
	axios
		.get("/customer/customer/", {
			params: {
				id: clickedId
			},
		})
		.then(function(response) {
			if (response.status === 200) {
				customerObject = response.data;
				conversationNumber = clickedId;
				loadCustomerMessages(clickedId);
			} else if (response.status === 404){
					console.log("404 - Paciente não encontrado...");
			} else {
					console.log(response);
			}
		});
}

function findCustomer(search) {
	if (search !== " "){
		axios
			.get("/customer/livesearch/", {
				params: {
					input: search,
				},
			})
			.then(function(response) {
				if (response.status === 200) {
					customerArray = response.data;
					loopShowCustomers();
				} else if (response.status === 404){
					console.log("404 - Paciente não encontrado...");
				} else {
					console.log(response);
				}
		});
	}
}

search.addEventListener('input', function() {
	if (this.value !== ""){
		findCustomer(this.value);
	} else {
		loadCustomers();
	}
	
});

function unixTimestampToDate(unix_timestamp) {
	var timeStamp = unix_timestamp * 1000;
	var dateFormat = new Date(timeStamp);

	var day = addZeroToDate(dateFormat.getDate());
	var month = addZeroToDate((dateFormat.getMonth() + 1));
	var year = addZeroToDate(dateFormat.getFullYear());
	var hours = addZeroToDate(dateFormat.getHours());
	var minutes = addZeroToDate(dateFormat.getMinutes());

	var date = day + "/" + month + "/" + year +
		" " +
		hours + ":" + minutes;
	return date;
}

function addZeroToDate(data) {
	if (data.toString().length <= 1) {
		data = "0" + data;
	}
	return data;
}

//function setNewCustomersIntoArray(searchCustomersObject){
//	console.log(customerArray);
//	customerArray = customerArray.filter(person => person.phone_number != searchCustomersObject.phone_number);
//	customerArray.push(searchCustomersObject);
//	console.log(customerArray);
//}
