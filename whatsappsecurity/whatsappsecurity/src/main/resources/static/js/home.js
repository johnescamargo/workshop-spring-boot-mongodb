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
let messageBase = [];
let messageBaseToBeSentToServer = [];
let arrayOfAllMessages = [];
//*****************************************************************************************************

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
    excel_date_row = convertDate(sheet_data[2][10]);
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

  return onlyNumbers;
}

// Converting excel text to date
function convertDate(value) {
  var date = new Date(Math.round((value - (25567 + 1)) * 86400 * 1000));
  var converted_date = date.toLocaleString([], {
    year: "numeric",
    month: "numeric",
    day: "numeric",
  });
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
  });
}

// Display Data int html
function htmlDisplay() {
 
  checkMessageType(); // Type of service

  var table_output =
    '<div><button onclick="sendJsonAllMessages()" id="btn-all" class="btn btn-success btn-sm">Enviar para todos</button></div><br>' +
    "<div>" +
    '<div class="row">' +
    '<div class="col">' +
    '<label for="input1">Doutor/a</label>' +
    '<input id="doctor" type="text" class="form-control" value="' +
    excel_doctor +
    '" >' +
    "</div>" +
    '<div class="col">' +
    '<label for="input2">Serviço</label>' +
    '<input id="doctor" type="text" class="form-control" value="' +
    excel_service +
    '" >' +
    "</div>" +
    "</div>" +
    '<div class="form-group">' +
    '<label for="exampleFormControlTextarea1">Texto base</label>' +
    '<textarea class="form-control" id="textarea" rows="8" >' +
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
      "<th id='date-" + i + "'>" + excel_array[i].excel_date_row + "</th>";
    table_output +=
      "<th id='time-" + i + "'>" + excel_array[i].excel_hour_minute + "</th>";
    table_output +=
      "<th id='name-" + i + "'>" + excel_array[i].excel_name + "</th>";
    table_output +=
      //"<th id='phone-" + i + "'>" + excel_array[i].excel_phone + "</th>";
      "<th><input id='phone-" +
      i +
      "' type='text' value='" +
      excel_array[i].excel_phone +
      "'></input></th>";
    table_output +=
      "<th><button onclick='sendJsonData(this.id)' id='btn-" +
      i +
      "'class='btn btn-success btn-sm'>Enviar</button></th>";

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

// Send data after button clicked individual
function sendJsonData(value) {
  modal.style.display = "block";

  saveArrayOfMessage();

  var id = value.slice(4);
  var date = document.getElementById("date-" + id).innerText;
  var time = document.getElementById("time-" + id).innerText;
  var name = document.getElementById("name-" + id).innerText;
  var phone = document.getElementById("phone-" + id).value;

  axios
    .post("/message/send", {
      date: date,
      time: time,
      name: name,
      phone: phone,
      doctor: excel_doctor,
      service: excel_service,
      messages: messageBaseToBeSentToServer,
    })
    .then((response) => {
      sleep(1500);
      modal.style.display = "none";
      if (response.data === true) {
        let tr = document.getElementById("tr-" + id);
        tr.style.backgroundColor = "rgba(58, 159, 97, 0.51)";
      } else if (response.data === false) {
        let tr = document.getElementById("tr-" + id);
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
    .post("/message/sendAll", arrayOfAllMessages)
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
      messageBase = messageMapeamento;
      break;
    case "EXAME - CAMPIMETRIA":
      messageBase = messageExame;
      break;
    case "EXAME - CERATOSCOPIA":
      messageBase = [];
      break;
    case "EXAME - CURVA TENSIONAL":
      messageBase = messageMapeamento;
      break;
    case "EXAME - MICROSCOPIA":
      messageBase = [];
      break;
    case "EXAME - RETINOGRAFIA":
      messageBase = [];
      break;
    case "EXAMES":
      messageBase = messageMapeamento;
      break;
    case "EXAMES - PAQUIMETRIA":
      messageBase = [];
      break;
    case "EXAMES-ULTRASSONOGRAFIA":
      messageBase = [];
      break;
    case "EXAME-TESTE PROV. GLAUCOMA":
      messageBase = [];
      break;
    case "PROCEDIMENTOS":
      messageBase = [];
      break;
    case "RESERVADO PARA AMPLAVISÃO":
      messageBase = [];
      break;
    case "RESERVADO PARA OCT":
      messageBase = [];
      break;
    case "RESERVADO PARA TESTE PROVOCATIVO":
      messageBase = [];
      break;
    case "RETINOGRAFIA":
      messageBase = [];
      break;
    case "TESTE DE ADAPTACAO":
      messageBase = [];
      break;
    default:
      alert("Tipo de serviço não registrado.");
      messageBase = [];
  }
}

function saveArrayOfMessage() {
  let textArea = document.getElementById("textarea");
  messageBaseToBeSentToServer = [];
  messageBaseToBeSentToServer = textArea.value.split("\n");
}


function setResponseColor(response) {
  for (var i = 0; i < response.length; i++) {
    if (response[i].sent === true) {
      let tr = document.getElementById(response[i].id);
      tr.style.backgroundColor = "rgba(58, 159, 97, 0.51)";
    } else {
      let tr = document.getElementById(response[i].id);
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

//
function updateArrayOfAllMessages() {
  for (var i = 0; i < arrayOfAllMessages.length; i++) {
    arrayOfAllMessages[i].messages = [];
    arrayOfAllMessages[i].messages = messageBaseToBeSentToServer;
  }
}

// Message Base to be sent to server *********************************************************
const messageConsulta = [
  "Olá",
  "Somos da clínica de olhos IMAV e estamos entrando em contato para confirmar sua consulta.",
  "Podemos confirmar sua presença?",
  "Aguardamos sua confirmação.",
  "Obrigado.",
];

const messageExame = [
  "Olá",
  "Somos da clínica de olhos IMAV e estamos entrando em contato para confirmar seu exame.",
  "Podemos confirmar sua presença?",
  "Aguardamos sua confirmação.",
  "Obrigado.",
];

const messageMapeamento = [
  "Olá",
  "Somos da clínica de olhos IMAV e estamos entrando em contato para confirmar seu exame.",
  "Este exame exige dilatação das pupilas. Não venha dirigindo, pois você não conseguirá retomar as atividades normais por longas horas do dia devido a este procedimento.",
  "Podemos confirmar sua presença?",
  "Aguardamos sua confirmação.",
  "Obrigado.",
];

