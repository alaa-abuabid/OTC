// on load the website the loader 
	function onloadFunction(){
	myVar = setTimeout(showPage, 200);
	}
	
	function showPage() {
		  document.getElementById("loader").style.display = "none";
		  document.getElementById("website").style.display = "block";
	}
	
	