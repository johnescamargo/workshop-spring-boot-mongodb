import Websocket from "./websocket.js";
const websocket = new Websocket();

const nfDiv = document.getElementById("nf-div");
const relatorioDiv = document.getElementById("relatorio-div");
const nfMenu = document.getElementById("nf-menu");
const relatorioMenu = document.getElementById("relatorio-menu");
const rightSide = document.getElementById("right-side");

var dadosPagamento = [];
var examesSelecionados = [];
var customer;
var datepicker = document.getElementById("datepicker");
var pesquisa = document.getElementById("pesquisa");
var month = document.getElementById("start");
var doctor = document.getElementById("medico1");
var outros = "";
let role;

var relatorioArr = [];
var monthRelatorio;
var yearRelatorio;

const textName = document.getElementById('text-name');
const textDate = document.getElementById('text-date');

const bioQuant = document.getElementById('bio-quant');
const bioValorTotal = document.getElementById('bio-valor-total');
const bioPercentage = document.getElementById('bio-percent');
const bioPercResult = document.getElementById('bio-perc-total');

const camQuant = document.getElementById('cam-quant');
const camValorTotal = document.getElementById('cam-valor-total');
const camPercentage = document.getElementById('cam-percent');
const camPercResult = document.getElementById('cam-perc-total');

const capQuant = document.getElementById('cap-quant');
const capValorTotal = document.getElementById('cap-valor-total');
const capPercentage = document.getElementById('cap-percent');
const capPercResult = document.getElementById('cap-perc-total');

const cteQuant = document.getElementById('cte-quant');
const cteValorTotal = document.getElementById('cte-valor-total');
const ctePercentage = document.getElementById('cte-percent');
const ctePercResult = document.getElementById('cte-perc-total');

const torQuant = document.getElementById('tor-quant');
const torValorTotal = document.getElementById('tor-valor-total');
const torPercentage = document.getElementById('tor-percent');
const torPercResult = document.getElementById('tor-perc-total');

const fotQuant = document.getElementById('fot-quant');
const fotValorTotal = document.getElementById('fot-valor-total');
const fotPercentage = document.getElementById('fot-percent');
const fotPercResult = document.getElementById('fot-perc-total');

const gonQuant = document.getElementById('gon-quant');
const gonValorTotal = document.getElementById('gon-valor-total');
const gonPercentage = document.getElementById('gon-percent');
const gonPercResult = document.getElementById('gon-perc-total');

const mapQuant = document.getElementById('map-quant');
const mapValorTotal = document.getElementById('map-valor-total');
const mapPercentage = document.getElementById('map-percent');
const mapPercResult = document.getElementById('map-perc-total');

const micQuant = document.getElementById('mic-quant');
const micValorTotal = document.getElementById('mic-valor-total');
const micPercentage = document.getElementById('mic-percent');
const micPercResult = document.getElementById('mic-perc-total');

const paQuant = document.getElementById('pam-quant');
const paValorTotal = document.getElementById('pam-valor-total');
const paPercentage = document.getElementById('pam-percent');
const paPercResult = document.getElementById('pam-perc-total');

const paqQuant = document.getElementById('paq-quant');
const paqValorTotal = document.getElementById('paq-valor-total');
const paqPercentage = document.getElementById('paq-percent');
const paqPercResult = document.getElementById('paq-perc-total');

const retQuant = document.getElementById('ret-quant');
const retValorTotal = document.getElementById('ret-valor-total');
const retPercentage = document.getElementById('ret-percent');
const retPercResult = document.getElementById('ret-perc-total');

const tonQuant = document.getElementById('ton-quant');
const tonValorTotal = document.getElementById('ton-valor-total');
const tonPercentage = document.getElementById('ton-percent');
const tonPercResult = document.getElementById('ton-perc-total');

const cerQuant = document.getElementById('cer-quant');
const cerValorTotal = document.getElementById('cer-valor-total');
const cerPercentage = document.getElementById('cer-percent');
const cerPercResult = document.getElementById('cer-perc-total');

const ocQuant = document.getElementById('oct-quant');
const ocValorTotal = document.getElementById('oct-valor-total');
const ocPercentage = document.getElementById('oct-percent');
const ocPercResult = document.getElementById('oct-perc-total');

const valorTotal = document.getElementById('valor-total');
const valorPercTotal = document.getElementById('valor-perc-total');

