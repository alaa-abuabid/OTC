// send reply button send reply message by the the web socket 
function ReplyMsgBtn() {
	if (websocket != null){
		// create the send message 
		var newMessage= new Object();
		var IdMsg1=document.getElementById("TitleReplyMsg").innerHTML;
		var IdMsg=IdMsg1+"Div";
		
		newMessage.Sender= document.getElementById("addNickName").innerHTML;
		var MsgText=document.getElementById('ReplyMsgId').value;
		// get the nickname of the reply message  
		var MsgUserTag = document.getElementById(IdMsg).childNodes[1].childNodes[0].childNodes[0].id;
		// add @ and message text 
		newMessage.Message="@"+MsgUserTag + " "+MsgText;
		newMessage.Parent=IdMsg1;
		var channelName=document.getElementById("channelNameInfo").innerHTML;
		newMessage.Channel=channelName;
		
		// check the kind of the channel and set the id of the send message 
		var channelKind = document.getElementById(channelName).parentNode;
		if(channelKind.parentNode.id == "publicChannelList"){
			newMessage.ID="Public";
		}else if(channelKind.parentNode.id == "privateChannelList"){
			newMessage.ID="Private";
		}
		newMessage.Photo = "12e.jpg";
		var sendMsg= JSON.stringify(newMessage);
		 websocket.send(sendMsg);
	}
}

// send regular message by websocket
function sendMsgBtn() {
	if (websocket != null){
		var newMessage= new Object();
		newMessage.Sender= document.getElementById("addNickName").innerHTML;
		newMessage.Message=document.getElementById('MsgId').value;
		var channelName=document.getElementById("channelNameInfo").innerHTML;
		newMessage.Channel=channelName;
		newMessage.Parent="0";
		// tested code 
		var imgSrc1 = document.getElementById("addUserPhoto").getElementsByTagName("IMG");
		var photoSrc = imgSrc1.src;
		newMessage.Photo = "12e.jpg";
		var channelKind = document.getElementById(channelName).parentNode;
		if(channelKind.parentNode.id == "publicChannelList"){
			newMessage.ID="Public";
		}else if(channelKind.parentNode.id == "privateChannelList"){
			newMessage.ID="Private";
		}
		var sendMsg= JSON.stringify(newMessage);
   		 websocket.send(sendMsg);
    }
}
