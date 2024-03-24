const nameCadastro = document.getElementById("name-cadastro");
const infoDiv = document.getElementById("name-cadastro");
const planoDiv = document.getElementById("name-cadastro");
const nameH4 = document.getElementById("name-h4");
const finalData = document.getElementById("div-data-tick");
var def = document.getElementById("cadastro-all-info");

var dataDiv = document.getElementById("div-all");
var infoArr = [];
var planoArr = [];
var namePlano = "";

function btnExit() {
	document.getElementById("main-div").style.display = 'flex';
	document.getElementById("cadastro-div").style.display = 'none';
	location.reload();
}

function btnCloseDiv() {
	dataDiv.innerHTML = "";
}

function btnAddInfo() {
	var data =
		'<div class="add-info-div">' +
		'<div class="add-info">' +
		'<div class="deeper-div">' +
		"<h4>Informação</h4>" +
		'<button onclick="btnCloseDiv()" class="exit-btn" class="btn-save">Sair</button>' +
		"</div>" +
		'<label for="information">Título</label>' +
		'<input type="text" id="information">' +
		'<textarea class="textarea" name="textarea-1" id="textarea-1" cols="30" rows="25"></textarea>' +
		'<button onclick="saveInfo()" class="btn-save">Salvar</button>' +
		"</div>" +
		"</div>";

	dataDiv.innerHTML = data;
}

function btnAddPlano() {
	var data =
		'<div class="add-plano-div">' +
		'<div class="add-plano">' +
		'<div class="deeper-div">' +
		"<h4>Plano - Rede</h4>" +
		'<button onclick="btnCloseDiv()" class="exit-btn" class="btn-save">Sair</button>' +
		"</div>" +
		'<div class="checkbox-div">' +
		'<input type="checkbox" id="rede" name="rede" value="Rede">' +
		'<label for="rede"> Rede</label><br>' +
		'<input type="checkbox" id="plano" name="plano" value="Plano">' +
		'<label for=plano"> Plano</label><br>' +
		'<input type="checkbox" id="outro" name="outro" value="Outro">' +
		'<label for="outro"> Outro</label>' +
		'<input type="text" id="input-outro">' +
		"</div>" +
		'<textarea class="textarea" name="textarea-2" id="textarea-2" cols="30" rows="25"></textarea>' +
		'<button onclick="savePlano()" class="btn-save">Salvar</button>' +
		"</div>" +
		"</div>";

	dataDiv.innerHTML = data;
}

nameCadastro.addEventListener("input", function() {
	nameH4.innerHTML = this.value;
});

function saveInfo() {
	var title = document.getElementById("information").value;

	if (title.length > 0) {
		var textObjArr = [];
		var texts = document.getElementById("textarea-1").value.split("\n");

		for (let i = 0; i < texts.length; i++) {
			let text = texts[i];
			textObjArr.push({ text });
		}


		infoArr.push({ title, content: textObjArr });

		//console.log(infoArr);

		var data =
			'<div class="added-convenio">' +
			'<p class="p-info">' +
			title +
			"</p>" +
			'<div class="tick"></div>' +
			"</div>";

		finalData.innerHTML = data + finalData.innerHTML;
		def.style.display = "block";
		btnCloseDiv();
	} else {
		document.getElementById("information").style.outlineColor = "red";
		document.getElementById("information").style.outlineStyle = "auto";
	}
}

function savePlano() {
	let rede = document.getElementById("rede");
	let plano = document.getElementById("plano");
	let outro = document.getElementById("outro");
	let outroValue = document.getElementById("input-outro");
	var title = "";

	var condition = isTrue(
		rede.checked,
		plano.checked,
		outro.checked,
		outroValue.value
	);

	if (condition) {
		title = getPlanoName(rede, plano, outro, outroValue.value);
		var planArr = [];

		var data =
			'<div class="added-convenio">' +
			'<p class="p-info">' +
			title +
			"</p>" +
			'<div class="tick"></div>' +
			"</div>";

		finalData.innerHTML = data + finalData.innerHTML;
		def.style.display = "block";

		var planArr = document.getElementById("textarea-2").value.split("\n");

		for (let i = 0; i < planArr.length; i++) {
			let text = planArr[i];
			planoArr.push({ text });
		}

		btnCloseDiv();
	}
}

function isInfoTrue() { }

function isTrue(rede, plano, outro, outroValue) {
	var arr = [rede, plano, outro];
	var resp = false;
	var count = 0;
	var x = parseInt(outroValue.length);

	for (let i = 0; i < arr.length; i++) {
		if (arr[i] === true) {
			count++;
		}
	}

	if (count === 1) {
		resp = true;
	} else {
		document.getElementById("rede").style.outlineColor = "red";
		document.getElementById("rede").style.outlineStyle = "auto";

		document.getElementById("plano").style.outlineColor = "red";
		document.getElementById("plano").style.outlineStyle = "auto";

		document.getElementById("outro").style.outlineColor = "red";
		document.getElementById("outro").style.outlineStyle = "auto";
		alert("selecione somente ou apenas um elemento");
	}

	if (arr[2] === true && x === 0) {
		resp = false;
		document.getElementById("input-outro").style.outlineColor = "red";
		document.getElementById("input-outro").style.outlineStyle = "auto";
		alert('Escreva o nome - "Outro"');
	}

	return resp;
}

function getPlanoName(rede, plano, outro, outroValue) {
	var arr = [rede, plano, outro];
	namePlano = "";

	for (let i = 0; i < arr.length; i++) {
		if (arr[i].checked === true) {
			namePlano = arr[i].value;
			if (arr[2].checked === true) {
				namePlano = outroValue;
			}
		}
	}

	return namePlano;
}

function saveData() {
	if (nameCadastro.value.length > 0) {
		axios
			.post("/convenios/save", {
				name: nameCadastro.value,
				redePlano: namePlano,
				infos: infoArr,
				planos: planoArr,
			})
			.then(function(response) {
				console.log(response);
				btnCloseDiv();
			})
			.catch(function(error) {
				console.log(error);
			});
		alert("Salvo com sucesso!");
	} else {
		nameCadastro.style.outlineColor = "red";
		nameCadastro.style.outlineStyle = "auto";
	}
}

function delBtn(id) {
	axios
		.delete("/convenios/delete", {
			params: {
				id: id
			},
		})
		.then(function(response) {
			console.log(response);
		}).catch(error => {
			console.log(error);
		});
}	
