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
const pagamentoExtra = document.getElementById("pagamento-extra");
const textareaTag = document.getElementById("textarea-tag");

// it's only for testing
const user = 'johnes';

var prontuarioBoolean = false;
var consultaExameBoolean = false;
var nomeBoolean = false;
var cpfBoolean = false;
var emailBoolean = true;
var telefoneBoolean = false;
var cepBoolean = false;
var numeroBoolean = false;
var ruaBoolean = false;
var bairroBoolean = false;
var cidadeBoolean = false;
var estadoBoolean = false;
var valorBoolean = false;
var formaPagamentoBoolean = false;
var creditoPagamentoBoolean = true;
var medicoBoolean = false;
var outrosBoolean = false;
var cartaoCredit = false;

let dadosPagamento = [];
var examesSelecionados = [];
const date = new Date();

// Add cep number after urlCepBeginning
const urlCepBeginning = "https://viacep.com.br/ws/";
const urlCepEnd = "/json/";

function getData() {
	// Declare variables inside function
	let prontuario = document.getElementById("prontuario").value;

	let consulta = document.getElementById("consulta").checked;
	let exame = document.getElementById("exame").checked;

	let nome = document.getElementById("nome").value;
	let cpf = document.getElementById("cpf").value;
	let cep = document.getElementById("cep").value;
	let numero = document.getElementById("numero").value;
	let email = document.getElementById("e-mail").value;
	let valor = document.getElementById("valor").value;
	let formaPagamento = document.getElementById("forma-pagamento").value;
	let telefone = document.getElementById("telefone").value;
	let medico = document.getElementById("medico").value;

	let rua = document.getElementById("rua").value;
	let bairro = document.getElementById("bairro").value;
	let cidade = document.getElementById("cidade").value;
	let estado = document.getElementById("estado").value;

	let outros = document.getElementById("outros");
	var outrosInput = document.getElementById("outros-input").value;

	prontuario
		? ((document.getElementById("prontuario").style.border = "solid green"),
			(prontuarioBoolean = true))
		: ((document.getElementById("prontuario").style.border = "solid red"),
			(prontuarioBoolean = false));

	// verify if consulta or exame are selected
	if (!consulta && !exame) {
		document.getElementById("consulta").style.outlineColor = "red";
		document.getElementById("consulta").style.outlineStyle = "auto";
		document.getElementById("exame").style.outlineColor = "red";
		document.getElementById("exame").style.outlineStyle = "auto";
		consultaExameBoolean = false;
	} else {
		document.getElementById("consulta").style.outlineColor = "dimgrey";
		document.getElementById("consulta").style.outlineStyle = "auto";
		document.getElementById("exame").style.outlineColor = "dimgrey";
		document.getElementById("exame").style.outlineStyle = "auto";
		consultaExameBoolean = true;
	}

	nome
		? ((document.getElementById("nome").style.border = "solid green"),
			(nomeBoolean = true))
		: ((document.getElementById("nome").style.border = "solid red"),
			(nomeBoolean = false));

	// CPF
	if (cpf.length === 14 && isNumeric(cpf) && validaCpf(cpf)) {
		document.getElementById("cpf").style.border = "solid green";
		cpfBoolean = true;
	} else {
		document.getElementById("cpf").style.border = "solid red";
		cpfBoolean = false;
	}

	if (email.length > 0) {
		if (validateEmail(email)) {
			document.getElementById("e-mail").style.border = "solid green";
			emailBoolean = true;
		} else {
			document.getElementById("e-mail").style.border = "solid red";
			emailBoolean = false;
		}
	} else {
		emailBoolean = true;
	}

	cep
		? ((document.getElementById("cep").style.border = "solid green"),
			(cepBoolean = true))
		: ((document.getElementById("cep").style.border = "solid red"),
			(cepBoolean = false));

	numero
		? ((document.getElementById("numero").style.border = "solid green"),
			(numeroBoolean = true))
		: ((document.getElementById("numero").style.border = "solid red"),
			(numeroBoolean = false));

	// Valor
	if (isNumeric(valor) && valor.length > 0) {
		document.getElementById("valor").style.border = "solid green";
		valorBoolean = true;
	} else {
		document.getElementById("valor").style.border = "solid red";
		valorBoolean = false;
	}

	formaPagamento
		? ((document.getElementById("forma-pagamento").style.border =
			"solid green"),
			(formaPagamentoBoolean = true))
		: ((document.getElementById("forma-pagamento").style.border = "solid red"),
			(formaPagamentoBoolean = false));

	if (formaPagamento === "Cartão Crédito") {
		cartaoCredit = true;

		let cartaoCred = document.getElementById("credito-pagamento").value;
		cartaoCred
			? ((document.getElementById("credito-pagamento").style.border =
				"solid green"),
				(creditoPagamentoBoolean = true))
			: ((document.getElementById("credito-pagamento").style.border =
				"solid red"),
				(creditoPagamentoBoolean = false));
	} else {
		creditoPagamentoBoolean = true;
		cartaoCredit = false;
	}

	telefone
		? ((document.getElementById("telefone").style.border = "solid green"),
			(telefoneBoolean = true))
		: ((document.getElementById("telefone").style.border = "solid red"),
			(telefoneBoolean = false));

	medico
		? ((document.getElementById("medico").style.border = "solid green"),
			(medicoBoolean = true))
		: ((document.getElementById("medico").style.border = "solid red"),
			(medicoBoolean = false));

	rua
		? ((document.getElementById("rua").style.border = "solid green"),
			(ruaBoolean = true))
		: ((document.getElementById("rua").style.border = "solid red"),
			(ruaBoolean = false));

	bairro
		? ((document.getElementById("bairro").style.border = "solid green"),
			(bairroBoolean = true))
		: ((document.getElementById("bairro").style.border = "solid red"),
			(bairroBoolean = false));

	cidade
		? ((document.getElementById("cidade").style.border = "solid green"),
			(cidadeBoolean = true))
		: ((document.getElementById("cidade").style.border = "solid red"),
			(cidadeBoolean = false));

	estado
		? ((document.getElementById("estado").style.border = "solid green"),
			(estadoBoolean = true))
		: ((document.getElementById("estado").style.border = "solid red"),
			(estadoBoolean = false));

	if (outros.checked && outrosInput) {
		document.getElementById("outros-input").style.border = "solid green";
		outrosBoolean = true;
	} else if (outros.checked && !outrosInput) {
		document.getElementById("outros-input").style.border = "solid red";
		outrosBoolean = false;
	} else if (!outros.checked && outrosInput) {
		document.getElementById("outros").checked = "on";
		outrosBoolean = true;
	} else {
		document.getElementById("outros-input").style.border = "2px solid dimgrey";
		document.getElementById("outros-input").value = "";
		outrosBoolean = true;
	}

	if (
		prontuarioBoolean &&
		consultaExameBoolean &&
		nomeBoolean &&
		cpfBoolean &&
		emailBoolean &&
		telefoneBoolean &&
		cepBoolean &&
		numeroBoolean &&
		bairroBoolean &&
		cidadeBoolean &&
		estadoBoolean &&
		valorBoolean &&
		formaPagamentoBoolean &&
		creditoPagamentoBoolean &&
		medicoBoolean &&
		outrosBoolean
	) {
		getExamesSelecionados();
		getDadosPagamento();
		sendData();
	}
}

