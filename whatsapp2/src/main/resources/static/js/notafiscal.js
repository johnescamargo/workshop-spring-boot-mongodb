import Websocket from "./websocket.js";
const websocket = new Websocket();

var dadosPagamento = [];
var examesSelecionados = [];
var customer;
var datepicker = document.getElementById("datepicker");
var pesquisa = document.getElementById("pesquisa");
var outros = "";

function onloadInit() {
	loadWantsToTalk();
	setTable();
	websocket.connect;
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
		dataHtml =
			'<button class="dropbtn">' +
			'<div id="alert-div">' +
			'<div id="alert-number">' +
			dataLength +
			"</div>" +
			"</div>" +
			"</button>" +
			'<div class="dropdown-content" id="myDropdown">';

		for (var i = 0; i < data.length; i++) {
			dataHtml += "<p>" + data[i].name + " - " + data[i].phone + "</p>";
		}

		dataHtml += "</div>";
	}
	document.getElementById("exit").innerHTML = dataHtml;
}

function unixTimestampToDate(unixTimestamp) {
	var timestamp = unixTimestamp * 1000;
	var dateFormat = new Date(timestamp);

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

datepicker.addEventListener("input", function() {
	getCustomersByDate(this.value);
});

pesquisa.addEventListener("input", function() {
	if (this.value !== "") {
		liveSearch(this.value);
	} else {
		setTable();
	}
});

function changeformat(val) {
	const myArray = val.split("-");
	let year = myArray[0];
	let month = myArray[1];
	let day = myArray[2];
	let formatteddate = day + "/" + month + "/" + year;

	return formatteddate;
}

function setTable() {
	// Clean div
	document.getElementById("data-table").innerHTML = "";

	axios
		.get("/nf/all")
		.then(function(response) {
			//console.log(response);
			displayTableHtml(response);
		})
		.catch(function(error) {
			console.log(error);
		});
}

function displayTableHtml(data) {
	var dataTable = "";
	var dataLength = data.data.length;

	for (let i = dataLength - 1; i >= 0; i--) {
		let id = data.data[i].id;
		let time = unixTimestampToDate(data.data[i].timestampCreated);

		if (data.data[i].nfDone === false) {
			dataTable +=
				'<div class="table-customers-red" id="' +
				id +
				'" onclick="getCustomerId(this.id)" >' +
				'<div class="name">' +
				data.data[i].nome.toUpperCase() +
				"</div>" +
				'<div class="div-date">' +
				time +
				"</div>" +
				"</div>";
		} else {
			dataTable +=
				'<div class="table-customers-green" id="' +
				id +
				'" onclick="getCustomerId(this.id)" >' +
				'<div class="name">' +
				data.data[i].nome.toUpperCase() +
				"</div>" +
				'<div class="div-date">' +
				time +
				"</div>" +
				"</div>";
		}
	}

	document.getElementById("data-table").innerHTML = dataTable;
}

function getCustomerId(id) {
	//console.log(id);
	axios
		.get("/nf/getcustomer", {
			params: {
				id: id,
			},
		})
		.then(function(response) {
			console.log(response);
			customer = response.data;
			setCustomerHtml(response.data);
		})
		.catch(function(error) {
			console.log(error);
		});
}

function liveSearch(data) {
	//console.log(data);
	axios
		.get("/nf/livesearch", {
			params: {
				input: data,
			},
		})
		.then(function(response) {
			//console.log(response);
			displayTableHtml(response);
		})
		.catch(function(error) {
			console.log(error);
		});
}

function getCustomersByDate(data) {
	var resp = changeDateformat(data);

	axios
		.get("/nf/getbydate", {
			params: {
				date: resp,
			},
		})
		.then(function(response) {
			//console.log(response);
			displayTableHtml(response);
		})
		.catch(function(error) {
			console.log(error);
		});
}

function setChecked(data) {
	var resp = "";

	if (data === true) {
		resp = "checked";
	}

	return resp;
}

function setCustomerHtml(customer) {
	//console.log(customer);

	// Clean div
	document.getElementById("customer-div").innerHTML = "";
	outros = "";

	let data =
		"" +
		'<div class="frame-div">' +
		'<div class="main-div">' +
		"<!-- Dados Principais-->" +
		'<div class="col-1-data">' +
		setNfData(customer) +
		'      <div class="main-data">' +
		'          <div class="prontuario-div">' +
		'              <label for="prontuario">Prontuário Nº 1/</label>' +
		'              <input type="text" name="prontuario" id="prontuario" value="' +
		customer.prontuario +
		'" disabled>' +
		"          </div>" +
		"          <br>" +
		"          <!-- Exame / Consulta -->" +
		'          <div class="exam-consul">' +
		'              <input type="checkbox" name="consulta" id="consulta" ' +
		setChecked(customer.consulta) +
		' disabled>' +
		'              <label for="consulta">Consulta</label>' +
		'              <input type="checkbox" name="exame" id="exame" ' +
		setChecked(customer.exame) +
		' disabled>' +
		'              <label for="exame">Exame</label>' +
		"          </div>" +
		"          <br>" +
		"          <!-- Exame / Consulta end-->" +
		'          <label for="nome">Nome</label>' +
		'          <input type="text" name="nome" id="nome" placeholder="Nome" value="' +
		customer.nome.toUpperCase() +
		'" disabled>' +
		"          <br>" +
		'          <label for="cpf">CPF</label>' +
		'          <input type="text" name="cpf" id="cpf" onkeyup="validateCpf(this)" maxlength="14"' +
		'              placeholder="___.___.___-__" value="' +
		validateCpf(customer.cpf) +
		'" disabled>' +
		"          <br>" +
		'          <label for="e-mail">E-mail</label>' +
		'          <input type="text" name="e-mail" id="e-mail" placeholder="exemplo@website.com.br" value="' +
		customer.email.toLowerCase() +
		'" disabled>' +
		"          <br>" +
		'          <label for="telefone">Telefone</label>' +
		'          <input type="text" name="telefone" id="telefone" onkeyup="validateTelefone(this)" maxlength="15"' +
		'              placeholder="Telefone" value="' +
		validateTelefone(customer.telefone) +
		'" disabled>' +
		"      </div>" +
		'      <!-- <div class="forma-pagamento-select">' +
		'          <label for="obs">Obs.:</label>' +
		'          <textarea name="obs" id="obs" cols="30" rows="3" disabled></textarea>' +
		"      </div> -->" +
		"  </div>" +
		"  <!-- Dados Principais End -->" +
		'  <div class="col-2-data">' +
		'      <div class="cep-data">' +
		'          <div class="div-container">' +
		'              <div class="cep-container">' +
		'                  <label for="cep">CEP</label>' +
		'                  <input type="text" name="cep" id="cep" onkeyup="validateCep(this)" maxlength="9"' +
		'                      placeholder="_____-___" value="' +
		validateCep(customer.cep) +
		'" disabled>' +
		"              </div>" +
		"          </div>" +
		'          <div class="div-container">' +
		'              <div class="deeper-container">' +
		'                  <label for="rua">Rua</label>' +
		'                  <input type="text" name="rua" id="rua" placeholder="Nome da rua" value="' +
		customer.rua +
		'" disabled>' +
		"              </div>" +
		'              <div class="numero-container">' +
		'                  <label for="numero">Nº</label>' +
		'                  <input type="text" name="numero" id="numero" placeholder="Número" value="' +
		customer.numero +
		'" disabled>' +
		"              </div>" +
		"          </div>" +
		'          <label for="complemento">Complemento</label>' +
		'          <input type="text" name="complemento" id="complemento" placeholder="Complemento" value="' +
		customer.complemento +
		'" disabled>' +
		"          <br>" +
		'          <label for="bairro">Bairro</label>' +
		'          <input type="text" name="bairro" id="bairro" placeholder="Bairro" value="' +
		customer.bairro +
		'" disabled>' +
		"          <br>" +
		'          <label for="cidade">Cidade</label>' +
		'          <input type="text" name="cidade" id="cidade" placeholder="Cidade" value="' +
		customer.cidade +
		'" disabled>' +
		"          <br>" +
		'          <label for="cidade">Estado</label>' +
		'          <input type="text" name="estado" id="estado" placeholder="Estado" value="' +
		customer.estado +
		'" disabled>' +
		"          <br>" +
		'          <label for="valor">Valor</label>' +
		'          <div class="div-container">' +
		'              <label for="valor">R$</label>' +
		'              <input type="text" name="valor" id="valor" onkeyup="validateValor(this)" maxlength="11"' +
		'                  placeholder="00,00" value="' +
		validateValor(customer.valor) +
		'" disabled>' +
		"          </div>" +
		"          <br>" +
		'          <div class="prontuario-div">' +
		'              <label for="forma-pagamento">Forma de Pagamento</label>' +
		'              <input type="text" name="forma-pagamento" id="forma-pagamento" value="' +
		customer.formaPagamento +
		'" disabled>' +
		"          </div>" +
		'          <div id="pagamento-extra">' +
		isNull(customer.pagamento2) +
		'          <textarea name="textarea-tag" id="textarea-tag" cols="37" rows="3" disabled>' +
		customer.pagamento1 +
		"</textarea>" +
		"          </div>" +
		"      </div>" +
		"  </div>" +
		'  <div class="col-3-data">' +
		"      <!-- Exames -->" +
		'      <div class="exames">' +
		"          <h4>Exames</h4>" +
		"          <br>" +
		'          <label for="medico">Médico Solicitante</label>' +
		'          <input name="medico" id="medico" value="' +
		customer.medico +
		'" disabled>' +
		"          <br><br>" +
		"          <!-- Checkbox -->" +
		'          <input type="checkbox" name="Biometria" id="biometria" ' +
		setCheckedExames(customer.exames, "biometria") +
		' disabled>' +
		'          <label for="biometria">Biometria</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Campimetria" id="campimetria"' +
		setCheckedExames(customer.exames, "campimetria") +
		' disabled>' +
		'          <label for="campimetria">Campimetria</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Capsulotomia" id="capsulotomia"' +
		setCheckedExames(customer.exames, "capsulotomia") +
		' disabled>' +
		'          <label for="capsulotomia">Capsulotomia</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Curva Tensional" id="curva-tensional"' +
		setCheckedExames(customer.exames, "curva-tensional") +
		' disabled>' +
		'          <label for="curva-tensional">Curva Tensional</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Teste Ortóptico" id="teste-ortoptico"' +
		setCheckedExames(customer.exames, "teste-ortoptico") +
		' disabled>' +
		'          <label for="teste-ortoptico">Teste Ortóptico</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Fotocoagulacao à Laser" id="fotocoagulacao-a-laser"' +
		setCheckedExames(customer.exames, "fotocoagulacao-a-laser") +
		' disabled>' +
		'          <label for="fotocoagulacao-a-laser">Fotocoagulacao à Laser</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Gonioscopia" id="gonioscopia"' +
		setCheckedExames(customer.exames, "gonioscopia") +
		' disabled>' +
		'          <label for="gonioscopia">Gonioscopia</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Mapeamento de Retina" id="mapeamento-de-retina"' +
		setCheckedExames(customer.exames, "mapeamento-de-retina") +
		' disabled>' +
		'          <label for="mapeamento-de-retina">Mapeamento de Retina</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Microscopia" id="microscopia"' +
		setCheckedExames(customer.exames, "microscopia") +
		' disabled>' +
		'          <label for="microscopia">Microscopia</label>' +
		"          <br>" +
		'          <input type="checkbox" name="P.A.M." id="pam"' +
		setCheckedExames(customer.exames, "pam") +
		' disabled>' +
		'          <label for="pam">P.A.M.</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Paquimetria" id="paquimetria"' +
		setCheckedExames(customer.exames, "paquimetria") +
		' disabled>' +
		'          <label for="paquimetria">Paquimetria</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Retinografia" id="retinografia"' +
		setCheckedExames(customer.exames, "retinografia") +
		' disabled>' +
		'          <label for="retinografia">Retinografia</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Tonometria" id="tonometria"' +
		setCheckedExames(customer.exames, "tonometria") +
		' disabled>' +
		'          <label for="tonometria">Tonometria</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Ceratoscopia" id="ceratoscopia"' +
		setCheckedExames(customer.exames, "ceratoscopia") +
		' disabled>' +
		'          <label for="ceratoscopia">Ceratoscopia</label>' +
		"          <br>" +
		'          <input type="checkbox" name="OCT" id="oct"' +
		setCheckedExames(customer.exames, "oct") +
		' disabled>' +
		'          <label for="oct">OCT</label>' +
		"          <br>" +
		'          <input type="checkbox" name="Outros" id="outros"' +
		setCheckedExames(customer.exames, "outros") +
		' disabled>' +
		'          <label for="outros">Outros</label>' +
		'          <input type="text" id="outros-input" value="' +
		outros +
		'" disabled>' +
		' <div class="div-auth" sec:authorize="hasAuthority("ADMIN")">' +
		' <button class="button-save" onclick="freeFields(this.id)">Liberar Campos</button>' +
		' <button class="button-update" onclick="updateNF(this.id)" id="' + customer.id + '">Atualizar</button>' +
		' </div>' +
		"          <!-- Checkbox end-->" +
		"      </div>" +
		"      <!-- Exames end-->" +
		"  </div>" +
		"  </div>" +
		"</div>";

	document.getElementById("customer-div").innerHTML = data;
}

function isNull(data) {
	var resp = data;

	if (data === null) {
		resp = "";
	} else {
		resp =
			'<input type="text" name="forma-pagamento" id="forma-pagamento1" value="' +
			data +
			'" disabled>';
	}

	return resp;
}

function setCheckedExames(data, id) {
	var checked = "";

	for (let i = 0; i < data.length; i++) {
		if (id === data[i].nameId) {
			checked = "checked";
		}

		if (data[i].nameId === "outros") {
			outros = data[i].name;
		}
	}

	return checked;
}

function saveNF(id) {
	var nfNumero = document.getElementById("nota-fiscal").value;
	var username = document.getElementById("username").innerText;

	if (nfNumero) {
		axios
			.post("/nf/update", {
				id: id,
				nfNumero: nfNumero,
				nfDone: true,
				nfDoneBy: username,
			})
			.then(function(response) {
				//console.log(response);
				if (response.status === 200) {
					alert("NF salva com sucesso!");
					window.location.reload();
					//setTable();
				}
			})
			.catch(function(error) {
				console.log(error);
				alert(error);
			});
	}
}

function setNfData(data) {
	//console.log(data);
	let timeCreated = unixTimestampToDate(data.timestampCreated);
	let timeNf = unixTimestampToDate(data.timestampNf);
	let text = "";

	if (data.nfDone === false) {
		text +=
			"" +
			"      <!-- Dados NF -->" +
			'          <div class="dados-nf">' +
			'            <div class="nf-time">' +
			"              <h4>Dados NF</h4>" +
			"                <div><b>Data:</b> " +
			timeCreated +
			"</div>" +
			"            </div>" +
			'              <div class="checkbox-div">' +
			'                  <div class="checkbox-div2">' +
			'                      <input type="checkbox" name="sim" id="sim">' +
			'                      <label for="sim">Sim</label>' +
			"                  </div>" +
			'                  <button class="button-save" onclick="saveNF(this.id)" id="' +
			data.id +
			'">Salvar</button>' +
			"              </div>" +
			'              <label for="nota-fiscal">Nota Fiscal Nº</label>' +
			'              <input type="text" name="nota-fiscal" id="nota-fiscal">' +
			"          </div>" +
			"      <!-- Dados NF end-->";
	} else if (data.nfDone === true) {
		text +=
			"" +
			"      <!-- Dados NF -->" +
			'          <div class="dados-nf">' +
			'            <div class="nf-time">' +
			"              <h4>Dados NF</h4>" +
			"                <div><b>Data:</b> " +
			timeCreated +
			"</div>" +
			"            </div>" +
			'              <div class="checkbox-div">' +
			'                  <div class="checkbox-div2">' +
			'                      <input type="checkbox" name="sim" id="sim" checked  disabled>' +
			'                      <label for="sim">Sim</label>' +
			"                  </div>" +
			'            <div class="nf-time">' +
			"                <div><b>Nf Data:</b> " +
			timeNf +
			"</div>" +
			"            </div>" +
			"              </div>" +
			'              <label for="nota-fiscal">Nota Fiscal Nº</label>' +
			'              <input type="text" name="nota-fiscal" id="nota-fiscal" value="' +
			data.nfNumero +
			'" disabled>' +
			"          </div>" +
			"      <!-- Dados NF end-->";
	} else {
		alert("Erro de dados do NF");
	}

	return text;
}

function validateCpf(data) {
	let input = data.toString();
	var part1 = "";
	var part2 = "";
	var part3 = "";
	var part4 = "";
	var resp = "";

	part1 = input.slice(0, 3);
	part2 = input.slice(3, 6);
	part3 = input.slice(6, 9);
	part4 = input.slice(9, 11);

	resp = part1 + "." + part2 + "." + part3 + "-" + part4;

	return resp;
}

function validateCep(data) {
	let input = data.toString();
	var part1 = "";
	var part2 = "";
	var resp = "";

	part1 = input.slice(0, 5);
	part2 = input.slice(5, 8);

	resp = part1 + "-" + part2;

	return resp;
}

function validateTelefone(data) {
	let input = data.toString();
	var part1 = "";
	var part2 = "";
	var resp = "";

	part1 = input.slice(0, 2);
	part2 = input.slice(2, 12);

	resp = "(" + part1 + ")" + part2;

	return resp;
}

function validateValor(input) {
	var valueInput = input.toString();
	valueInput = valueInput.replaceAll(",", "");
	valueInput = valueInput.replaceAll(".", "");
	let arrInput = valueInput.split("");
	//console.log(arrInput);

	switch (arrInput.length) {
		case 3:
			valueInput = arrInput[0] + "," + arrInput[1] + arrInput[2];
			break;

		case 4:
			valueInput = arrInput[0] + arrInput[1] + "," + arrInput[2] + arrInput[3];
			break;

		case 5:
			valueInput =
				arrInput[0] +
				arrInput[1] +
				arrInput[2] +
				"," +
				arrInput[3] +
				arrInput[4];
			break;

		case 6:
			valueInput =
				arrInput[0] +
				"." +
				arrInput[1] +
				arrInput[2] +
				arrInput[3] +
				"," +
				arrInput[4] +
				arrInput[5];
			break;

		case 7:
			valueInput =
				arrInput[0] +
				arrInput[1] +
				"." +
				arrInput[2] +
				arrInput[3] +
				arrInput[4] +
				"," +
				arrInput[5] +
				arrInput[6];
			break;

		case 8:
			valueInput =
				arrInput[0] +
				arrInput[1] +
				arrInput[2] +
				"." +
				arrInput[3] +
				arrInput[4] +
				arrInput[5] +
				"," +
				arrInput[6] +
				arrInput[7];
			break;

		case 9:
			valueInput =
				arrInput[0] +
				"." +
				arrInput[1] +
				arrInput[2] +
				arrInput[3] +
				"," +
				arrInput[4] +
				arrInput[5] +
				arrInput[6] +
				"," +
				arrInput[7] +
				arrInput[8];
			break;

		case 10:
			valueInput =
				arrInput[0] +
				arrInput[1] +
				"." +
				arrInput[2] +
				arrInput[3] +
				arrInput[4] +
				"," +
				arrInput[5] +
				arrInput[6] +
				arrInput[7] +
				"," +
				arrInput[8] +
				arrInput[9];
			break;

		default:
	}

	return valueInput;
}

function changeDateformat(val) {
	const myArray = val.split("-");
	let year = myArray[0];
	let month = myArray[1];
	let day = myArray[2];
	let formatteddate = day + "/" + month + "/" + year;

	return formatteddate;
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

function freeFields() {
	document.getElementById("prontuario").disabled = false;
	document.getElementById("nota-fiscal").disabled = false;

	document.getElementById("consulta").disabled = false;
	document.getElementById("exame").disabled = false;

	document.getElementById("nome").disabled = false;
	//const cpf = document.getElementById("cpf").disabled = false;
	document.getElementById("e-mail").disabled = false;
	document.getElementById("telefone").disabled = false;
	document.getElementById("cep").disabled = false;
	document.getElementById("rua").disabled = false;
	document.getElementById("numero").disabled = false;
	document.getElementById("complemento").disabled = false;
	document.getElementById("bairro").disabled = false;
	document.getElementById("cidade").disabled = false;
	document.getElementById("estado").disabled = false;
	document.getElementById("valor").disabled = false;
	document.getElementById("textarea-tag").disabled = false;
	document.getElementById("forma-pagamento").disabled = false;

	if (document.getElementById("forma-pagamento1")) {
		document.getElementById("forma-pagamento1").disabled = false;
	}

	document.getElementById("medico").disabled = false;

	// Exames
	document.getElementById("biometria").disabled = false;
	document.getElementById("campimetria").disabled = false;
	document.getElementById("capsulotomia").disabled = false;
	document.getElementById("curva-tensional").disabled = false;
	document.getElementById("teste-ortoptico").disabled = false;
	document.getElementById("fotocoagulacao-a-laser").disabled = false;
	document.getElementById("gonioscopia").disabled = false;
	document.getElementById("mapeamento-de-retina").disabled = false;
	document.getElementById("microscopia").disabled = false;
	document.getElementById("pam").disabled = false;
	document.getElementById("paquimetria").disabled = false;
	document.getElementById("retinografia").disabled = false;
	document.getElementById("tonometria").disabled = false;
	document.getElementById("ceratoscopia").disabled = false;
	document.getElementById("oct").disabled = false;
	document.getElementById("outros").disabled = false;
	document.getElementById("outros-input").disabled = false;
	document.getElementById("pagamento-extra").disabled = false;
}

function updateNF(id) {
	const prontuario = document.getElementById("prontuario");

	const consulta = document.getElementById("consulta");
	const exame = document.getElementById("exame");

	const nome = document.getElementById("nome");
	const cpf = document.getElementById("cpf");
	const email = document.getElementById("e-mail");
	const telefone = document.getElementById("telefone");
	const cep = document.getElementById("cep");
	const rua = document.getElementById("rua");
	const numero = document.getElementById("numero");
	const complemento = document.getElementById("complemento");
	const bairro = document.getElementById("bairro");
	const cidade = document.getElementById("cidade");
	const estado = document.getElementById("estado");
	const valor = document.getElementById("valor");
	const formaPagamento = document.getElementById("forma-pagamento");
	const medico = document.getElementById("medico");
	const nfNumero = document.getElementById("nota-fiscal");
	
	var username = document.getElementById("username").innerText;

	getExamesSelecionados();
	getDadosPagamento();
	
	axios
		.post("/nf/updatenf", {
			id: id,
			prontuario: prontuario.value,
			consulta: consulta.checked,
			exame: exame.checked,
			nome: nome.value,
			cpf: removeSpecialCharacters(cpf.value),
			email: email.value,
			telefone: removeSpecialCharacters(telefone.value),
			cep: removeSpecialCharacters(cep.value),
			rua: rua.value,
			numero: removeSpecialCharacters(numero.value),
			complemento: complemento.value,
			bairro: bairro.value,
			cidade: cidade.value,
			estado: estado.value,
			valor: removeSpecialCharacters(valor.value),
			formaPagamento: formaPagamento.value,
			medico: medico.value,
			nfNumero: nfNumero.value,
			nfDone: customer.nfDone,
			userName: username,
			nfDoneBy: customer.nfDoneBy,
			pagamento1: dadosPagamento[0],
			pagamento2: dadosPagamento[1],
			exames: examesSelecionados,
		})
		.then(function(response) {
			//console.log(response);
			if (response.status === 200) {
				alert("Documento salvo com sucesso!");
				window.location.reload();
			}
		})
		.catch(function(error) {
			console.log(error);
			alert(error);
		});
}

function getExamesSelecionados() {
	// Exames
	const biometria = document.getElementById("biometria");
	const campimetria = document.getElementById("campimetria");
	const capsulotomia = document.getElementById("capsulotomia");
	const curvaTensional = document.getElementById("curva-tensional");
	const testeOrtoptico = document.getElementById("teste-ortoptico");
	const fotocoagulacaoALaser = document.getElementById("fotocoagulacao-a-laser");
	const gonioscopia = document.getElementById("gonioscopia");
	const mapeamentoDeRetina = document.getElementById("mapeamento-de-retina");
	const microscopia = document.getElementById("microscopia");
	const pam = document.getElementById("pam");
	const paquimetria = document.getElementById("paquimetria");
	const retinografia = document.getElementById("retinografia");
	const tonometria = document.getElementById("tonometria");
	const ceratoscopia = document.getElementById("ceratoscopia");
	const oct = document.getElementById("oct");
	const outros = document.getElementById("outros");
	const outrosInput = document.getElementById("outros-input");


	examesSelecionados = [];

	if (biometria.checked) {
		examesSelecionados.push({
			nameId: biometria.id,
			name: biometria.name,
		});
	}

	if (campimetria.checked) {
		examesSelecionados.push({
			nameId: campimetria.id,
			name: campimetria.name,
		});
	}

	if (capsulotomia.checked) {
		examesSelecionados.push({
			nameId: capsulotomia.id,
			name: capsulotomia.name,
		});
	}

	if (curvaTensional.checked) {
		examesSelecionados.push({
			nameId: curvaTensional.id,
			name: curvaTensional.name,
		});
	}

	if (testeOrtoptico.checked) {
		examesSelecionados.push({
			nameId: testeOrtoptico.id,
			name: testeOrtoptico.name,
		});
	}

	if (fotocoagulacaoALaser.checked) {
		examesSelecionados.push({
			nameId: fotocoagulacaoALaser.id,
			name: fotocoagulacaoALaser.name,
		});
	}

	if (gonioscopia.checked) {
		examesSelecionados.push({
			nameId: gonioscopia.id,
			name: gonioscopia.name,
		});
	}

	if (mapeamentoDeRetina.checked) {
		examesSelecionados.push({
			nameId: mapeamentoDeRetina.id,
			name: mapeamentoDeRetina.name,
		});
	}

	if (microscopia.checked) {
		examesSelecionados.push({
			nameId: microscopia.id,
			name: microscopia.name,
		});
	}

	if (pam.checked) {
		examesSelecionados.push({
			nameId: pam.id,
			name: pam.name,
		});
	}

	if (paquimetria.checked) {
		examesSelecionados.push({
			nameId: paquimetria.id,
			name: paquimetria.name,
		});
	}

	if (retinografia.checked) {
		examesSelecionados.push({
			nameId: retinografia.id,
			name: retinografia.name,
		});
	}

	if (tonometria.checked) {
		examesSelecionados.push({
			nameId: tonometria.id,
			name: tonometria.name,
		});
	}

	if (ceratoscopia.checked) {
		examesSelecionados.push({
			nameId: ceratoscopia.id,
			name: ceratoscopia.name,
		});
	}

	if (oct.checked) {
		examesSelecionados.push({
			nameId: oct.id,
			name: oct.name,
		});
	}

	if (outros.checked) {
		examesSelecionados.push({
			nameId: outros.id,
			name: outrosInput.value,
		});
	}
}

// remove special characters end return only number
function removeSpecialCharacters(valueInput) {
	valueInput = valueInput.replaceAll(",", "");
	valueInput = valueInput.replaceAll(".", "");
	valueInput = valueInput.replaceAll(" ", "");
	valueInput = valueInput.replaceAll("-", "");
	valueInput = valueInput.replaceAll("(", "");
	valueInput = valueInput.replaceAll(")", "");

	return valueInput;
}

function getDadosPagamento() {
	dadosPagamento = [];

	let textareaTag = document.getElementById("textarea-tag").value;
	dadosPagamento.push(textareaTag);
	let formaPagamento = document.getElementById("forma-pagamento").value;

	//get value from select tag cartão de crédito - número de parcelas
	if (formaPagamento === "Cartão Crédito") {
		let cartaoCredValue = document.getElementById("forma-pagamento1").value;
		dadosPagamento.push(cartaoCredValue);
	} else {
		dadosPagamento.push(null);
	}
}

window.addEventListener("onload", onloadInit());
window.addEventListener("onload", onloadInit);
window.saveNF = saveNF;
window.loadWantsToTalk = loadWantsToTalk;
window.changeformat = changeformat;
window.getCustomerId = getCustomerId;
window.changeDateformat = changeDateformat;
window.validateValor = validateValor;
window.setNfData = setNfData;
window.toggleHamburger = toggleHamburger;
window.freeFields = freeFields;
window.getDadosPagamento = getDadosPagamento;
window.updateNF = updateNF;
