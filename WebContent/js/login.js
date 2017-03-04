

// After login this function will build the main UI of the application
function loadPage( myData){
	
		// open web socket
		var wsUri = "ws://"+window.location.host+"/OTC/chat/"+myData.user.NickName;
	    websocket = new WebSocket(wsUri);
	    websocket.onopen = function(evt) {
	    	
	    };
	    websocket.onmessage = function(evt) {
	    	notify(evt.data);
	    };
	    websocket.onerror = function(evt) {
	    	
	    };
	    
	    websocket.onclose = function(evt) {
	    	websocket = null;
		  } 
		// when login is true we hid the login div and show the main page 
		document.getElementById('id01').style.display='none';
		document.getElementById('MainDiv').style.display='none';
		document.getElementById('thePage').style.display='';
		document.getElementById('homePage').style.display='';
		document.getElementById('createChannelLi').setAttribute('class', '');
		document.getElementById('homeLi').setAttribute('class', 'active');
		document.getElementById('searchChannelLi').setAttribute('class', '');
		
		// create user profile
			// user name 
			var NickNameFromServer = document.createTextNode(myData.user.NickName);
			document.getElementById("addNickName").appendChild(NickNameFromServer);
			// user photo 
			/////////////////////////////////////////////var userPhotoFromServer = myData.user.Photo;
			var userPhotoFromServer = myData.user.Photo;
			var elem = document.createElement("IMG");
			
			elem.setAttribute("class", "img-circle");
			elem.src = userPhotoFromServer;
			elem.setAttribute("height", "50");
			elem.setAttribute("width", "50");
			elem.setAttribute("alt", "username");
			document.getElementById("addUserPhoto").appendChild(elem);
		
		// create public channels list
			for(l in myData.PublicChannels){
				createPublicChannelsList( myData.PublicChannels[l]);
			}
		
		// create private channel list
		for(k in myData.PrivateChannels){
			createPrivateChannelsList( myData.PrivateChannels[k]);
		}	
	
}




