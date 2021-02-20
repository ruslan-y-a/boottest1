var request = new XMLHttpRequest();
request.onload = function() {
	var ajaxlist = JSON.parse(this.responseText);	
	ajaxlist.forEach((entity) => {
	//	document.getElementById("ajaxdiv").innerHTML = `${entity.title}`;
		//document.write("<p>" + `${entity.title}` + "</p>");
	});
};
request.open('GET', ajaxUrl);
request.send();	