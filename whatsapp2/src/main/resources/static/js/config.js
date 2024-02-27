import Websocket from "./websocket.js";
const websocket = new Websocket();

let passwordText = document.getElementById("psw");
let passwordText2 = document.getElementById("psw-repeat");

function onloadInit() {
  websocket.connect;
}

passwordText2.addEventListener("input", function () {
  if (this.value === passwordText.value) {
  }
  //border-color: blue;
  //border-style: solid;
});

function newPassword() {
  var user = $("#my-username").html();
  var usernameSession = user.trim();

  if (passwordText.value === passwordText2.value) {
    axios
      .post("/user/newpassword", {
        username: usernameSession,
        password: passwordText.value,
        matchingPassword: passwordText2.value,
      })
      .then(function (response) {
        console.log(response);
        if (response.status === 200) {
          alert("A nova senha foi salva!");
        } else {
          alert("A nova senha não foi salva!");
        }
      });
  }
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

window.toggleHamburger = toggleHamburger;
window.addEventListener("onload", onloadInit());
window.addEventListener("onload", onloadInit);
window.newPassword = newPassword;
