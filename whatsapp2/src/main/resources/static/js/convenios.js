import Websocket from "./websocket.js";
const websocket = new Websocket();

function onloadInit(){
	websocket.connect;
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

window.addEventListener('onload', onloadInit());
window.addEventListener('onload', onloadInit);
window.toggleHamburger = toggleHamburger;




