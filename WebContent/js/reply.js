

////////////////  reply javascript ///////////////////////////////////






// create a link to the reply message and show it in the channel window
function createLinkReply(myData , MainDiv){
	var LinkReplyText = document.createTextNode(myData.Message);
	// create new line
	var newLine=document.createElement("BR");
	// create a clickable link to the reply 
	var LinkReply = document.createElement("A");
	LinkReply.setAttribute("href", "#");
	LinkReply.appendChild(LinkReplyText);
	LinkReply.appendChild(newLine);
	LinkReply.setAttribute('id', myData.Parent);
	LinkReply.onclick = function() { replyFun(this.id) };
	MainDiv.appendChild(LinkReply);
	
	// scroll the window of replies down
	var element = document.getElementById("msgWindow");
    element.scrollTop = element.scrollHeight;
}


// create reply message and show it in reply window 
function createReplyMsg(myData , MainDiv) {
	    // create and update the Settings  message elements
		var Div1 = document.createElement("DIV");
		Div1.setAttribute('class', 'media');
		Div1.setAttribute('id', myData.ID+"Div");
		
		var Div2 = document.createElement("DIV");
		Div2.setAttribute('class', 'media-left');
		
		var Div3 = document.createElement("DIV");
		Div3.setAttribute('class', 'media-right');
		
		// create new img and update the Settings 
		var PhotoSender = document.createElement("IMG");
		PhotoSender.setAttribute("class", "img-circle");
		PhotoSender.setAttribute("height", "50");
		PhotoSender.setAttribute("width", "50");
		PhotoSender.setAttribute("alt", "senderPhoto");
		
		var Div4 = document.createElement("DIV");
		Div4.setAttribute('class', 'media-body');
		
		// create clickable name of sender 
		var SenderName = document.createTextNode(myData.Sender);
		var SenderA = document.createElement("A");
		SenderA.setAttribute("href", "#");
		SenderA.appendChild(SenderName);
		SenderA.setAttribute('id', myData.Sender);
		SenderA.onclick = function() { openPrivateChannel(this.id) };
		
		// create a reply button to this message
		var reply = document.createTextNode("  Reply");
		var newBtnRply = document.createElement("BUTTON");
		
		// create a reply window to this message
		var MainReplyDiv = document.createElement("DIV");
		MainReplyDiv.setAttribute('id', myData.ID+"DivReply");
		MainReplyDiv.setAttribute('class', 'hidden');
		document.getElementById('ReplymsgWindow').appendChild(MainReplyDiv);
		newBtnRply.setAttribute('id', myData.ID);
		
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
		var element = document.getElementById("ReplymsgWindow");
	    element.scrollTop = element.scrollHeight;
}



// reply button on click open a new window to write reply on message
function replyFun( myData){
	var commNum=document.getElementById("ReplymsgWindow").hasChildNodes();
	var noneDiv=document.getElementById("ReplymsgWindow").contains(document.getElementById(myData+"DivReply"));
	if(commNum){
		var idCurrentComm = document.getElementById("TitleReplyMsg").innerHTML;
		if(idCurrentComm != ""){
			// replace the id of reply window
			document.getElementById(idCurrentComm+"DivReply").style.display='none';
		}
	}
	
	if( !noneDiv){
		// create a new window reply message if there no window
		var MainDiv = document.createElement("DIV");
		MainDiv.setAttribute('id', myData+"DivReply");
		document.getElementById('TitleReplyMsg').innerHTML= myData;
		document.getElementById('ReplyWindow').style.display='';
		document.getElementById('ReplymsgWindow').appendChild(MainDiv);
	}else{
		// show the window of this reply message
		document.getElementById(myData+"DivReply").setAttribute('class', 'visible');
		document.getElementById(myData+"DivReply").style.display='';
		document.getElementById('TitleReplyMsg').innerHTML= myData;
		document.getElementById('ReplyWindow').style.display='';
	}
}



//Cancel button closed the reply window
function CancelBtn(){
	document.getElementById('ReplyWindow').style.display = "none";
}