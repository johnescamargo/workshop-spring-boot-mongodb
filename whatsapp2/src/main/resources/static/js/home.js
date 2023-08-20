// Variables ******************************************************************************************
const excel_file = document.getElementById("excel_file");
const export_button = document.getElementById("export_button");
let modal = document.getElementById("myModal");

var excel_array = [];
let excel_date_row;
let excel_name;
let excel_phone;
let excel_hour_minute;
let excel_doctor;
let excel_service;
let excel_service_customer;
let messageBase = [];
let messageBaseToBeSentToServer = [];
let arrayOfAllMessages = [];

let newArray = [];

excel_file.addEventListener("change", (event) => {
  if (
    ![
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "application/vnd.ms-excel",
    ].includes(event.target.files[0].type)
  ) {
    document.getElementById("excel_data").innerHTML =
      '<div class="alert alert-danger">Apenas arquivos com extensão .xlsx ou .xls são permitidos</div>';

    excel_file.value = "";

    return false;
  }

  var reader = new FileReader();

  reader.readAsArrayBuffer(event.target.files[0]);

  reader.onload = function () {
    var data = new Uint8Array(reader.result);

    var work_book = XLSX.read(data, { type: "array" });

    var sheet_name = work_book.SheetNames;

    var sheet_data = XLSX.utils.sheet_to_json(work_book.Sheets[sheet_name[0]], {
      header: 1,
    });

    // Filtering Excel sheet
    filterExcel(sheet_data);

    excel_file.value = "";
  };
});

// Filtering Excel sheet
function filterExcel(sheet_data) {
  if (sheet_data.length > 0) {
    var verirify = Boolean(true);

    // Empty array
    excel_array = [];
    // Date
    excel_date_row = convertDate(sheet_data[17][1]);
    excel_doctor = sheet_data[14][2];
    excel_service = sheet_data[12][2];

    // Starting on line 26
    for (var row = 25; row < sheet_data.length; row++) {
      for (var cell = 0; cell < sheet_data[row].length; cell++) {
        if (!sheet_data[row][8] == "") {
          // Hour and Minute
          if (cell == 0) {
            excel_hour_minute = convertHoursMinutes(sheet_data[row][cell]);
          }

          // Name
          if (cell == 2) {
            excel_name = clearName(sheet_data[row][cell]);
          }

          // Serviço
          if (cell == 6) {
            excel_service_customer = sheet_data[row][cell];
          }

          // Mobile Phone
          if (cell == 8) {
            excel_phone = clearPhoneNumber(sheet_data[row][cell]);
          }

          verirify = true;
        } else {
          verirify = false;
        }
      }

      // Save to Array
      if (verirify) {
        saveToArrayExcel();
      }
    }

    // Display data sheet to HTML
    htmlDisplay();
    displayHtmlTemplate();
  }
}

// Getting rid of some random texts in a string
function clearName(value) {
  let name = value.replace("MED - ", "");
  return (name = name.replace("REP - ", ""));
}

// Getting rid of strings and allowing only numbers
function clearPhoneNumber(value) {
  var phone = value;
  phone = phone.replace("-", "");
  phone = phone.replace(" ", "");
  phone = phone.replace("-", "");
  phone = phone.replace("(", "");
  phone = phone.replace(")", "");
  phone = phone.replace(" -", "");
  phone = phone.replace("- ", "");
  phone = phone.replace(" - ", "");

  var size = phone.length;
  if (size === 9) {
    phone = "11" + phone;
  }

  let onlyNumbers = phone.replace(/\D/g, "");
  onlyNumbers = "55" + onlyNumbers;

  return onlyNumbers;
}

// Converting excel text to date
function convertDate(value) {
  var date = new Date(Math.round((value - (25567 + 2)) * 86400 * 1000));
  var converted_date = date.toLocaleString("pt-br", {
    year: "numeric",
    month: "numeric",
    day: "numeric",
  });

  //var dateBR = new Date().toLocaleDateString( 'pt-br', converted_date);

  return converted_date;
}

