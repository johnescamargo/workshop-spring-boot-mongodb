import Websocket from "./websocket.js";

const websocket = new Websocket();

const searchConvenio = document.getElementById("pesquisa");
const searchPlano = document.getElementById("pesquisa-plano");

function onloadInit() {
	websocket.connect;
	getAllConvenios();
}

function deletar102(){
	axios
			.delete("/convenios/delete", {
				params: {
					id: 102
				},
			})
			.then(function(response) {
					console.log(response);
			}).catch(error => {
				console.log(error);
			});
}

function configBtn(){
	document.getElementById("main-div").style.display = 'none';
	document.getElementById("cadastro-div").style.display = 'flex';
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
			 +  '<p>' + response[i].name + '</p>'
			 +  '</div>';
	}

	document.getElementById("convenios").innerHTML = data;
}

function getId(id) {

	document.getElementById("convenios").innerHTML = '';
	document.getElementById("info-div-display").innerHTML = '';
	document.getElementById("right-space").style.display = 'none';

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
					//console.log(response);
					setConvenio(response.data.infos);
					setPlanos(response.data.planos);
					setConvenioName(response.data.name);
					setPlanoName(response.data.redePlano);
					sessionStorage.setItem("id", response.data.id);
				} else {
					console.log(response);
				}
			});
	}
}

function getPlanoById(input) {

	document.getElementById("convenios").innerHTML = '';
	//console.log(input);
	var id = sessionStorage.getItem("id");

	if (input !== " ") {
		axios
			.get("/planos/livesearch", {
				params: {
					id: id,
					input: input
				},
			})
			.then(function(response) {
				if (response.status === 200) {
					//console.log(response);
					setPlanos(response.data);
				} else if (response.status === 404) {
					console.log("404 - convenio não encontrado...");
				} else {
					console.log(response);
				}
			});
	} else {
		getId(id);
	}

}

function setPlanoName(typePlano) {
	document.getElementById("type-plano").innerHTML = "Pesquisa " + typePlano;
}

function setConvenio(response) {
	//console.log(response);
	var data = '';

	for (let i = 0; i < response.length; i++) {
		data += '<div class="info-div">'
			  + '<div class="info">'
			  + '<h4>' + response[i].title + '</h4>'
			  + '<div class="info-field">'
			  + setContent(response[i].content)
			  + '</div>'
			  + '</div>'
			  + '</div>';
	}

	document.getElementById("info-div-display").innerHTML = data;
}

function setConvenioName(name) {
	var dataName = '<h3>' + name + '</h3>';
	document.getElementById("convenio-name").innerHTML = dataName;
}

function setContent(response) {

	var data = '';

	for (let i = 0; i < response.length; i++) {
		data += '<p>' + response[i].text + '</p>';
	}
	return data;
}

function setPlanos(response) {

	document.getElementById("right-space").style.display = 'initial';
	//console.log(response);
	var data = '';

	for (let i = 0; i < response.length; i++) {
		data += '<div class="plano" id="' + response[i].id + '">'
			+ '<p>' + response[i].text + '</p>'
			+ '</div>';
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

function findConvenio(search) {
	if (search !== " ") {
		axios
			.get("/convenios/livesearch", {
				params: {
					input: search,
				},
			})
			.then(function(response) {
				if (response.status === 200) {
					setConveniosHtml(response.data);
				} else if (response.status === 404) {
					console.log("404 - convenio não encontrado...");
				} else {
					console.log(response);
				}
			});
	}
}

searchConvenio.addEventListener("input", function() {

	document.getElementById("convenios").innerHTML = '';
	document.getElementById("info-div-display").innerHTML = '';
	document.getElementById("right-space").style.display = 'none';

	if (this.value !== "") {
		findConvenio(this.value);
	} else {
		getAllConvenios();
	}
});

searchPlano.addEventListener("input", function() {

	document.getElementById("data-div").innerHTML = '';

	if (this.value !== "") {
		getPlanoById(this.value);
	} else {
		getId(sessionStorage.getItem("id"));
	}
});

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
window.configBtn = configBtn;
window.deletar102 = deletar102;



