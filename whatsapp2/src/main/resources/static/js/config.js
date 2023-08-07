let passwordText = document.getElementById("psw");
let passwordText2 = document.getElementById("psw-repeat");

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