import Websocket from "./websocket.js";
const websocket = new Websocket();

var arrayOfTime = [];

var domingoComeco = document.getElementById("domingo-comeco");
var domingoFim = document.getElementById("domingo-fim");
var domingoOff = document.getElementById("domingo-off");

var segundaComeco = document.getElementById("segunda-comeco");
var segundaFim = document.getElementById("segunda-fim");
var segundaOff = document.getElementById("segunda-off");

var tercaComeco = document.getElementById("terca-comeco");
var tercaFim = document.getElementById("terca-fim");
var tercaOff = document.getElementById("terca-off");

var quartaComeco = document.getElementById("quarta-comeco");
var quartaFim = document.getElementById("quarta-fim");
var quartaOff = document.getElementById("quarta-off");

var quintaComeco = document.getElementById("quinta-comeco");
var quintaFim = document.getElementById("quinta-fim");
var quintaOff = document.getElementById("quinta-off");

var sextaComeco = document.getElementById("sexta-comeco");
var sextaFim = document.getElementById("sexta-fim");
var sextaOff = document.getElementById("sexta-off");

var sabadoComeco = document.getElementById("sabado-comeco");
var sabadoFim = document.getElementById("sabado-fim");
var sabadoOff = document.getElementById("sabado-off");

let passwordText = document.getElementById("psw");
let passwordText2 = document.getElementById("psw-repeat");
let usernameText = document.getElementById("nickname");
let emailText = document.getElementById("email");
let userRole = document.getElementById("user-role");

