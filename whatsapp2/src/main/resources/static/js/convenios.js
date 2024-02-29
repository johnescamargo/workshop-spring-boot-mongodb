import Websocket from "./websocket.js";
const websocket = new Websocket();



function onloadInit() {
	websocket.connect;
	getAllConvenios();
}

function getAllConvenios() {
	axios({
		method: "get",
		url: "/convenios/getAll",
	}).then(function(response) {
		if (response.status === 200) {
			//console.log(response);
			setSelectConvenios(response.data);
			setConveniosHtml(response.data);
		} else {
			console.log(response);
		}
	});
}

function setSelectConvenios(response) {
	var data = '<select name="convenio" id="convenio" onchange="selectGetById()" required>'
		+ '<option value="null" selected>Todos *</option>';

	for (let i = 0; i < response.length; i++) {
		data += '<option value="' + response[i].id + '">' + response[i].name + '</option>';
	}

	data += '</select></div>';

	document.getElementById("select-convenio").innerHTML = data;
}


function setConveniosHtml(response) {
	var data = '';

	for (let i = 0; i < response.length; i++) {
		data += '<div onclick="getId(this.id)" id="' + response[i].id + '" class="convenio">'
			+ '<p>' + response[i].name + '</p>'
			+ '</div>';
	}

	document.getElementById("convenios").innerHTML = data;
}

function getId(id) {

	if (id === 'null') {
		getAllConvenios();
	} else {
		axios
			.get("/convenios/findbyid", {
				params: {
					id: id
				},
			})
			.then(function(response) {
				if (response.status === 200) {
					setConvenio(response.data.infos);
					setPlanos(response.data.planos);
				} else {
					console.log(response);
				}
			});
	}
}

function setConvenio(response){
	//console.log(response);
	var data = '';
	
	for(let i = 0; i < response.length; i++){
		data += '<div class="info-div">'
			 +	'<div class="info">'
			 +	 	'<h4>' + response[i].title + '</h4>'
			 +	 	'<div class="info-field">'
			 +			setContent(response[i].content)
			 +	 	'</div>'
			 +	 '</div>'
			 + '</div>';
	}
	
	document.getElementById("info-div-display").innerHTML = data;
}

function setContent(response){
	
	var data = '';
	
		for(let i = 0; i < response.length; i++){
			data += '<p>' + response[i].text + '</p>';	
		}
	return data;
}

function setPlanos(response){
	
	document.getElementById("right-space").style.display = 'initial';
	console.log(response);
	var data = '';
	
	for(let i = 0; i < response.length; i++){
		data += '<div class="plano">'
			 +	  '<p>' + response[i].textOne + ' ' + response[i].textTwo + '</p>'
			 +  '</div>';
	}
	
	document.getElementById("data-div").innerHTML = data;
}

function selectGetById() {
	var id = document.getElementById("convenio").value;
	document.getElementById("convenios").innerHTML = '';
	document.getElementById("info-div-display").innerHTML = '';
	document.getElementById("right-space").style.display = 'none';

	getId(id);
}

function toggleHamburger(x) {
	x.classList.toggle("change");
	let lengthX = x.classList.length;

	if (lengthX > 1) {
		document.getElementById("div-ul").style.right = "0%";
		document.getElementById("div-ul").style.transition = "0.8s";
	} else {
		document.getElementById("div-ul").style.right = "-100%";
		document.getElementById("div-ul").style.transition = "all 0.8s ease 0.4s";
	}
}

window.addEventListener("onload", onloadInit());
window.addEventListener("onload", onloadInit);
window.toggleHamburger = toggleHamburger;
window.getId = getId;
window.getAllConvenios = getAllConvenios;
window.selectGetById = selectGetById;

