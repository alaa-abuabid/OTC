

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		var app = angular.module('myApp', []);
		app.controller('login', function($scope) {
		    $scope.loginBtn = function loginBtn(){
		    	var user= new Object();
		    	//var mydata= new Object();
		    	var result;	
		    	user.UserName=$scope.username ;
		    	user.Password=$scope.password ;
		    	$.post("/OTC/LoginServlet", JSON.stringify(user), function(response) {
		    		var myData = response;
		    		var resultFromServer=myData.result;
		    		if(resultFromServer == false){
		    	    	alert("The Username or Password is incorrect");
		    		}else if(resultFromServer == true){
		            	loadPage(myData);
		    		} 
		    	});
		    };
		});
		
		
		
		app.controller('createChannel', function($scope) {
			$scope.channelName = "";
			$scope.channelDescription = "";
			   
			$scope.nameChLength  = function() {return 30 - $scope.channelName.length;};
			$scope.ChDsLength  = function() {return 500 - $scope.channelDescription.length;};
		    $scope.createChannelBtn = function createChannelBtn(){
		    	  var channel= new Object();
		    	  channel.Name=$scope.channelName ;
		    	  channel.Description=$scope.channelDescription;
		    	  var userNameCreator=document.getElementById("addNickName");
		    	  channel.Creator=userNameCreator.innerHTML;
		    	  var sendMsg= JSON.stringify(channel);
		    	 
		      	$.post("/OTC/CreateChannelServlet",sendMsg, function(response) {
		      	    // handle response from your servlet.
		      		var mydata = response;
		      	    var resultFromServer=mydata.result;
		
		      	    if(resultFromServer == false){
		      	    	alert("The channel Name is exists , enter another channel name");	
		      	    }else if(resultFromServer == true){
		      	    	alert("Successful create channel,Thank you");
		      	    	createPublicChannelsList( mydata.channel );
		      	    	document.getElementById('homePage').style.display='';
		      	  		document.getElementById('createPage').style.display='none';
		      	    }
		      	   
		      	}); 
		    	};
		    });   
		
		
		app.controller('registration', function($scope) {
			$scope.usernameRg = "";
			$scope.passwordRg = "";
			$scope.userNicknameRg = "";
			$scope.shortDescripitionRg = "";
			   
			$scope.nameULength  = function() {return 10 - $scope.usernameRg.length;};
			$scope.passLength  = function() {return 8 - $scope.passwordRg.length;};
			$scope.nickNLength  = function() {return 20 - $scope.userNicknameRg.length;};
			$scope.disLength  = function() {return 50 - $scope.shortDescripitionRg.length;};
			$scope.registrationBtn = function registrationBtn(){
			
			
		   
		    	var user= new Object();
		    	
		    	user.UserName=$scope.usernameRg ;
		    	user.Password=$scope.passwordRg ;
		    	user.NickName=$scope.userNicknameRg;
		
		    	if($scope.shortDescripitionRg== null){
		    		user.Description ="No description";
		    	}
		    	else {
		    		user.Description =$scope.shortDescripitionRg;
		    	}
		    	if($scope.photoRg== null){
		    		user.Photo ="Photo/photo.png";
		    	}
		    	else {
		    		user.Photo =$scope.photoRg;
		    	}
		    	
		  		var sendMsg= JSON.stringify(user);
		    	$.post("/OTC/RegisterServlet",sendMsg, function(response) {
		    	    // handle response from your servlet.
		    		var mydata = response;
		    	    var resultFromServer=mydata.result;
		    	    if(resultFromServer == "exists"){
		    	    	alert("The User Name exists , enter another user name");	
		    	    }else if(resultFromServer == "registered"){
		    	    	alert("Successful registration, you can now login");
		    	    	document.getElementById('id02').style.display='none';
		    	    	var user= new Object();
		    	    	user.UserName=$scope.usernameRg ;
		    	    	user.Password=$scope.passwordRg ;
		    	    	$.post("/OTC/LoginServlet", JSON.stringify(user), function(response) {
		    	    		var myData = response;
		    	    		var resultFromServer=myData.result;
		    	    		if(resultFromServer == false){
		    	    	    	alert("The Username or Password is incorrect");
		    	    		}else if(resultFromServer == true){
		    	            	loadPage(myData);
		    	    		} 
		    	    	});
		    	    	
		    	    	
		    	    	
		    	    }
		    	});
		    };
		});

