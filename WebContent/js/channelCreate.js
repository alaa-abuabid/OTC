

// close the private channel when the other participant click un-subscribed
function closePrivateChannelonline(NickName){
	    	alert("unsubscribed Private Channel "+ NickName);
	    	var ButtonChannel = document.getElementById(NickName);
	    	ButtonChannel.parentElement.removeChild(ButtonChannel);
	    	var TextChannel = document.getElementById(NickName+"Div");
	    	TextChannel.parentElement.removeChild(TextChannel);
	    	if(document.getElementById('channelNameInfo')== null){
	    		document.getElementById('channelNameInfo').innerHTML="";
	    	}
	    	document.getElementById('channelNameInfo').innerHTML="";
	    	document.getElementById('ReplyWindow').style.display = "none";	
}

//open the private channel when the other participant click the name of the user
function openPrivateChannelOnline(NickName) {
      	    	var nameChannel = document.createTextNode(NickName);
      	    	var BtnIdChannelName=NickName;
				var newLi = document.createElement("LI");
				var MainDiv = document.createElement("DIV");
				var newBtn = document.createElement("BUTTON");
				// create unread labels  
				var unReadM = document.createElement("SPAN");
				unReadM.setAttribute('class', 'label label-danger');
				unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
				var NumUnRd = document.createTextNode("0");
				unReadM.appendChild(NumUnRd);
				var unReadC = document.createElement("SPAN");
				unReadC.setAttribute('class', 'label label-success');
				unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
				var NumUnRdC = document.createTextNode("0");
				unReadC.appendChild(NumUnRdC);
				MainDiv.setAttribute('class', 'hidden');
				MainDiv.setAttribute('id', BtnIdChannelName+"Div");
    		    // create channel button 
    		    newBtn.setAttribute('id', BtnIdChannelName);
    		    newBtn.setAttribute('class', 'btn btn-default btn-block');
    		    newBtn.style.textAlign = "left";
    		    newBtn.appendChild(nameChannel);
    		    newBtn.appendChild(unReadM);
    		    newBtn.appendChild(unReadC);
    		    
    		    document.getElementById("msgWindow").appendChild(MainDiv);
    		    newLi.appendChild(newBtn);
    		    newBtn.onclick = function() { clickTheChannel(this.id) };
    		    document.getElementById("privateChannelList").appendChild(newLi);
}

//open the private channel when the user click another user nickName in channel 
function openPrivateChannel(NickName) {
	
    var r = confirm("Press a button!\n Ok to open a private channel with "+NickName);
    if (r == true) {
       //// open the channel
       	var privateChannel= new Object();
       	privateChannel.NickName= document.getElementById("addNickName").innerHTML;
       	privateChannel.ID=NickName;
    
    	var sendMsg= JSON.stringify(privateChannel);
    	// send to server to update the DB with new private channel creation
      	$.post("/OTC/CreatePrivateChannelServlet",sendMsg, function(response) {
      		var mydata = response;
      	    var resultFromServer=mydata.result;

      	    if(resultFromServer == false){
      	    	alert("Error");	
      	    }else if(resultFromServer == true){
      	    	alert("private channel created with " + NickName);
      	    	// create private channel and add it to private channel list
      	    	var nameChannel = document.createTextNode(mydata.channel.NickName);
      	    	var BtnIdChannelName=mydata.channel.NickName;
				var newLi = document.createElement("LI");
				var MainDiv = document.createElement("DIV");
				var newBtn = document.createElement("BUTTON");
				
				var unReadM = document.createElement("SPAN");
				unReadM.setAttribute('class', 'label label-danger');
				unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
				var NumUnRd = document.createTextNode("0");
				unReadM.appendChild(NumUnRd);
				
				var unReadC = document.createElement("SPAN");
				unReadC.setAttribute('class', 'label label-success');
				unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
				var NumUnRdC = document.createTextNode("0");
				unReadC.appendChild(NumUnRdC);
				
				
				MainDiv.setAttribute('class', 'hidden');
				MainDiv.setAttribute('id', BtnIdChannelName+"Div");
    		    
    		    newBtn.setAttribute('id', BtnIdChannelName);
    		    newBtn.setAttribute('class', 'btn btn-default btn-block');
    		    newBtn.style.textAlign = "left";
    		    newBtn.appendChild(nameChannel);
    		    newBtn.appendChild(unReadM);
    		    newBtn.appendChild(unReadC);
    		    
    		    document.getElementById("msgWindow").appendChild(MainDiv);
    		    newLi.appendChild(newBtn);
    		    newBtn.onclick = function() { clickTheChannel(this.id) };
    		    document.getElementById("privateChannelList").appendChild(newLi);
      	    }	
      		
      	});
    }
}


