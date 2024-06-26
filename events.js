function goToEventPage(eventName) {
	window.location.href = "event.html?eventName="+eventName;
}

function search(){
	$.ajax({
		type: "GET",
		url: "EventDisplayServlet",
		data: {
			search:$("#searchbox").val()
		},
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
		    '<p>'+json.event_date+'</p></div><div id="right"><p>$'+json.price+'</p><p>'+json.city+", "+json.state+'</p></div></div></div></div>'				
				}
			}
		}
	})
}

function generateContent(){
	$.ajax({
		type: "POST",
		url: "EventDisplayServlet",
		data: JSON.stringify({
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
                    '<p>'+json.event_date+'</p></div><div id="right"><p>$'+json.price+'</p><p>'+json.city+", "+json.state+'</p></div></div></div></div>'				
				}
			}
		}
	})
}
