package models;

import java.util.ArrayList;

public class LoginResponse {
	boolean result;
	Users user;
	ArrayList<PublicChannel> PublicChannels;
	ArrayList<PrivateChannel> PrivateChannels;
	
	public LoginResponse(boolean result, Users user, ArrayList<PublicChannel> PublicChannels,ArrayList<PrivateChannel> PrivateChannels) {
		this.result = result;
		this.user = user;
		this.PublicChannels = PublicChannels;
		this.PrivateChannels=PrivateChannels;
	}
	
	
}