var name = "";
var monthName = "";

var medico2 = "";
var name2 = "";

var biometriaQuant = 0;
var campimetriaQuant = 0;
var capsulotomiaQuant = 0;
var curvaTensionalQuant = 0;
var testeOrtopticoQuant = 0;
var fotocoagulacaoALaserQuant = 0;
var gonioscopiaQuant = 0;
var mapeamentoDeRetinaQuant = 0;
var microscopiaQuant = 0;
var pamQuant = 0;
var paquimetriaQuant = 0;
var retinografiaQuant = 0;
var tonometriaQuant = 0;
var ceratoscopiaQuant = 0;
var octQuant = 0;

var biometriaAmount = 0;
var campimetriaAmount = 0;
var capsulotomiaAmount = 0;
var curvaTensionalAmount = 0;
var testeOrtopticoAmount = 0;
var fotocoagulacaoALaserAmount = 0;
var gonioscopiaAmount = 0;
var mapeamentoDeRetinaAmount = 0;
var microscopiaAmount = 0;
var pamAmount = 0;
var paquimetriaAmount = 0;
var retinografiaAmount = 0;
var tonometriaAmount = 0;
var ceratoscopiaAmount = 0;
var octAmount = 0;

var biometriaPercentage = document.getElementById('biometria1');
var campimetriaPercentage = document.getElementById('campimetria1');
var capsulotomiaPercentage = document.getElementById('capsulotomia1');
var curvaTensionalPercentage = document.getElementById('curva-tensional1');
var testeOrtopticoPercentage = document.getElementById('teste-ortoptico1');
var fotocoagulacaoALaserPercentage = document.getElementById('fotocoagulacao-a-laser1');
var gonioscopiaPercentage = document.getElementById('gonioscopia1');
var mapeamentoDeRetinaPercentage = document.getElementById('mapeamento-de-retina1');
var microscopiaPercentage = document.getElementById('microscopia1');
var pamPercentage = document.getElementById('pam1');
var paquimetriaPercentage = document.getElementById('paquimetria1');
var retinografiaPercentage = document.getElementById('retinografia1');
var tonometriaPercentage = document.getElementById('tonometria1');
var ceratoscopiaPercentage = document.getElementById('ceratoscopia1');
var octPercentage = document.getElementById('oct1');

var biometriaPercResult = 0;
var campimetriaPercResult = 0;
var capsulotomiaPercResult = 0;
var curvaTensionalPercResult = 0;
var testeOrtopticoPercResult = 0;
var fotocoagulacaoALaserPercResult = 0;
var gonioscopiaPercResult = 0;
var mapeamentoDeRetinaPercResult = 0;
var microscopiaPercResult = 0;
var pamPercResult = 0;
var paquimetriaPercResult = 0;
var retinografiaPercResult = 0;
var tonometriaPercResult = 0;
var ceratoscopiaPercResult = 0;
var octPercResult = 0;

var totalAmount = 0;
var totalPercAmount = 0;

