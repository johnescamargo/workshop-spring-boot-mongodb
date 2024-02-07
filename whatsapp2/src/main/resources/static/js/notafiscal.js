var datepicker = document.getElementById("datepicker");
var pesquisa = document.getElementById("pesquisa");
var dataTable = document.getElementById("data-table");
var outros = "";
var datepicked = "";
var stompClient = null;
const dropdown = document.querySelector("#exit");

function connect() {
	websocketFunction();
	loadWantsToTalk();
	setTable();
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
	//var socket = new SockJS("https://www.web.login.imav.com.br:5000/websocket-server");

	// Localhost
	var socket = new SockJS("http://localhost:5000/websocket-server");

	stompClient = Stomp.over(socket);
	stompClient.connect({}, function () {
		setConnected(true);

		// Update reload DOM
		stompClient.subscribe("/topic/talks", function (response) {
			//console.log(response);
			loadWantsToTalk();
		});
	});
}

function loadWantsToTalk() {
	axios({
		method: "get",
		url: "/talk/all",
	}).then(function (response) {
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
dropdown.addEventListener("click", function () {
	myDropdown();
});






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

datepicker.addEventListener("input", function () {
	getCustomersByDate(this.value);
});

pesquisa.addEventListener("input", function () {

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
		.then(function (response) {
			//console.log(response);
			displayTableHtml(response);
		})
		.catch(function (error) {
			console.log(error);
		});
}

function displayTableHtml(data) {

	var dataTable = '';
	var dataLength = data.data.length;

	for (let i = (dataLength - 1); i >= 0; i--) {

		let id = data.data[i].id;
		let time = unixTimestampToDate(data.data[i].timestampCreated);

		if (data.data[i].nfDone === false) {
			dataTable += '<div class="table-customers-red" id="' + id + '" onclick="getCustomerId(this.id)" >'
				+ '<div class="name">' + data.data[i].nome.toUpperCase() + '</div>'
				+ '<div class="div-date">' + time + '</div>'
				+ '</div>';
		} else {
			dataTable += '<div class="table-customers-green" id="' + id + '" onclick="getCustomerId(this.id)" >'
				+ '<div class="name">' + data.data[i].nome.toUpperCase() + '</div>'
				+ '<div class="div-date">' + time + '</div>'
				+ '</div>';
		}
	}

	document.getElementById("data-table").innerHTML = dataTable;
}

function getCustomerId(id) {
	//console.log(id);
	axios
		.get("/nf/getcustomer", {
			params: {
				id: id
			}
		}
		)
		.then(function (response) {
			//console.log(response);
			setCustomerHtml(response.data);
		})
		.catch(function (error) {
			console.log(error);
		});
}

function liveSearch(data) {
	//console.log(data);
	axios
		.get("/nf/livesearch", {
			params: {
				input: data
			}
		}
		)
		.then(function (response) {
			//console.log(response);
			displayTableHtml(response);
		})
		.catch(function (error) {
			console.log(error);
		});

}

function getCustomersByDate(data) {

	var resp = changeDateformat(data);

	axios
		.get("/nf/getbydate", {
			params: {
				date: resp
			}
		}
		)
		.then(function (response) {
			//console.log(response);
			displayTableHtml(response);
		})
		.catch(function (error) {
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
	document.getElementById("customer-div").innerHTML = '';
	outros = "";

	let data = ''
		+ '<div class="frame-div">'
		+ '<div class="main-div">'

		+ '<!-- Dados Principais-->'
		+ '<div class="col-1-data">'

		+ setNfData(customer)

		+ '      <div class="main-data">'
		+ '          <div class="prontuario-div">'
		+ '              <label for="prontuario">Prontuário Nº 1/</label>'
		+ '              <input type="text" name="prontuario" id="prontuario" value="' + customer.prontuario + '" disabled="disabled">'
		+ '          </div>'
		+ '          <br>'

		+ '          <!-- Exame / Consulta -->'
		+ '          <div class="exam-consul">'
		+ '              <input type="checkbox" name="consulta" id="consulta" ' + setChecked(customer.consulta) + ' disabled="disabled">'
		+ '              <label for="consulta">Consulta</label>'

		+ '              <input type="checkbox" name="exame" id="exame" ' + setChecked(customer.exame) + ' disabled="disabled">'
		+ '              <label for="exame">Exame</label>'
		+ '          </div>'
		+ '          <br>'
		+ '          <!-- Exame / Consulta end-->'

		+ '          <label for="nome">Nome</label>'
		+ '          <input type="text" name="nome" id="nome" placeholder="Nome" value="' + customer.nome.toUpperCase() + '" disabled="disabled">'
		+ '          <br>'

		+ '          <label for="cpf">CPF</label>'
		+ '          <input type="text" name="cpf" id="cpf" onkeyup="validateCpf(this)" maxlength="14"'
		+ '              placeholder="___.___.___-__" value="' + validateCpf(customer.cpf) + '" disabled="disabled">'
		+ '          <br>'

		+ '          <label for="e-mail">E-mail</label>'
		+ '          <input type="text" name="e-mail" id="e-mail" placeholder="exemplo@website.com.br" value="' + customer.email.toLowerCase() + '" disabled="disabled">'
		+ '          <br>'

		+ '          <label for="telefone">Telefone</label>'
		+ '          <input type="text" name="telefone" id="telefone" onkeyup="validateTelefone(this)" maxlength="15"'
		+ '              placeholder="Telefone" value="' + validateTelefone(customer.telefone) + '" disabled="disabled">'
		+ '      </div>'


		+ '      <!-- <div class="forma-pagamento-select">'
		+ '          <label for="obs">Obs.:</label>'
		+ '          <textarea name="obs" id="obs" cols="30" rows="3" disabled="disabled"></textarea>'
		+ '      </div> -->'

		+ '  </div>'
		+ '  <!-- Dados Principais End -->'

		+ '  <div class="col-2-data">'

		+ '      <div class="cep-data">'

		+ '          <div class="div-container">'
		+ '              <div class="cep-container">'
		+ '                  <label for="cep">CEP</label>'
		+ '                  <input type="text" name="cep" id="cep" onkeyup="validateCep(this)" maxlength="9"'
		+ '                      placeholder="_____-___" value="' + validateCep(customer.cep) + '" disabled="disabled">'
		+ '              </div>'
		+ '          </div>'

		+ '          <div class="div-container">'
		+ '              <div class="deeper-container">'
		+ '                  <label for="rua">Rua</label>'
		+ '                  <input type="text" name="rua" id="rua" placeholder="Nome da rua" value="' + customer.rua + '" disabled="disabled">'
		+ '              </div>'
		+ '              <div class="numero-container">'
		+ '                  <label for="numero">Nº</label>'
		+ '                  <input type="text" name="numero" id="numero" placeholder="Número" value="' + customer.numero + '" disabled="disabled">'
		+ '              </div>'
		+ '          </div>'

		+ '          <label for="complemento">Complemento</label>'
		+ '          <input type="text" name="complemento" id="complemento" placeholder="Complemento" value="' + customer.complemento + '" disabled="disabled">'
		+ '          <br>'

		+ '          <label for="bairro">Bairro</label>'
		+ '          <input type="text" name="bairro" id="bairro" placeholder="Bairro" value="' + customer.bairro + '" disabled="disabled">'
		+ '          <br>'

		+ '          <label for="cidade">Cidade</label>'
		+ '          <input type="text" name="cidade" id="cidade" placeholder="Cidade" value="' + customer.cidade + '" disabled="disabled">'
		+ '          <br>'

		+ '          <label for="cidade">Estado</label>'
		+ '          <input type="text" name="estado" id="estado" placeholder="Estado" value="' + customer.estado + '" disabled="disabled">'
		+ '          <br>'

		+ '          <label for="valor">Valor</label>'
		+ '          <div class="div-container">'
		+ '              <label for="valor">R$</label>'
		+ '              <input type="text" name="valor" id="valor" onkeyup="validateValor(this)" maxlength="11"'
		+ '                  placeholder="00,00" value="' + validateValor(customer.valor) + '" disabled="disabled">'
		+ '          </div>'
		+ '          <br>'

		+ '          <div class="prontuario-div">'
		+ '              <label for="forma-pagamento">Forma de Pagamento</label>'
		+ '              <input type="text" name="forma-pagamento" id="forma-pagamento" value="' + customer.formaPagamento + '" disabled="disabled">'
		+ '          </div>'

		+ '          <div id="pagamento-extra">'
		+ isNull(customer.pagamento2)
		+ '          <textarea name="textarea-tag" id="textarea-tag" cols="37" rows="3" disabled="disabled">' + customer.pagamento1 + '</textarea>'
		+ '          </div>'

		+ '      </div>'

		+ '  </div>'

		+ '  <div class="col-3-data">'
		+ '      <!-- Exames -->'
		+ '      <div class="exames">'
		+ '          <h4>Exames</h4>'
		+ '          <br>'

		+ '          <label for="medico">Médico Solicitante</label>'
		+ '          <input name="medico" id="medico" value="' + customer.medico + '" disabled="disabled">'
		+ '          <br><br>'

		+ '          <!-- Checkbox -->'
		+ '          <input type="checkbox" name="Biometria" id="biometria" ' + setCheckedExames(customer.exames, "biometria") + ' disabled="disabled">'
		+ '          <label for="biometria">Biometria</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Campimetria" id="campimetria"' + setCheckedExames(customer.exames, "campimetria") + ' disabled="disabled">'
		+ '          <label for="campimetria">Campimetria</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Capsulotomia" id="capsulotomia"' + setCheckedExames(customer.exames, "capsulotomia") + ' disabled="disabled">'
		+ '          <label for="capsulotomia">Capsulotomia</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Curva Tensional" id="curva-tensional"' + setCheckedExames(customer.exames, "curva-tensional") + ' disabled="disabled">'
		+ '          <label for="curva-tensional">Curva Tensional</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Teste Ortóptico" id="teste-ortoptico"' + setCheckedExames(customer.exames, "teste-ortoptico") + ' disabled="disabled">'
		+ '          <label for="teste-ortoptico">Teste Ortóptico</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Fotocoagulacao à Laser" id="fotocoagulacao-a-laser"' + setCheckedExames(customer.exames, "fotocoagulacao-a-laser") + ' disabled="disabled">'
		+ '          <label for="fotocoagulacao-a-laser">Fotocoagulacao à Laser</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Gonioscopia" id="gonioscopia"' + setCheckedExames(customer.exames, "gonioscopia") + ' disabled="disabled">'
		+ '          <label for="gonioscopia">Gonioscopia</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Mapeamento de Retina" id="mapeamento-de-retina"' + setCheckedExames(customer.exames, "mapeamento-de-retina") + ' disabled="disabled">'
		+ '          <label for="mapeamento-de-retina">Mapeamento de Retina</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Microscopia" id="microscopia"' + setCheckedExames(customer.exames, "microscopia") + ' disabled="disabled">'
		+ '          <label for="microscopia">Microscopia</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="P.A.M." id="pam"' + setCheckedExames(customer.exames, "pam") + ' disabled="disabled">'
		+ '          <label for="pam">P.A.M.</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Paquimetria" id="paquimetria"' + setCheckedExames(customer.exames, "paquimetria") + ' disabled="disabled">'
		+ '          <label for="paquimetria">Paquimetria</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Retinografia" id="retinografia"' + setCheckedExames(customer.exames, "retinografia") + ' disabled="disabled">'
		+ '          <label for="retinografia">Retinografia</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Tonometria" id="tonometria"' + setCheckedExames(customer.exames, "tonometria") + ' disabled="disabled">'
		+ '          <label for="tonometria">Tonometria</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Ceratoscopia" id="ceratoscopia"' + setCheckedExames(customer.exames, "ceratoscopia") + ' disabled="disabled">'
		+ '          <label for="ceratoscopia">Ceratoscopia</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="OCT" id="oct"' + setCheckedExames(customer.exames, "oct") + ' disabled="disabled">'
		+ '          <label for="oct">OCT</label>'
		+ '          <br>'

		+ '          <input type="checkbox" name="Outros" id="outros"' + setCheckedExames(customer.exames, "outros") + ' disabled="disabled">'
		+ '          <label for="outros">Outros</label>'
		+ '          <input type="text" id="outros-input" value="' + outros + '" disabled="disabled">'
		+ '          <br>'
		+ '          <!-- Checkbox end-->'

		+ '      </div>'
		+ '      <!-- Exames end-->'
		+ '  </div>'
		+ '  </div>'
		+ '</div>';

	document.getElementById("customer-div").innerHTML = data;

}

function isNull(data) {

	var resp = data;

	if (data === null) {
		resp = "";
	} else {
		resp = '<input type="text" name="forma-pagamento" id="forma-pagamento1" value="' + data + '" disabled="disabled">'
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
				nfDoneBy: username
			})
			.then(function (response) {
				console.log(response);
				if (response.status === 200) {
					alert("NF salva com sucesso!");
					window.location.reload();
					//setTable();
				}
			})
			.catch(function (error) {
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
		text += ''
			+ '      <!-- Dados NF -->'
			+ '          <div class="dados-nf">'
			+ '            <div class="nf-time">'
			+ '              <h4>Dados NF</h4>'
			+ '                <div><b>Data:</b> ' + timeCreated + '</div>'
			+ '            </div>'
			+ '              <div class="checkbox-div">'
			+ '                  <div class="checkbox-div2">'
			+ '                      <input type="checkbox" name="sim" id="sim">'
			+ '                      <label for="sim">Sim</label>'
			+ '                  </div>'
			+ '                  <button onclick="saveNF(this.id)" id="' + data.id + '">Salvar</button>'
			+ '              </div>'

			+ '              <label for="nota-fiscal">Nota Fiscal Nº</label>'
			+ '              <input type="text" name="nota-fiscal" id="nota-fiscal">'
			+ '          </div>'
			+ '      <!-- Dados NF end-->';

	} else if (data.nfDone === true) {
		text += ''
			+ '      <!-- Dados NF -->'
			+ '          <div class="dados-nf">'
			+ '            <div class="nf-time">'
			+ '              <h4>Dados NF</h4>'
			+ '                <div><b>Data:</b> ' + timeCreated + '</div>'
			+ '            </div>'
			+ '              <div class="checkbox-div">'
			+ '                  <div class="checkbox-div2">'
			+ '                      <input type="checkbox" name="sim" id="sim" checked  disabled="disabled">'
			+ '                      <label for="sim">Sim</label>'
			+ '                  </div>'
			+ '            <div class="nf-time">'
			+ '                <div><b>Nf Data:</b> ' + timeNf + '</div>'
			+ '            </div>'
			+ '              </div>'

			+ '              <label for="nota-fiscal">Nota Fiscal Nº</label>'
			+ '              <input type="text" name="nota-fiscal" id="nota-fiscal" value="' + data.nfNumero + '" disabled="disabled">'
			+ '          </div>'
			+ '      <!-- Dados NF end-->';

	} else {
		alert("Erro de dados do NF")
	}

	return text;

}

function validateCpf(data) {

	let input = data.toString()
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

	let input = data.toString()
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