function onloadInit() {
  loadUsers();
  loadWantsToTalk();
  loadWeekTime();
  websocket.connect;
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

passwordText2.addEventListener("input", function () {
  if (this.value === passwordText.value) {
    console.log("Ok");
  }
  //border-color: blue;
  //border-style: solid;
});

function saveUser() {
  let pswLength = passwordText.value.length;
  let psw2Length = passwordText2.value.length;
  let emailLength = emailText.value.length;
  let usernameLength = usernameText.value.length;
  let userRoleResp = userRole.value;

  if (
    userRoleResp &&
    passwordText.value === passwordText2.value &&
    pswLength > 0 &&
    psw2Length > 0 &&
    emailLength > 0 &&
    usernameLength > 0
  ) {
    axios
      .post("/user/registration", {
        username: usernameText.value,
        email: emailText.value,
        password: passwordText.value,
        matchingPassword: passwordText2.value,
        role: userRoleResp,
      })
      .then(function (response) {
        if (response.status === 201) {
          alert("Usuário criado");
          loadUsers();
        } else {
          alert("Usuário não criado");
        }
      });
  }
}

function loadUsers() {
  axios({
    method: "get",
    url: "/user/users",
  }).then(function (response) {
    if (response.status === 200) {
      showUsers(response.data);
    } else if (response.status === 404) {
      console.log("404 - Pacientes não encontrados...");
    } else {
      console.log(response);
    }
  });
}

function showPassword(data) {
  let idResp = data.substring(4);
  var data = $("#" + idResp + " :input");
  console.log(data);

  var senha = document.getElementById("senha-" + idResp);

  if (senha.type === "password") {
    senha.type = "text";
  } else {
    senha.type = "password";
  }
}

function buttonDelete(clickedId) {
  var username = clickedId.substring(7);

  axios
    .get("/user/delete", {
      params: {
        username: username,
      },
    })
    .then(function (response) {
      if (response.status === 200) {
        alert("Usuário excluído...");
        loadUsers();
      } else if (response.status === 404) {
        alert("404 - Usuário não encontrado...");
      } else {
        console.log(response);
      }
    });
}

function buttonUpdate(clickedId) {
  let idResp = clickedId.substring(6);
  var data = $("#" + idResp + " :input");
  var newPassword = data[0].value;

  axios
    .post("/user/newpassword-from-admin", {
      username: idResp,
      password: newPassword,
    })
    .then(function (response) {
      if (response.status === 200) {
        alert("Senha salva com sucesso...");
        loadUsers();
      } else if (response.status === 404) {
        alert("404 - Usuário não encontrado...");
      } else {
        console.log(response);
      }
    });
}

function showUsers(data) {
  $("#users").html("");

  var table =
    '<table class="table" id="table">' +
    "<thead>" +
    "<tr>" +
    "<th>Nome</th>" +
    "<th>Email</th>" +
    "<th>Nova senha</th>" +
    "<th></th>" +
    "<th></th>" +
    "</tr>" +
    "</thead>" +
    "<tbody>";

  for (var i = 0; i < data.length; i++) {
    if (data[i].username !== "admin") {
      table =
        table +
        '<tr class="tr" id="' +
        data[i].username +
        '" >' +
        '<td class="nome" id="name-' +
        data[i].username +
        '">' +
        data[i].username +
        "</td>" +
        '<td id="email">' +
        data[i].email +
        "</td>" +
        '<td id="senha">' +
        '<input class="senha-input" style="margin:0;" id="senha-' +
        data[i].username +
        '" type="password" placeholder="***********">' +
        '<img class="img-password" id="img-' +
        data[i].username +
        '" src="/img/eye_closed.png" onclick="showPassword(this.id)">' +
        "</td>" +
        '<td id="nome">' +
        '<button class="button-update" id="btnUp-' +
        data[i].username +
        '" onclick="buttonUpdate(this.id)">Atualizar</button>' +
        "</td>" +
        '<td class="nome">' +
        '<button type="submit" class="button-delete" id="btnDel-' +
        data[i].username +
        '" onclick="buttonDelete(this.id)">Excluir</button>' +
        "</td>" +
        "</tr>";
    } else {
      table =
        table +
        '<tr class="tr" id="' +
        data[i].username +
        '" >' +
        '<td class="nome" id="name-' +
        data[i].username +
        '">' +
        data[i].username +
        "</td>" +
        '<td id="email">' +
        data[i].email +
        "</td>" +
        '<td id="senha">' +
        '<input class="senha-input" style="margin:0;" id="senha-' +
        data[i].username +
        '" type="password" placeholder="***********">' +
        '<img class="img-password" id="img-' +
        data[i].username +
        '" src="/img/eye_closed.png" onclick="showPassword(this.id)">' +
        "</td>" +
        '<td id="nome">' +
        '<button class="button-update" id="btnUp-' +
        data[i].username +
        '" onclick="buttonUpdate(this.id)">Atualizar</button>' +
        "</td>" +
        '<td class="nome">' +
        "</td>" +
        "</tr>";
    }
  }

  table = table + "</tbody></table>";
  $("#users").append(table);
}

function validateEmail(emailText) {
  if (
    /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(myForm.emailAddr.value)
  ) {
    return true;
  }
  alert("You have entered an invalid email address!");
  return false;
}

function loadWeekTime() {
  axios({
    method: "get",
    url: "/week/all",
  }).then(function (response) {
    if (response.status === 200) {
      //console.log(response);
      insertWeekTimeHtml(response.data);
    } else if (response.status === 404) {
      console.log("404 - Dias não encontrados...");
    } else {
      console.log(response);
    }
  });
}

function insertWeekTimeHtml(data) {
  if (data[0].day === 1) {
    domingoComeco.value = data[0].dayInit;
    domingoFim.value = data[0].dayEnd;
    domingoOff.checked = data[0].dayOff;
  }

  if (data[1].day === 2) {
    segundaComeco.value = data[1].dayInit;
    segundaFim.value = data[1].dayEnd;
    segundaOff.checked = data[1].dayOff;
  }

  if (data[2].day === 3) {
    tercaComeco.value = data[2].dayInit;
    tercaFim.value = data[2].dayEnd;
    tercaOff.checked = data[2].dayOff;
  }

  if (data[3].day === 4) {
    quartaComeco.value = data[3].dayInit;
    quartaFim.value = data[3].dayEnd;
    quartaOff.checked = data[3].dayOff;
  }

  if (data[4].day === 5) {
    quintaComeco.value = data[4].dayInit;
    quintaFim.value = data[4].dayEnd;
    quintaOff.checked = data[4].dayOff;
  }

  if (data[5].day === 6) {
    sextaComeco.value = data[5].dayInit;
    sextaFim.value = data[5].dayEnd;
    sextaOff.checked = data[5].dayOff;
  }

  if (data[6].day === 7) {
    sabadoComeco.value = data[6].dayInit;
    sabadoFim.value = data[6].dayEnd;
    sabadoOff.checked = data[6].dayOff;
  }
}

function updateTime() {
  arrayOfTime = [];
  var arrayHelper = {};

  // Save Domingo
  arrayHelper = {
    day: 1,
    dayInit: domingoComeco.value,
    dayEnd: domingoFim.value,
    dayOff: domingoOff.checked,
    name: "Domingo",
  };
  saveTimeToArray(arrayHelper);
  arrayHelper = {};

  // Segunda-feira
  arrayHelper = {
    day: 2,
    dayInit: segundaComeco.value,
    dayEnd: segundaFim.value,
    dayOff: segundaOff.checked,
    name: "Segunda-Feira",
  };
  saveTimeToArray(arrayHelper);
  arrayHelper = {};

  // Terça-Feira
  arrayHelper = {
    day: 3,
    dayInit: tercaComeco.value,
    dayEnd: tercaFim.value,
    dayOff: tercaOff.checked,
    name: "Terça-Feira",
  };
  saveTimeToArray(arrayHelper);
  arrayHelper = {};

  // Quarta-feira
  arrayHelper = {
    day: 4,
    dayInit: quartaComeco.value,
    dayEnd: quartaFim.value,
    dayOff: quartaOff.checked,
    name: "Quarta-feira",
  };
  saveTimeToArray(arrayHelper);
  arrayHelper = {};

  // Quinta-feira
  arrayHelper = {
    day: 5,
    dayInit: quintaComeco.value,
    dayEnd: quintaFim.value,
    dayOff: quintaOff.checked,
    name: "Quinta-feira",
  };
  saveTimeToArray(arrayHelper);
  arrayHelper = {};

  // Sexta-feira
  arrayHelper = {
    day: 6,
    dayInit: sextaComeco.value,
    dayEnd: sextaFim.value,
    dayOff: sextaOff.checked,
    name: "Sexta-feira",
  };
  saveTimeToArray(arrayHelper);
  arrayHelper = {};

  // Sábado
  arrayHelper = {
    day: 7,
    dayInit: sabadoComeco.value,
    dayEnd: sabadoFim.value,
    dayOff: sabadoOff.checked,
    name: "Sábado",
  };
  saveTimeToArray(arrayHelper);

  saveTime();
}

function saveTimeToArray(data) {
  //console.log(data)

  if (data.dayInit === "") {
    data.dayInit = "00:00";
  }

  if (data.dayEnd === "") {
    data.dayEnd = "00:00";
  }

  arrayOfTime.push({
    day: data.day,
    dayInit: data.dayInit,
    dayEnd: data.dayEnd,
    dayOff: data.dayOff,
    name: data.name,
  });
}

function saveTime() {
  axios
    .post("/week/update", arrayOfTime)
    .then((response) => {
      //console.log(response);
      if (response) {
        snackbarShowSuccess();
        loadWeekTime();
      } else {
        snackbarShowError();
      }
    })
    .catch((error) => {
      console.log(error);
    });
}

function snackbarShowSuccess() {
  var x = document.getElementById("snackbarOk");
  x.className = "show";
  setTimeout(function () {
    x.className = x.className.replace("show", "");
  }, 3000);
}

function snackbarShowError() {
  var x = document.getElementById("snackbarError");
  x.className = "show";
  setTimeout(function () {
    x.className = x.className.replace("show", "");
  }, 3000);
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
window.loadUsers = loadUsers;
window.buttonDelete = buttonDelete;
window.showUsers = showUsers;
window.validateEmail = validateEmail;
window.loadWantsToTalk = loadWantsToTalk;
window.saveUser = saveUser;
window.showPassword = showPassword;
window.buttonUpdate = buttonUpdate;
window.loadWeekTime = loadWeekTime;
window.saveTime = saveTime;
window.saveTimeToArray = saveTimeToArray;
window.updateTime = updateTime;
window.insertWeekTimeHtml = insertWeekTimeHtml;
