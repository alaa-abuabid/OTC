// create message function create a new message with all needed elements 
// the function chick if this message is regular message or reply message if 
// reply message call the createReplyMsg() function 
function createMsg(myData , MainDiv) {
	if(myData.Parent==0){
		var Div1 = document.createElement("DIV");
		Div1.setAttribute('class', 'media');
		Div1.setAttribute('id', myData.ID+"Div");
		
		var Div2 = document.createElement("DIV");
		Div2.setAttribute('class', 'media-left');
		
		var Div3 = document.createElement("DIV");
		Div3.setAttribute('class', 'media-right');
		
		var PhotoSender = document.createElement("IMG");
		PhotoSender.setAttribute("class", "img-circle");
		PhotoSender.setAttribute("height", "50");
		PhotoSender.setAttribute("width", "50");
		PhotoSender.setAttribute("alt", "senderPhoto");
		
		var Div4 = document.createElement("DIV");
		Div4.setAttribute('class', 'media-body');
		
		var SenderName = document.createTextNode(myData.Sender);
		var SenderA = document.createElement("A");
		SenderA.setAttribute("href", "#");
		SenderA.appendChild(SenderName);
		SenderA.setAttribute('id', myData.Sender);
		SenderA.onclick = function() { openPrivateChannel(this.id) };
		
		var reply = document.createTextNode("  Reply");
		var newBtnRply = document.createElement("BUTTON");
		newBtnRply.setAttribute('id', myData.ID);
		var MainReplyDiv = document.createElement("DIV");
		MainReplyDiv.setAttribute('id', myData.ID+"DivReply");
		MainReplyDiv.setAttribute('class', 'hidden');
		document.getElementById('ReplymsgWindow').appendChild(MainReplyDiv);

		newBtnRply.setAttribute('class', 'btn btn-danger glyphicon glyphicon-share-alt btn-xs');
		newBtnRply.onclick = function() { replyFun(this.id) };
		newBtnRply.appendChild(reply);

		var NameH4 = document.createElement("H4");
		NameH4.setAttribute('class', 'media-heading');
		NameH4.appendChild(SenderA);
		
		var TimeI = document.createElement("I");
		var Msg = document.createElement("P");
		Msg.setAttribute('id', myData.ID+"TextMsg");
		
		Msg.appendChild(document.createTextNode(myData.Message));
		TimeI.appendChild(document.createTextNode("  "+myData.Date_Time));
		Msg.appendChild(TimeI);
		var userPhotoFromServer1 = myData.Photo;
		PhotoSender.src = userPhotoFromServer1;
		Div4.appendChild(NameH4);
		Div4.appendChild(Msg);
		Div4.appendChild(newBtnRply);
		Div2.appendChild(PhotoSender);
		Div1.appendChild(Div2);
		Div1.appendChild(Div4);
		Div1.appendChild(Div3);
		MainDiv.appendChild(Div1);
		var element = document.getElementById("msgWindow");
	    element.scrollTop = element.scrollHeight;
	}else{
		// reply message
		createLinkReply(myData, MainDiv);
		var MainReplyDiv=document.getElementById(myData.Parent+"DivReply");
		var threadDiv=document.getElementById("ReplymsgWindow").contains(document.getElementById(myData.Parent+"DivReply"));
		if(!threadDiv){
			var MainReplyDiv = document.createElement("DIV");
			MainReplyDiv.setAttribute('id', myData.Parent+"DivReply");
			MainReplyDiv.setAttribute('class', 'hidden');
			document.getElementById('ReplymsgWindow').appendChild(MainReplyDiv);
		}
		createReplyMsg(myData , MainReplyDiv);
		
	}
}
