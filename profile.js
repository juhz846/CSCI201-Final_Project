function generateUserContent(){
	$.ajax({
		type: "POST",
		url: "ProfileServlet",
		data: JSON.stringify({
			type: "user",
			ID: localStorage.getItem("userID"),
		}),		
		success: (result)=>{
			var events = document.getElementById("events");
			const jsonArray = result.split("*");
			console.log(jsonArray)
			if (jsonArray == ''){
				events.innerHTML = "no data to display!";
			} else {
				events.innerHTML = "";
				for (const jsonObj of jsonArray) {
					const json = JSON.parse(jsonObj)
					events.innerHTML += '<div class="row"><div class="event-card"><br><img src= "'+json.img_file+'" width = "200" height = "200"'+
                    'alt = "'+json.name+'"><div id="description"><div><h3><a onclick="goToEventPage(\''+json.name+'\')">'+json.name+'</a></h3>' +
                    '<p>'+json.event_date+'</p></div><div id="right"><p>#: '+json.quantity+'</p><p>'+json.city+", "+json.state+'</p></div></div></div></div>'				
				}
			}
		}
	})
}

function generateOrganizerContent(){
	$.ajax({
		type: "POST",
		url: "ProfileServlet",
		data: JSON.stringify({
			type: "organizer",
			ID: localStorage.getItem("organizerID"),
		}),		
		success: (result)=>{
			var events = document.getElementById("events");
			const jsonArray = result.split("*");
			console.log(jsonArray)
			if (jsonArray == ''){
				events.innerHTML = "no data to display!";
			} else {
				events.innerHTML = "";
				for (const jsonObj of jsonArray) {
					const json = JSON.parse(jsonObj)
					events.innerHTML += '<div class="row"><div class="event-card"><br><img src= "'+json.img_file+'" width = "200" height = "200"'+
                    'alt = "'+json.name+'"><div id="description"><div><h3><a onclick="goToEventPage(\''+json.name+'\')">'+json.name+'</a></h3>' +
                    '<p>'+json.event_date+'</p></div><div id="right"><p>available: '+json.availability+'</p><p>'+json.city+", "+json.state+'</p></div></div></div></div>'				
				}
			}
		}
	})
}

function logout(){
	function logout(){
	window.location.href = "login_page.html";
	localStorage.setItem("userID", null)
	localStorage.setItem("organizerID", null)
}
}