// Converting hour and minutes from data sheet
function convertHoursMinutes(value) {
  var fractional_day = value - Math.floor(value) + 0.0000001;
  var total_seconds = Math.floor(86400 * fractional_day);

  var seconds = total_seconds % 60;

  total_seconds -= seconds;

  var hours = Math.floor(total_seconds / (60 * 60));
  var minutes = Math.floor(total_seconds / 60) % 60;

  var minutesString = minutes.toString();

  if (minutesString.length <= 1) {
    if (
      minutesString === "1" ||
      minutesString === "2" ||
      minutesString === "3" ||
      minutesString === "4" ||
      minutesString === "5" ||
      minutesString === "6" ||
      minutesString === "7" ||
      minutesString === "8" ||
      minutesString === "9"
    ) {
      minutesString = "0" + minutesString;
    } else {
      minutesString = minutesString + "0";
    }
  }

  return hours + ":" + minutesString;
}

// Saving formatted data to array
function saveToArrayExcel() {
  excel_array.push({
    excel_date_row,
    excel_hour_minute,
    excel_name,
    excel_phone,
    excel_doctor,
    excel_service,
    excel_service_customer,
  });
}

// Display Data int html
function htmlDisplay() {
  checkMessageType(); // Type of service
  arrayOfAllMessages = [];

  var table_output =
    '<div><button onclick="sendJsonAllMessages()" id="btn-all" class="btn btn-success btn-sm">Enviar para todos</button></div><br>' +
    "<div>" +
    '<div class="form-group">' +
    '<label for="textarea1">Texto base</label>' +
    '<textarea onkeyup="updateTemplateText(this)" class="form-control" id="textarea1" rows="4">' +
    messageBase.join("\n") +
    "</textarea>" +
    "</div>" +
    "</div>" +
    "<!-- The Modal -->" +
    '<div id="myModal" class="modal">' +
    "<!-- Modal content -->" +
    '<div class="modal-content">' +
    '<div class="form-save">' +
    '<div class="container2">' +
    '<label for="waiting"><b>Enviando mensagens, aguarde </b></label>' +
    '<div class="col-3">' +
    '<div class="snippet" data-title="dot-typing">' +
    '<div class="stage">' +
    '<div class="dot-typing"></div>' +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";

  table_output +=
    '<table id="excel_data_1" class="table table-striped table-bordered">';

  for (var i = 0; i < excel_array.length; i++) {
    table_output += '<tr id="tr-' + i + '">';

    table_output +=
      '<th class="class-date" id="date-' +
      i +
      '">' +
      excel_array[i].excel_date_row +
      "</th>";
    table_output +=
      '<th class="class-doctor"><input id="doctor-' +
      i +
      '" type="text" value="' +
      excel_array[i].excel_doctor +
      '"></th>';
    table_output +=
      '<th class="class-service"><input id="service-' +
      i +
      '" type="text" value="' +
      excel_array[i].excel_service_customer +
      '"></th>';
    table_output +=
      '<th class="class-time"><input id="time-' +
      i +
      '" type="text" value="' +
      excel_array[i].excel_hour_minute +
      '" maxlength="5"></th>';
    table_output +=
      '<th class="class-name"><input id="name-' +
      i +
      '" type="text" value="' +
      excel_array[i].excel_name +
      '"></th>';
    table_output +=
      '<th class="class-phone"><input  id="phone-' +
      i +
      '" type="text" value="' +
      excel_array[i].excel_phone +
      '"></th>';
    table_output +=
      '<th class="class-button"><button onclick="sendJsonData(this.id)" id="btn-' +
      i +
      ' "class="btn btn-success btn-sm">Enviar</button></th>';

    table_output += "</tr>";

    arrayOfAllMessages.push({
      id: "tr-" + i,
      date: excel_array[i].excel_date_row,
      time: excel_array[i].excel_hour_minute,
      name: excel_array[i].excel_name,
      phone: excel_array[i].excel_phone,
      doctor: excel_doctor,
      service: excel_service,
      messages: messageBaseToBeSentToServer,
    });

    // This i++ is here to fix duplication on the excel table
    i++;
  }

  table_output += "</table>";

  document.getElementById("excel_data").innerHTML = table_output;
}