// Send JSON
function sendData() {
	axios
		.post("/nf/save", {
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
			nfNumero: "",
			nfDone: false,
			userName: user,
			nfDoneBy: "",
			pagamento1: dadosPagamento[0],
			pagamento2: dadosPagamento[1],
			exames: examesSelecionados,
		})
		.then(function (response) {
			console.log(response);
			if (response.status === 200) {
				alert("Documento salvo com sucesso!");
				window.location.reload();
			}
		})
		.catch(function (error) {
			console.log(error);
			alert(error);
		});
}

function validateEmail(mail) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
		return true;
	}
	return false;
}

function validateCpf(input) {
	var valueInput = input.value;
	var inputLength = input.value.length;

	switch (inputLength) {
		case 3:
			valueInput = valueInput + ".";
			break;

		case 7:
			valueInput = valueInput + ".";
			break;

		case 11:
			valueInput = valueInput + "-";
			break;

		default:
	}

	document.getElementById("cpf").value = valueInput;
}

function validateTelefone(input) {
	var valueInput = input.value;
	var inputLength = input.value.length;

	switch (inputLength) {
		case 1:
			valueInput = "(" + valueInput;
			break;

		case 3:
			valueInput = valueInput + ")";
			break;

		default:
	}

	document.getElementById("telefone").value = valueInput;
}

function validateValor(input) {
	var valueInput = input.value;
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

	document.getElementById("valor").value = valueInput;
	return valueInput;
}

function validateCep(input) {
	var valueInput = input.value;
	var inputLength = input.value.length;

	switch (inputLength) {
		case 5:
			valueInput = valueInput.replace(/-/g, "");
			valueInput = valueInput + "-";
			break;

		case 6:
			valueInput = valueInput.replace(/-/g, "");
			valueInput = valueInput;
			break;

		case 9:
			getCEP(valueInput);
			break;

		default:
	}

	document.getElementById("cep").value = valueInput;
}

