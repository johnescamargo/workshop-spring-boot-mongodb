let passwordText = document.getElementById("psw");
let usernameText = document.getElementById("nickname");

function login() {
	axios
		.post("/user/registration/", {
			username: usernameText.value,
			password: passwordText.value
		})
		.then(function(response) {
			if (response.status === 201) {
				
			} else {
				console.log("Usuário não encontrado");
			}

		});

}