function displayHtmlTemplate() {
  var test =
    '<div class="message-dialogue">' +
    '<div class="template-model"><b>Modelo</b></div>' +
    '<div class="my-message">' +
    '<p>Olá <b>' +
    excel_array[0].excel_name +
    "</b>, espero que esteja bem. 😊</p>" +
    "<p>Somos da Clínica de Olhos <b>IMAV</b> e estamos entrando em contato " +
    "para confirmar seu exame e/ou consulta conosco.</p>" +
    '<p>Serviço: <b id="servico-temp">' +
    excel_array[0].excel_service_customer +
    "</b></p>" +
    '<p>Data: <b id="data-temp">' +
    excel_array[0].excel_date_row +
    "</b></p>" +
    "<p>Hora: <b>" +
    excel_array[0].excel_hour_minute +
    "</b></p>" +
    '<p>Medico: <b id="medico-temp">' +
    excel_doctor +
    "</b></p>" +
    "<p></p>" +
    '<p><b id="texto-temp">' +
    messageBase.join("\n") +
    "</b></p><br>" +
    "<p>Podemos confirmar sua presença?</p>" +
    "<p>Aguardamos sua confirmação.</p>" +
    "<p>Obrigado</p>" +
    "<br>" +
    '<p id="imav-temp">IMAV - Instituto de Medicina Avançada da Visão</p>' +
    '<div class="time" id="time">' +
    '<div id="time-conversation">01/01/2023 11:03</div>' +
    "</div>" +
    "</div>" +
    '<div class="button-dialogue">' +
    '<div class="my-message-button">SIM</div>' +
    '<div class="my-message-button">REMARCAR</div>' +
    "</div>" +
    "</div>";

  document.getElementById("showcase").innerHTML = test;
}

// Displaying data on HTML
function html_table_to_excel(type) {
  var data = document.getElementById("excel_data_1");

  var file = XLSX.utils.table_to_book(data, { sheet: "sheet1" });
  XLSX.write(file, { bookType: type, bookSST: true, type: "base64" });
  XLSX.writeFile(file, "file." + type);
}

export_button.addEventListener("click", () => {
  html_table_to_excel("xlsx");
});

// Send data after clicking on individual button
function sendJsonData(data) {
  modal.style.display = "block";

  saveArrayOfMessage();

  var id = data.slice(4);

  var date = document.querySelector('#date-' + id).innerText;
  var time = document.querySelector("#time-" + id).value;
  var name = document.querySelector("#name-" + id).value;
  var phone = document.querySelector("#phone-" + id).value;
  var doctor = document.querySelector("#doctor-" + id).value;
  var service = document.querySelector("#service-" + id).value;

  axios
    .post("/message/send", {
      date: date,
      time: time,
      name: name,
      phone: phone,
      doctor: doctor,
      service: service,
      messages: messageBaseToBeSentToServer,
    })
    .then((response) => {
      sleep(1500);
      modal.style.display = "none";
      if (response.data === true) {
        let tr = document.querySelector("#tr-" + id);
        tr.style.backgroundColor = "rgba(58, 159, 97, 0.51)";
      } else if (response.data === false) {
        let tr = document.querySelector("#tr-" + id);
        tr.style.backgroundColor = "rgba(159, 58, 58, 0.51)";
      }
    })
    .catch((error) => {
      if (error.response === false) {
        //response status is an error code
        console.log(error.response);
        alert("Sem resposta do servidor local 1.");
        modal.style.display = "none";
      } else if (error.request) {
        //response not received though the request was sent
        console.log(error.request);
        alert("Sem resposta do servidor local 2.");
        modal.style.display = "none";
      } else {
        //an error occurred when setting up the request
        console.log(error.message);
        alert("Sem resposta do servidor local 3.");
        modal.style.display = "none";
      }
    });
}

// Send data after button clicked (sending all)
function sendJsonAllMessages() {
  modal.style.display = "block";

  saveArrayOfMessage();
  updateArrayOfAllMessages();

  axios
    .post("/message/sendAll", newArray) // old = arrayOfAllMessages
    .then((response) => {
      sleep(2000);
      modal.style.display = "none";
      setResponseColor(response.data);
      //console.log(response.data);
    })
    .catch((error) => {
      if (error.response) {
        //response status is an error code
        console.log(error.response.status);
        alert("Sem resposta do servidor local 1.");
        modal.style.display = "none";
      } else if (error.request) {
        //response not received though the request was sent
        console.log(error.request);
        alert("Sem resposta do servidor local 2.");
        modal.style.display = "none";
      } else {
        //an error occurred when setting up the request
        console.log(error.message);
        alert("Sem resposta do servidor local 3.");
        modal.style.display = "none";
      }
    });

  arrayOfAllMessages = [];
}