function onloadInit() {
	loadWantsToTalk();
	setTable();
	websocket.connect;
	getCustomerRole();
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

if (month) {
	month.addEventListener("input", function() {
		getMonthByDate(this.value);
		//getMonthByDateAndMedico(this.value);
	});
}

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
			//console.log(response);
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

function getMonthByDate(data) {
	var resp = changeDateformatMonth(data);

	axios
		.get("/nf/getbymonth", {
			params: {
				date: resp
			},
		})
		.then(function(response) {
			//console.log(response.data);
			saveRelatorioArr(response.data);
		})
		.catch(function(error) {
			console.log(error);
		});
}

function getMonthByDateAndMedico(data) {
	var resp = changeDateformatMonth(data);

	axios
		.get("/nf/getbymonthandmedico", {
			params: {
				date: resp,
				doctor: doctor.value
			},
		})
		.then(function(response) {
			//console.log(response.data);
			saveRelatorioArr(response.data);
		})
		.catch(function(error) {
			console.log(error);
		});
}


function getCustomerRole() {

	var username = document.getElementById("username");

	axios
		.get("/user/getrole", {
			params: {
				username: username.innerHTML
			},
		})
		.then(function(response) {
			//console.log(response.data);
			//sessionStorage.setItem("role" ,response.data);
			role = response.data;
		})
		.catch(function(error) {
			console.log(error);
		});
}

function saveRelatorioArr(arr) {

	relatorioArr = [];

	for (let i = 0; i < arr.length; i++) {
		relatorioArr.push(arr[i])
	}
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
		'<div class="frame-div" id="frame-div">' +
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
		setRole(customer) +
		"          <!-- Checkbox end-->" +
		"      </div>" +
		"      <!-- Exames end-->" +
		"  </div>" +
		"  </div>" +
		"</div>";

	document.getElementById("customer-div").innerHTML = data;
}

function setRole(customer) {
	var data = "";
	getCustomerRole();

	//let role = sessionStorage.getItem("role");

	if (role === 'ADMIN') {
		data += ' <div class="div-auth">' +
			' <button class="button-save" onclick="freeFields(this.id)">Liberar Campos</button>' +
			' <button class="button-update" onclick="updateNF(this.id)" id="' + customer.id + '">Atualizar</button>' +
			' </div>';
	}

	return data;
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

function changeDateformatMonth(val) {
	const myArray = val.split("-");
	let year = myArray[0];
	let month = myArray[1];
	let formatteddate = month + "/" + year;

	monthRelatorio = month;
	yearRelatorio = year;

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

function setNfRelatorio() {

	nfDiv.style.display = 'none';
	nfMenu.style.backgroundColor = 'rgb(17 41 70)';

	relatorioDiv.style.display = 'flex';
	relatorioMenu.style.backgroundColor = 'rgb(27 67 114)';

}

function setNfMenu() {

	nfDiv.style.display = 'flex';
	nfMenu.style.backgroundColor = 'rgb(27 67 114)';

	relatorioDiv.style.display = 'none';
	relatorioMenu.style.backgroundColor = 'rgb(17 41 70)';

}

function calculateMonthRelatorio() {

	rightSide.style.display = 'block';

	biometriaQuant = 0;
	biometriaAmount = 0;
	biometriaPercResult = 0;

	campimetriaQuant = 0;
	campimetriaAmount = 0;
	campimetriaPercResult = 0;

	capsulotomiaQuant = 0;
	capsulotomiaAmount = 0;
	capsulotomiaPercResult = 0;

	curvaTensionalQuant = 0;
	curvaTensionalAmount = 0;
	curvaTensionalPercResult = 0;

	testeOrtopticoQuant = 0;
	testeOrtopticoAmount = 0;
	testeOrtopticoPercResult = 0;

	fotocoagulacaoALaserQuant = 0;
	fotocoagulacaoALaserAmount = 0;
	fotocoagulacaoALaserPercResult = 0;

	gonioscopiaQuant = 0;
	gonioscopiaAmount = 0;
	gonioscopiaPercResult = 0;

	mapeamentoDeRetinaQuant = 0;
	mapeamentoDeRetinaAmount = 0;
	mapeamentoDeRetinaPercResult = 0;

	microscopiaQuant = 0;
	microscopiaAmount = 0;
	microscopiaPercResult = 0;

	pamQuant = 0;
	pamAmount = 0;
	pamPercResult = 0;

	paquimetriaQuant = 0;
	paquimetriaAmount = 0;
	paquimetriaPercResult = 0;

	retinografiaQuant = 0;
	retinografiaAmount = 0;
	retinografiaPercResult = 0;

	tonometriaQuant = 0;
	tonometriaAmount = 0;
	tonometriaPercResult = 0;

	ceratoscopiaQuant = 0;
	ceratoscopiaAmount = 0;
	ceratoscopiaPercResult = 0;

	octQuant = 0;
	octAmount = 0;
	octPercResult = 0;

	monthName = getMonthName(monthRelatorio);


	if (relatorioArr.length !== 0) {
		name = doctor.value;

		for (let i = 0; i < relatorioArr.length; i++) {

			var amount = relatorioArr[i].valor;
			var lengthArr = relatorioArr[i].exames.length;

			for (let j = 0; j < relatorioArr[i].exames.length; j++) {

				var medico = relatorioArr[i].medico;
				medico2 = splitName(medico);
				name2 = splitName(name);

				if (medico2 === name2) {

					var nameId = relatorioArr[i].exames[j].nameId;
					var valor = amount / lengthArr;

					valor = splitNumber(valor);

					if (nameId === 'biometria') {
						biometriaQuant++;
						biometriaAmount = biometriaAmount + valor;
						biometriaPercResult = calculatePercentage(biometriaAmount, biometriaPercentage.value);

					} else if (nameId === 'campimetria') {
						campimetriaQuant++;
						campimetriaAmount = campimetriaAmount + valor;
						campimetriaPercResult = calculatePercentage(campimetriaAmount, campimetriaPercentage.value);

					} else if (nameId === 'capsulotomia') {
						capsulotomiaQuant++;
						capsulotomiaAmount = capsulotomiaAmount + valor;
						capsulotomiaPercResult = calculatePercentage(capsulotomiaAmount, capsulotomiaPercentage.value);

					} else if (nameId === 'curva-tensional') {
						curvaTensionalQuant++;
						curvaTensionalAmount = curvaTensionalAmount + valor;
						curvaTensionalPercResult = calculatePercentage(curvaTensionalAmount, curvaTensionalPercentage.value);

					} else if (nameId === 'teste-ortoptico') {
						testeOrtopticoQuant++;
						testeOrtopticoAmount = testeOrtopticoAmount + valor;
						testeOrtopticoPercResult = calculatePercentage(testeOrtopticoAmount, testeOrtopticoPercentage.value);

					} else if (nameId === 'fotocoagulacao-a-laser') {
						fotocoagulacaoALaserQuant++;
						fotocoagulacaoALaserAmount = fotocoagulacaoALaserAmount + valor;
						fotocoagulacaoALaserPercResult = calculatePercentage(fotocoagulacaoALaserAmount, fotocoagulacaoALaserPercentage.value);

					} else if (nameId === 'gonioscopia') {
						gonioscopiaQuant++;
						gonioscopiaAmount = gonioscopiaAmount + valor;
						gonioscopiaPercResult = calculatePercentage(gonioscopiaAmount, gonioscopiaPercentage.value);

					} else if (nameId === 'mapeamento de Retina') {
						mapeamentoDeRetinaQuant++;
						mapeamentoDeRetinaAmount = mapeamentoDeRetinaAmount + valor;
						mapeamentoDeRetinaPercResult = calculatePercentage(mapeamentoDeRetinaAmount, mapeamentoDeRetinaPercentage.value);

					} else if (nameId === 'microscopia') {
						microscopiaQuant++;
						microscopiaAmount = microscopiaAmount + valor;
						microscopiaPercResult = calculatePercentage(microscopiaAmount, microscopiaPercentage.value);

					} else if (nameId === 'pam') {
						pamQuant++;
						pamAmount = pamAmount + valor;
						pamPercResult = calculatePercentage(pamAmount, pamPercentage.value);

					} else if (nameId === 'paquimetria') {
						paquimetriaQuant++;
						paquimetriaAmount = paquimetriaAmount + valor;
						paquimetriaPercResult = calculatePercentage(paquimetriaAmount, paquimetriaPercentage.value);

					} else if (nameId === 'retinografia') {
						retinografiaQuant++;
						retinografiaAmount = retinografiaAmount + valor;
						retinografiaPercResult = calculatePercentage(retinografiaAmount, retinografiaPercentage.value);

					} else if (nameId === 'tonometria') {
						tonometriaQuant++;
						tonometriaAmount = tonometriaAmount + valor;
						tonometriaPercResult = calculatePercentage(tonometriaAmount, tonometriaPercentage.value);

					} else if (nameId === 'ceratoscopia') {
						ceratoscopiaQuant++;
						ceratoscopiaAmount = ceratoscopiaAmount + valor;
						ceratoscopiaPercResult = calculatePercentage(ceratoscopiaAmount, ceratoscopiaPercentage.value);

					} else if (nameId === 'oct') {
						octQuant++;
						octAmount = octAmount + valor;
						octPercResult = calculatePercentage(octAmount, octPercentage.value);

					} else {
						console.log("nameId Not found")
					}

				}
			}
		}

		totalAmount = (
			biometriaAmount +
			campimetriaAmount +
			capsulotomiaAmount +
			curvaTensionalAmount +
			testeOrtopticoAmount +
			fotocoagulacaoALaserAmount +
			gonioscopiaAmount +
			mapeamentoDeRetinaAmount +
			microscopiaAmount +
			pamAmount +
			paquimetriaAmount +
			retinografiaAmount +
			tonometriaAmount +
			ceratoscopiaAmount +
			octAmount
		);

		totalPercAmount = (
			biometriaPercResult +
			campimetriaPercResult +
			capsulotomiaPercResult +
			curvaTensionalPercResult +
			testeOrtopticoPercResult +
			fotocoagulacaoALaserPercResult +
			gonioscopiaPercResult +
			mapeamentoDeRetinaPercResult +
			microscopiaPercResult +
			pamPercResult +
			paquimetriaPercResult +
			retinografiaPercResult +
			tonometriaPercResult +
			ceratoscopiaPercResult +
			octPercResult
		);

		//console.log(totalPercAmount);
	}

	setHtmlRelatorio();
}

function calculatePercentage(amount, percentage) {

	let valor = (amount * percentage) / 100;
	let text = valor.toString();
	const myArray = text.split(".");
	let result = parseInt(myArray[0]);

	return result;
}

function setHtmlRelatorio() {

	textName.innerHTML = name;
	textDate.innerHTML = monthName + "/" + yearRelatorio;

	bioQuant.innerHTML = biometriaQuant;
	bioValorTotal.innerHTML = validateValor(biometriaAmount);
	bioPercentage.innerHTML = biometriaPercentage.value + ' %';
	bioPercResult.innerHTML = validateValor(biometriaPercResult);

	camQuant.innerHTML = campimetriaQuant;
	camValorTotal.innerHTML = validateValor(campimetriaAmount);
	camPercentage.innerHTML = campimetriaPercentage.value + ' %';
	camPercResult.innerHTML = validateValor(campimetriaPercResult);

	capQuant.innerHTML = capsulotomiaQuant;
	capValorTotal.innerHTML = validateValor(capsulotomiaAmount);
	capPercentage.innerHTML = capsulotomiaPercentage.value + ' %';
	capPercResult.innerHTML = validateValor(capsulotomiaPercResult);

	cteQuant.innerHTML = curvaTensionalQuant;
	cteValorTotal.innerHTML = validateValor(curvaTensionalAmount);
	ctePercentage.innerHTML = curvaTensionalPercentage.value + ' %';
	ctePercResult.innerHTML = validateValor(curvaTensionalPercResult);

	torQuant.innerHTML = testeOrtopticoQuant;
	torValorTotal.innerHTML = validateValor(testeOrtopticoAmount);
	torPercentage.innerHTML = testeOrtopticoPercentage.value + ' %';
	torPercResult.innerHTML = validateValor(testeOrtopticoPercResult);

	fotQuant.innerHTML = fotocoagulacaoALaserQuant;
	fotValorTotal.innerHTML = validateValor(fotocoagulacaoALaserAmount);
	fotPercentage.innerHTML = fotocoagulacaoALaserPercentage.value + ' %';
	fotPercResult.innerHTML = validateValor(fotocoagulacaoALaserPercResult);

	gonQuant.innerHTML = gonioscopiaQuant;
	gonValorTotal.innerHTML = validateValor(gonioscopiaAmount);
	gonPercentage.innerHTML = gonioscopiaPercentage.value + ' %';
	gonPercResult.innerHTML = validateValor(gonioscopiaPercResult);

	mapQuant.innerHTML = mapeamentoDeRetinaQuant;
	mapValorTotal.innerHTML = validateValor(mapeamentoDeRetinaAmount);
	mapPercentage.innerHTML = mapeamentoDeRetinaPercentage.value + ' %';
	mapPercResult.innerHTML = validateValor(mapeamentoDeRetinaPercResult);

	micQuant.innerHTML = microscopiaQuant;
	micValorTotal.innerHTML = validateValor(microscopiaAmount);
	micPercentage.innerHTML = microscopiaPercentage.value + ' %';
	micPercResult.innerHTML = validateValor(microscopiaPercResult);

	paQuant.innerHTML = pamQuant;
	paValorTotal.innerHTML = validateValor(pamAmount);
	paPercentage.innerHTML = pamPercentage.value + ' %';
	paPercResult.innerHTML = validateValor(pamPercResult);

	paqQuant.innerHTML = paquimetriaQuant;
	paqValorTotal.innerHTML = validateValor(paquimetriaAmount);
	paqPercentage.innerHTML = paquimetriaPercentage.value + ' %';
	paqPercResult.innerHTML = validateValor(paquimetriaPercResult);

	retQuant.innerHTML = retinografiaQuant;
	retValorTotal.innerHTML = validateValor(retinografiaAmount);
	retPercentage.innerHTML = retinografiaPercentage.value + ' %';
	retPercResult.innerHTML = validateValor(retinografiaPercResult);

	tonQuant.innerHTML = tonometriaQuant;
	tonValorTotal.innerHTML = validateValor(tonometriaAmount);
	tonPercentage.innerHTML = tonometriaPercentage.value + ' %';
	tonPercResult.innerHTML = validateValor(tonometriaPercResult);

	cerQuant.innerHTML = ceratoscopiaQuant;
	cerValorTotal.innerHTML = validateValor(ceratoscopiaAmount);
	cerPercentage.innerHTML = ceratoscopiaPercentage.value + ' %';
	cerPercResult.innerHTML = validateValor(ceratoscopiaPercResult);

	ocQuant.innerHTML = octQuant;
	ocValorTotal.innerHTML = validateValor(octAmount);
	ocPercentage.innerHTML = octPercentage.value + ' %';
	ocPercResult.innerHTML = validateValor(octPercResult);

	valorTotal.innerHTML = validateValor(totalAmount);
	valorPercTotal.innerHTML = validateValor(totalPercAmount);

}

function splitNumber(number) {
	let text = number.toString();
	const myArray = text.split(".");
	let num = parseInt(myArray[0]);

	return num;
}

function splitName(name) {

	let text = name.toString();
	const myArray = text.split(" ");
	let resp = myArray[0];

	return resp;
}

function getMonthName(id) {
	var resp;

	const months = [
		{ id: '01', name: 'Janeiro' },
		{ id: '02', name: 'Fevereiro' },
		{ id: '03', name: 'Março' },
		{ id: '04', name: 'Abril' },
		{ id: '05', name: 'Maio' },
		{ id: '06', name: 'Junho' },
		{ id: '07', name: 'Julho' },
		{ id: '08', name: 'Agosto' },
		{ id: '09', name: 'Setembro' },
		{ id: '10', name: 'Outubro' },
		{ id: '11', name: 'Novembro' },
		{ id: '12', name: 'Dezembro' }
	];

	for (let i = 0; i < months.length; i++) {
		if (months[i].id === id) {
			resp = months[i].name;
			break;
		}
	}
	return resp;
}

function printDiv2(divId) {
	var printContents = document.getElementById(divId).innerHTML;
	var originalContents = document.body.innerHTML;

	document.body.innerHTML = printContents;

	window.print();

	document.body.innerHTML = originalContents;
}


function printDiv(divId) {
	var divText = document.getElementById(divId).innerHTML;
	var myWindow = window.open('', '', 'width=650,height=880');
	var doc = myWindow.document;

	var html1 = '<!DOCTYPE html> <html> <head> <style> :root { --border-color-div: rgb(207, 207, 207); } .button-save { width: 125px; height: 35px; margin-top: 4px; border-radius: 8px; background-color: rgb(57, 57, 192); color: aliceblue; cursor: pointer; border-style: none; } .button-save:hover { background-color: rgb(94, 94, 185); } .relatorio-month-card { min-height: auto; background-color: white; border-radius: 8px; padding: 10px; border-left: 4px solid rgb(32, 74, 135); border-right: 1px solid var(--border-color-div); border-bottom: 1px solid var(--border-color-div); border-top: 1px solid var(--border-color-div); margin: 10px; } .relatorio-name-date { display: flex; } table { font-family: arial, sans-serif; border-collapse: collapse; width: 100%; } td, th { border: 1px solid #dddddd; text-align: left; padding: 8px; } tr:nth-child(even) { background-color: #dddddd; } .label-rel { margin-right: 5px; margin-bottom: 5px; } </style> </head> <body><div id="right-side">';
	var html2 = '</div></body> <script> function printDiv() { window.print(); } </script> </html>';
	doc.open();
	doc.write(html1, divText, html2);
	doc.close();
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
window.setNfRelatorio = setNfRelatorio;
window.setNfMenu = setNfMenu;
window.calculateMonthRelatorio = calculateMonthRelatorio;
window.printDiv = printDiv;
window.getMonthByDateAndMedico = getMonthByDateAndMedico;
window.getCustomerRole = getCustomerRole;

