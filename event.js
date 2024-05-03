function generateContent(eventName){
	$.ajax({
		type: "POST",
		url: "EventDisplayServlet",
		data: JSON.stringify({
		}),
		success: (result)=>{
			const jsonArray = result.split("*");
			console.log(jsonArray)
			for (const jsonObj of jsonArray) {
				const json = JSON.parse(jsonObj)
				if (json.name == eventName){
					document.getElementById("name").innerHTML = json.name;
					document.getElementById("location").innerHTML = "<strong>Location: </strong>"+json.city + ", " + json.state;
					document.getElementById("date").innerHTML = "<strong>Date/Time: </strong>"+json.event_date + " " + json.event_time;
					document.getElementById("description").innerHTML = "<strong>Description: </strong>"+json.description;
					document.getElementById("price").innerHTML = "<strong>Price:</strong> $"+json.price;
					document.getElementById("image").innerHTML = '<img align="right" class="event-image" src="'+json.img_file+'" alt="Image failed to load"></img>';

					break;
				}
			}
		}
	})
}

function buyTicket(){
	$.ajax({
		type: "POST",
		url: "EventPurchaseServlet",
		data: JSON.stringify({
			name: document.getElementById("name").innerHTML,
			userID: localStorage.getItem("userID"),
			quantity: document.getElementById("quantity").value
		}),
		success: (result)=>{
			console.log(result)
			alert(result)
		},
		error: (xhr, status, error)=>{
			console.log(xhr.responseText)
			alert(xhr.responseText)
		}
	})
}

function getQueryParam(name) {
	console.log(name)
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}