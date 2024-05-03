function userType(){
	const userID = localStorage.getItem("userID")
	const organizerID = localStorage.getItem("organizerID")
	
	if (userID > 0){
		return 1;
	}
	else if (organizerID > 0){
		return 2;
	}
	else return -1;
}

function  generateNavbar(){
	if (userType() == 1){
		document.getElementById("navbar").innerHTML = ""+
		"<h1>201 ticketing</h1>"+
		"<div>"+
		    '<button id="home-btn"><a href="events.html">Home</a></button> '+
            '<button id="signin-btn"><a href="#" onclick="logout()">Logout</a></button> '+
            '<button id="profile-btn" onclick="window.location.href=\'profile-client.html\'"><i class="fa fa-user"></i></button>'+
        "</div>"
	} else if (userType() == 2){
		document.getElementById("navbar").innerHTML = ""+
		"<h1>201 ticketing</h1>"+
		"<div>"+
            '<button id="signin-btn"><a href="#" onclick="logout()">Logout</a></button> '+
        "</div>"
	} else {
		document.getElementById("navbar").innerHTML = ""+
		"<h1>201 ticketing</h1>"+
		"<div>"+
            '<button id="signin-btn"><a href="login_page.html">Login</a></button>'+
        "</div>"
	}
}

function logout(){
	window.location.href = "login_page.html";
	localStorage.setItem("userID", null)
	localStorage.setItem("organizerID", null)
}

function userLogin(userID){
	window.location.href = "events.html";
	localStorage.setItem("userID", userID);
	localStorage.setItem("organizerID", null);
}

function organizerLogin(organizerID){
	window.location.href = "events.html";
	localStorage.setItem("userID", null);
	localStorage.setItem("organizerID", organizerID);
}