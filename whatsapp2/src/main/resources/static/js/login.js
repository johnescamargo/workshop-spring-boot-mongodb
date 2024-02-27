const username = document.getElementById("username");

function getUsername() {
  document.getElementById("username").value = localStorage.getItem(
    "username",
    username.value
  );
}

username.addEventListener("input", function () {
  localStorage.setItem("username", username.value);
});