// Checking the type of the service and setting it
function checkMessageType() {
  switch (excel_service) {
    case "CONSULTA":
      messageBase = messageConsulta;
      break;
    case "CONSULTA + EXAMES":
      messageBase = messageConsulta;
      break;
    case "EXAME - CAMPIMETRIA":
      messageBase = messageConsulta;
      break;
    case "EXAME - CERATOSCOPIA":
      messageBase = messageConsulta;
      break;
    case "EXAME - CURVA TENSIONAL":
      messageBase = messageConsulta;
      break;
    case "EXAME - MICROSCOPIA":
      messageBase = messageConsulta;
      break;
    case "EXAME - RETINOGRAFIA":
      messageBase = messageConsulta;
      break;
    case "EXAMES":
      messageBase = messageMapeamento;
      break;
    case "EXAMES - PAQUIMETRIA":
      messageBase = messageConsulta;
      break;
    case "EXAMES-ULTRASSONOGRAFIA":
      messageBase = messageConsulta;
      break;
    case "EXAME-TESTE PROV. GLAUCOMA":
      messageBase = messageConsulta;
      break;
    case "PROCEDIMENTOS":
      messageBase = messageConsulta;
      break;
    case "RESERVADO PARA AMPLAVISÃO":
      messageBase = messageConsulta;
      break;
    case "RESERVADO PARA OCT":
      messageBase = messageConsulta;
      break;
    case "RESERVADO PARA TESTE PROVOCATIVO":
      messageBase = messageConsulta;
      break;
    case "RETINOGRAFIA":
      messageBase = messageConsulta;
      break;
    case "TESTE DE ADAPTACAO":
      messageBase = messageConsulta;
      break;
    default:
      messageBase = messageConsulta;
  }
}

function saveArrayOfMessage() {
  let textArea = document.querySelector("#textarea1");
  messageBaseToBeSentToServer = [];
  messageBaseToBeSentToServer = textArea.value.split("\n");
}

function setResponseColor(response) {
  for (var i = 0; i < response.length; i++) {
    if (response[i].sent === true) {
      let tr = document.querySelector("#" + response[i].id);
      tr.style.backgroundColor = "rgba(58, 159, 97, 0.51)";
    } else {
      let tr = document.querySelector("#" + response[i].id);
      tr.style.backgroundColor = "rgba(159, 58, 58, 0.51)";
    }
  }
}

// Sleep method
function sleep(milliseconds) {
  const date = Date.now();
  let currentDate = null;
  do {
    currentDate = Date.now();
  } while (currentDate - date < milliseconds);
}

// Get everything new from DOM
function updateArrayOfAllMessages() {

  newArray = [];

  let textArea = document.getElementById("textarea1");
  messageBaseToBeSentToServer = [];
  messageBaseToBeSentToServer = textArea.value.split("\n");

  let count = 0;
  for (var i = 0; i < arrayOfAllMessages.length; i++) {
    var id = count;

    var date = document.querySelector('#date-' + id).innerText;
    var time = document.querySelector("#time-" + id).value;
    var name = document.querySelector("#name-" + id).value;
    var phone = document.querySelector("#phone-" + id).value;
    var doctor = document.querySelector("#doctor-" + id).value;
    var service = document.querySelector("#service-" + id).value;

    newArray.push({
      id: "tr-" + id,
      date: date,
      time: time,
      name: name,
      phone: phone,
      doctor: doctor,
      service: service,
      messages: messageBaseToBeSentToServer,
    });

    count = count + 2;
  }
  console.log(newArray);
}

// Message Base to be sent to server *********************************************************
const messageConsulta = ["Chegar com pelo menos 10 minutos de antecedência."];

const messageExame = ["Chegar com pelo menos 10 minutos de antecedência."];

const messageMapeamento = ["Chegar com pelo menos 10 minutos de antecedência."];

function updateTemplateText(e) {
  document.getElementById("texto-temp").innerHTML = "";
  document.getElementById("texto-temp").innerHTML = e.value;  
}
