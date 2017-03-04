

// search by channel name button send to the server the name of the channel and the server get the channel with this name if it exists
function channelNameSearchBtn(){
    	var channelNameSearch=document.getElementById("channelNameSearch").value;
    	var sendMsg= JSON.stringify(channelNameSearch);
    	 
      	$.post("/OTC/SearchChannelServlet",sendMsg, function(response) {
      	    // handle response from servlet.
      		var mydata = response;
      	    var resultFromServer=mydata.result;

      	    if(resultFromServer == false){
      	    	alert("There is no channel in this name");	
      	    }else if(resultFromServer == true){
      	    	alert("Successful");
      	    	document.getElementById('searchPage').style.display='none';
      	    	document.getElementById('searchTable').style.display='';
      	    	// create the table to the found channels
      	    	for(k in mydata.ChannelNames){
      	    		var number = document.createTextNode(k);
					var nameChannel = document.createTextNode(mydata.ChannelNames[k].Name);
					var desChannel = document.createTextNode(mydata.ChannelNames[k].Description);
					var numChannel = document.createTextNode(mydata.ChannelNames[k].Creator);
					var BtnIdChannelName=mydata.ChannelNames[k].Name;
					
					var LineTable = document.createElement("TR");
					var Number = document.createElement("TD");
					Number.appendChild(number);
					var TDName = document.createElement("TD");
					TDName.appendChild(nameChannel);
					var TDDes = document.createElement("TD");
					TDDes.appendChild(desChannel);
					var TDNum = document.createElement("TD");
					TDNum.appendChild(numChannel);
					
					
	    		    var newBtn = document.createElement("BUTTON");
	    		    newBtn.setAttribute('id', BtnIdChannelName);
	    		    newBtn.setAttribute('class', 'btn btn-info');
	    		    var subScribeTX = document.createTextNode("Subscribe");
	    		    newBtn.appendChild(subScribeTX);
	    		    newBtn.onclick = function() { subscribeFun(this.id) };
	    		    
	    		    LineTable.appendChild(Number);
	    		    LineTable.appendChild(TDName);
	    		    LineTable.appendChild(TDDes);
	    		    LineTable.appendChild(TDNum);
	    		    LineTable.appendChild(newBtn);
	    		    document.getElementById("TableSearchId").appendChild(LineTable);
	    		    if (k === 99) { break; }
      	    	}
      	    	
      	   	}  
      	}); 
    };
   
    
 // search by  nameNick button send to the server the nickname and the server get the channels of this nickname if it exists   
 function channelNicknameSearchBtn(){
    	var NickNameSearch=document.getElementById("channelNicknameSearch").value;
    	
    	var sendMsg= JSON.stringify(NickNameSearch);
    	 
      	$.post("/OTC/SearchNickNameServlet",sendMsg, function(response) {
      	    // handle response from your servlet.
      		var mydata = response;
      	    var resultFromServer=mydata.result;

      	    if(resultFromServer == false){
      	    	alert("There is no channel");	
      	    }else if(resultFromServer == true){
      	    	alert("Successful");
      	    	document.getElementById('searchPage').style.display='none';
      	    	document.getElementById('searchTable').style.display='';
      	        // create the table to the found channels
      	    	for(k in mydata.ChannelNames){
      	    		var number = document.createTextNode(k);
					var nameChannel = document.createTextNode(mydata.ChannelNames[k].Name);
					var desChannel = document.createTextNode(mydata.ChannelNames[k].Description);
					var numChannel = document.createTextNode(mydata.ChannelNames[k].Creator);
					var BtnIdChannelName=mydata.ChannelNames[k].Name;
					
					var LineTable = document.createElement("TR");
					var Number = document.createElement("TD");
					Number.appendChild(number);
					var TDName = document.createElement("TD");
					TDName.appendChild(nameChannel);
					var TDDes = document.createElement("TD");
					TDDes.appendChild(desChannel);
					var TDNum = document.createElement("TD");
					TDNum.appendChild(numChannel);
					
					
	    		    var newBtn = document.createElement("BUTTON");
	    		    newBtn.setAttribute('id', BtnIdChannelName);
	    		    newBtn.setAttribute('class', 'btn btn-info');
	    		    var subScribeTX = document.createTextNode("Subscribe");
	    		    newBtn.appendChild(subScribeTX);
	    		    newBtn.onclick = function() { subscribeFun(this.id) };
	    		    
	    		    LineTable.appendChild(Number);
	    		    LineTable.appendChild(TDName);
	    		    LineTable.appendChild(TDDes);
	    		    LineTable.appendChild(TDNum);
	    		    LineTable.appendChild(newBtn);
	    		    document.getElementById("TableSearchId").appendChild(LineTable);
	    		    
      	    	}
      	   	}  
      	}); 
    };