// create a private channel list in log in event
function createPrivateChannelsList( myData){
	var nameChannel = document.createTextNode(myData.NickName);
	var BtnIdChannelName=myData.NickName;
	var newLi = document.createElement("LI");
	var MainDiv = document.createElement("DIV");
	
	var newBtn = document.createElement("BUTTON");
	
	var unReadM = document.createElement("SPAN");
	unReadM.setAttribute('class', 'label label-danger');
	unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
	var NumUnRd = document.createTextNode(myData.Unread);
	unReadM.appendChild(NumUnRd);
	
	var unReadC = document.createElement("SPAN");
	unReadC.setAttribute('class', 'label label-success');
	unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
	var NumUnRdC = document.createTextNode(myData.Replies);
	unReadC.appendChild(NumUnRdC);
	
	MainDiv.setAttribute('class', 'hidden');
	MainDiv.setAttribute('id', BtnIdChannelName+"Div");
   
    newBtn.setAttribute('id', BtnIdChannelName);
    newBtn.setAttribute('class', 'btn btn-default btn-block');
    newBtn.style.textAlign = "left";
    newBtn.appendChild(nameChannel);
    newBtn.appendChild(unReadM);
    newBtn.appendChild(unReadC);
    
    newBtn.onclick = function() { clickTheChannel(this.id) };
    
    // create message  call create message function
    document.getElementById("msgWindow").appendChild(MainDiv);
    for(l1 in myData.Messages){
    	createMsg(myData.Messages[l1], MainDiv);	
    }
    
    newLi.appendChild(newBtn);
    document.getElementById("privateChannelList").appendChild(newLi);
}


//create a public channel list in log in event
function createPublicChannelsList( myData){
	var nameChannel = document.createTextNode(myData.Name);
	var BtnIdChannelName=myData.Name;
	var newLi = document.createElement("LI");
	var MainDiv = document.createElement("DIV");
	
	var newBtn = document.createElement("BUTTON");
	
	var unReadM = document.createElement("SPAN");
	unReadM.setAttribute('class', 'label label-danger');
	unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
	var NumUnRd = document.createTextNode(myData.Unread);
	unReadM.appendChild(NumUnRd);
	
	var unReadC = document.createElement("SPAN");
	unReadC.setAttribute('class', 'label label-success');
	var NumUnRdC = document.createTextNode(myData.Replies);
	unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
	unReadC.appendChild(NumUnRdC);
	
	MainDiv.setAttribute('class', 'hidden');
	MainDiv.setAttribute('id', BtnIdChannelName+"Div");
   
    newBtn.setAttribute('id', BtnIdChannelName);
    newBtn.setAttribute('class', ' btn btn-default btn-block');
    newBtn.style.textAlign = "left";
    newBtn.appendChild(nameChannel);
    newBtn.appendChild(unReadM);
    newBtn.appendChild(unReadC);
    
    newBtn.onclick = function() { clickTheChannel(this.id) };
    
    // create message
    document.getElementById("msgWindow").appendChild(MainDiv);
 // create message  call create message function
    for(l1 in myData.Messages){
    	createMsg(myData.Messages[l1], MainDiv);	
    }
   
    newLi.appendChild(newBtn);
    document.getElementById("publicChannelList").appendChild(newLi);
}



// on click the channel button from channel list update the window of channel windows and reset the unread notification to zero 
function clickTheChannel(nameChannel) {
	
    currentChannel=document.getElementById('channelNameInfo').innerHTML;
    if(currentChannel != ""){
    	document.getElementById(currentChannel+"Div").setAttribute('class', 'hidden');
    }
    resetNotification(nameChannel);
    document.getElementById(nameChannel+"Div").setAttribute('class', 'visible');
    var newChannelName = document.createTextNode(nameChannel);
    var element = document.getElementById("msgWindow");
    element.scrollTop = element.scrollHeight;
    document.getElementById('channelNameInfo').innerHTML=nameChannel;
    document.getElementById('ReplyWindow').style.display = "none";
    
   
}


// reset the notification of channel and update the DB 
function resetNotification(nameChannel){
	 var unRead = new Object();
	    unRead.NickName=document.getElementById("addNickName").innerHTML;
	    unRead.Chat=nameChannel;
		var channelName=document.getElementById(nameChannel);
		var channelKind = channelName.parentNode;
		
		// check the kind of the channel and send to the server to update the channel unread notification
		// public channel update the unread notification
		if(channelKind.parentNode.id == "publicChannelList"){
			$.post("/OTC/PublicResetSerlvet", JSON.stringify(unRead), function(response) {
		    	var mydata = response;
		  	    var resultFromServer=mydata.result;
		  	    if(resultFromServer == "false"){
		  	    	alert("Error");	
		  	    }else if(resultFromServer == "true"){
		  	    	document.getElementById(nameChannel+"ReplyNT").innerHTML="0";
		  	    	document.getElementById(nameChannel+"MsgNT").innerHTML="0";
		  	    }
		    });// post
			
	    // private channel update the unread notification	
		}else if(channelKind.parentNode.id == "privateChannelList"){
			$.post("/OTC/PrivateResetSerlvet", JSON.stringify(unRead), function(response) {
		    	var mydata = response;
		  	    var resultFromServer=mydata.result;
		  	    if(resultFromServer == "false"){
		  	    	alert("Error");	
		  	    }else if(resultFromServer == "true"){
		  	    	document.getElementById(nameChannel+"ReplyNT").innerHTML="0";
		  	    	document.getElementById(nameChannel+"MsgNT").innerHTML="0";
		  	    }
		    });// post
		}
}

