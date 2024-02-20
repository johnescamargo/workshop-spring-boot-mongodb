let passwordText = document.getElementById("psw");
let passwordText2 = document.getElementById("psw-repeat");
let usernameText = document.getElementById("nickname");
let emailText = document.getElementById("email");
var millisecondsToWait = 500;

passwordText2.addEventListener('input', function() {
	if (this.value === passwordText.value){
		console.log("Ok");
	} 
	//border-color: blue;
    //border-style: solid;	
});


function saveUser() {
	let pswLength = (passwordText.value).length;
	let psw2Length = (passwordText2.value).length;
	let emailLength = (emailText.value).length;
	let usernameLength = (usernameText.value).length;
	const role = "ROLER_USER";
	
	
	if (passwordText.value === passwordText2.value && pswLength > 0 && psw2Length > 0 && emailLength > 0 && usernameLength > 0){
		axios
			.post("/user/registration/", {
				username: usernameText.value,
				email: emailText.value,
				password: passwordText.value,
				matchingPassword: passwordText2.value,
				role: role
			})
			.then(function(response) {
				if (response.status === 201) {
					setTimeout(function() {
						location.reload();
					}, millisecondsToWait);
					
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
	}).then(function(response) {
			if (response.status === 200) {
					console.log(response);
			} else if (response.status === 404){
					console.log("404 - Pacientes não encontrados...");
			} else {
				console.log(response);
			}
	});
}