function getDadosPagamento() {

	dadosPagamento = [];

	let textareaTag = document.getElementById("textarea-tag").value;
	dadosPagamento.push(textareaTag);

	//get value from select tag cartão de crédito - número de parcelas
	if (cartaoCredit) {
		let cartaoCredValue = document.getElementById("credito-pagamento").value;
		dadosPagamento.push(cartaoCredValue);
	}
}

// Get CEP via api
function getCEP(data) {
	var cep = data.replace(/-/g, "");

	axios
		.get(urlCepBeginning + cep + urlCepEnd)
		.then(function (response) {
			bairro.value = response.data.bairro;
			rua.value = response.data.logradouro;
			cidade.value = response.data.localidade;
			estado.value = response.data.uf;
		})
		.catch(function (error) {
			console.log(error);
		});
}

function getExamesSelecionados() {
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

// Check if variable is numeric
function isNumeric(valueInput) {
	valueInput = valueInput.replaceAll(",", "");
	valueInput = valueInput.replaceAll(".", "");
	valueInput = valueInput.replaceAll(" ", "");
	valueInput = valueInput.replaceAll("-", "");

	return !isNaN(valueInput);
}

function getDate() {
	var dia = date.getDate();
	var mes = date.getMonth();
	var ano = date.getFullYear();
	var horas = date.getHours();
	var minutos = date.getMinutes();
	var segundos = date.getSeconds();
}

formaPagamento.addEventListener("change", (event) => {
	let data = event.target.value;
	pagamentoExtra.innerHTML = "";

	if (data === "Boleto") {
		pagamentoExtra.innerHTML =
			'<div class="textarea-div"><textarea name="textarea-tag" id="textarea-tag" cols="37" rows="4"></textarea></div>';
	}

	if (data === "Cartão Débito") {
		pagamentoExtra.innerHTML =
			'<div class="textarea-div"><textarea name="textarea-tag" id="textarea-tag" cols="37" rows="4"></textarea></div>';
	}

	if (data === "Cartão Crédito") {
		pagamentoExtra.innerHTML =
			"<br>" +
			'<div class="prontuario-div">' +
			'<label for="credito-pagamento">Número de Parcelas</label>' +
			'<select name="credito-pagamento" id="credito-pagamento" required>' +
			"<option value selected disabled>Selecione *</option>" +
			'<option value="1x">1x</option>' +
			'<option value="2x">2x</option>' +
			'<option value="3x">3x</option>' +
			'<option value="4x">4x</option>' +
			'<option value="5x">5x</option>' +
			"</select>" +
			"</div>" +
			'<textarea name="textarea-tag" id="textarea-tag" cols="37" rows="4"></textarea>';
		document.getElementById("textarea-tag").style.height = "42px";
	}

	if (data === "Cheque") {
		pagamentoExtra.innerHTML =
			'<div class="textarea-div"><textarea name="textarea-tag" id="textarea-tag" cols="37" rows="4"></textarea></div>';
	}

	if (data === "Depósito") {
		pagamentoExtra.innerHTML =
			'<div class="textarea-div"><textarea name="textarea-tag" id="textarea-tag" cols="37" rows="4"></textarea></div>';
	}

	if (data === "Dinheiro") {
		pagamentoExtra.innerHTML =
			'<div class="textarea-div"><textarea name="textarea-tag" id="textarea-tag" cols="37" rows="4"></textarea></div>';
	}

	if (data === "Pix") {
		pagamentoExtra.innerHTML =
			'<div class="textarea-div"><textarea name="textarea-tag" id="textarea-tag" cols="37" rows="4"></textarea></div>';
	}
});

function validaCpf(valueInput) {

	var strCPF = "";
	strCPF = valueInput.replaceAll(",", "");
	strCPF = strCPF.replaceAll(".", "");
	strCPF = strCPF.replaceAll(" ", "");
	strCPF = strCPF.replaceAll("-", "");

	var Soma;
	var Resto;
	Soma = 0;
	if (strCPF == "00000000000") return false;

	for (i = 1; i <= 9; i++) Soma = Soma + parseInt(strCPF.substring(i - 1, i)) * (11 - i);
	Resto = (Soma * 10) % 11;

	if ((Resto == 10) || (Resto == 11)) Resto = 0;
	if (Resto != parseInt(strCPF.substring(9, 10))) return false;

	Soma = 0;
	for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i - 1, i)) * (12 - i);
	Resto = (Soma * 10) % 11;

	if ((Resto == 10) || (Resto == 11)) Resto = 0;
	if (Resto != parseInt(strCPF.substring(10, 11))) return false;
	return true;
}