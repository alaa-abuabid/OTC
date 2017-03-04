
// catch all message from web socket check the kind of the message 
//  1. regular message 
//  2. reply message
//  3. open or close private channel 
function notify(myData1) {
	// check if the message not empty
	if(myData1 != ""){
		var myData = JSON.parse(myData1);
		// check the order of the notify if it open new private channel
		if(myData.ID == "OpenPrivate"){
			openPrivateChannelOnline(myData.NickName);
			// check the order of the notify if it close private channel
		}else if(myData.ID == "ClosePrivate"){
			closePrivateChannelonline(myData.NickName);
		}else{
			if (myData.Parent == 0 ){
				 var channelWR = document.getElementById(myData.Channel+"Div");
				 createMsg(myData , channelWR );
				 currentChannel=document.getElementById('channelNameInfo').innerHTML;
				 if(currentChannel == myData.Channel){
					 resetNotification(myData.Channel);
					 document.getElementById(myData.Channel+"MsgNT").innerHTML="0";
				 }
	  	    		var NTMsg=document.getElementById(myData.Channel+"MsgNT").innerHTML;
	  	    		document.getElementById(myData.Channel+"MsgNT").innerHTML=Number(NTMsg)+1;
	  	    		
			}else{
				    // reply message 
					 var commWindow = document.getElementById(myData.Parent+"DivReply");
					 createReplyMsg(myData , commWindow );
					 var channelWR = document.getElementById(myData.Channel+"Div");
					 createLinkReply(myData, channelWR);
					 currentChannel=document.getElementById('channelNameInfo').innerHTML;
					 if(currentChannel == myData.Channel){
						 resetNotification(myData.Channel);
						 document.getElementById(myData.Channel+"ReplyNT").innerHTML="0";
					 }
	  	    		var ReplyNT=document.getElementById(myData.Channel+"ReplyNT").innerHTML;
	  	    		document.getElementById(myData.Channel+"ReplyNT").innerHTML=Number(ReplyNT)+1;
	  	    		var NTMsg=document.getElementById(myData.Channel+"MsgNT").innerHTML;
	  	    		document.getElementById(myData.Channel+"MsgNT").innerHTML=Number(NTMsg)+1;
				 	var idCurrentComm = document.getElementById("TitleReplyMsg").innerHTML;
					if((idCurrentComm != myData.Parent+"DivReply") && (idCurrentComm != "") ){
							document.getElementById(idCurrentComm+"DivReply").style.display='none';
						}
					 document.getElementById("TitleReplyMsg").innerHTML = myData.Parent;
					 commWindow.setAttribute('class', 'visible');
					 commWindow.style.display='';
			}
		}
	}
}