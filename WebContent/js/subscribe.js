
// subscribe button send to the serve the name of the channel  and nickname to update DB 
// and call createPublicChannelsList() function to add this channel to public channel list
function subscribeFun(nameChannel) {
    
	var subscribeChannel= new Object();
	subscribeChannel.Channel = nameChannel;
	subscribeChannel.NickName = document.getElementById('addNickName').innerHTML;
	$.post("/OTC/SubscribeServlet", JSON.stringify(subscribeChannel), function(response) {
		var mydata = response;
  	    var resultFromServer=mydata.result;

  	    if(resultFromServer == false){
  	    	alert("Error");	
  	    }else if(resultFromServer == true){
  	    	alert("Subscribed");
  	    	createPublicChannelsList( mydata.channel);

		    // Remove Table after subscribed
		    var myNode = document.getElementById("TableSearchId");
		    while (myNode.firstChild) {
		        myNode.removeChild(myNode.firstChild);
		    }
  	    	
  	  		document.getElementById('searchTable').style.display='none';
  	  		document.getElementById('homePage').style.display='';
  	    }
	});
};



//un-subscribe button send to the serve the name of the channel and nickname to update DB 
//and remove it from public  channel list or private channel list
function unsubscribeChannelBtn() {
	
	  var unsubscribe= new Object();
	  var username=document.getElementById("addNickName");
	  var channelname=document.getElementById("channelNameInfo");
	  var channelName=document.getElementById("channelNameInfo").innerHTML;
	  var channelKind = document.getElementById(channelName).parentNode;
	  
		if(channelKind.parentNode.id == "publicChannelList"){
			unsubscribe.Channel=channelname.innerHTML;
			  unsubscribe.NickName=username.innerHTML;
			  var sendMsg= JSON.stringify(unsubscribe);
			$.post("/OTC/UnsubscribeServlet",sendMsg, function(response) {
			    // handle response from your servlet.
				var mydata = response;
			    var resultFromServer=mydata.result;

			    if(resultFromServer == "error"){
			    	alert("error");	
			    }else if(resultFromServer == "unsubscribed"){
			    	alert("unsubscribed");
			    	var channelname=document.getElementById("channelNameInfo");
			    	var ButtonChannel = document.getElementById(channelname.innerHTML);
			    	ButtonChannel.parentElement.removeChild(ButtonChannel);
			    	var TextChannel = document.getElementById(channelname.innerHTML+"Div");
			    	TextChannel.parentElement.removeChild(TextChannel);
			    	document.getElementById('channelNameInfo').innerHTML="";
			    	document.getElementById('ReplyWindow').style.display = "none";
			    }
			   
			}); 
		}else if(channelKind.parentNode.id == "privateChannelList"){
			unsubscribe.Chat=channelname.innerHTML;
			  unsubscribe.NickName=username.innerHTML;
			  var sendMsg= JSON.stringify(unsubscribe);
			
			$.post("/OTC/PrivateUnsubscribeServlet",sendMsg, function(response) {
			    // handle response from your servlet.
				var mydata = response;
			    var resultFromServer=mydata.result;

			    if(resultFromServer == "error"){
			    	alert("error");	
			    }else if(resultFromServer == "unsubscribed"){
			    	alert("unsubscribed");
			    	var channelname=document.getElementById("channelNameInfo");
			    	var ButtonChannel = document.getElementById(channelname.innerHTML);
			    	ButtonChannel.parentElement.removeChild(ButtonChannel);
			    	var TextChannel = document.getElementById(channelname.innerHTML+"Div");
			    	TextChannel.parentElement.removeChild(TextChannel);
			    	document.getElementById('channelNameInfo').innerHTML="";
			    	document.getElementById('ReplyWindow').style.display = "none";
			    }
			   
			}); 
		}  
};
