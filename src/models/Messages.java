package models;

import java.sql.Timestamp;

public class Messages {
	private String ID;
	private String Sender;
	private String Channel;
	private String Message;
	private Timestamp Date_Time;
	private String Parent;
	private String Photo;

	
	public Messages(String ID,String Sender, String Channel, String Message, Timestamp Date_Time, String Parent){
		this.ID=ID;
		this.Sender=Sender;
		this.Channel=Channel;
		this.Message=Message;
		this.Date_Time=Date_Time;
		this.Parent=Parent;
	}
	public Messages(String ID,String Sender, String Channel, String Message, Timestamp Date_Time, String Parent,String Photo){
		this.ID=ID;
		this.Sender=Sender;
		this.Channel=Channel;
		this.Message=Message;
		this.Date_Time=Date_Time;
		this.Parent=Parent;
		this.Photo=Photo;
	}
	
	public Messages(String ID,String Sender, String Channel, String Message, String Parent,String Photo){
		this.ID=ID;
		this.Sender=Sender;
		this.Channel=Channel;
		this.Message=Message;
		this.Parent=Parent;
		this.Photo=Photo;
	}
	
	public Messages(String ID,String Sender, String Channel, String Message, String Parent){
		this.ID=ID;
		this.Sender=Sender;
		this.Channel=Channel;
		this.Message=Message;
		this.Parent=Parent;
	}
	
	


	
	public String getPhoto() {
		return Photo;
	}



	public void setPhoto(String photo) {
		Photo = photo;
	}



	public String getID() {
		return ID;
	}


	public void setID(String iD) {
		ID = iD;
	}


	public String getSender() {
		return Sender;
	}


	public void setSender(String sender) {
		Sender = sender;
	}


	public String getChannel() {
		return Channel;
	}


	public void setChannel(String channel) {
		Channel = channel;
	}


	public String getMessage() {
		return Message;
	}


	public void setMessage(String message) {
		Message = message;
	}


	public Timestamp getDate_Time() {
		return Date_Time;
	}


	public void setDate_Time(Timestamp date_Time) {
		Date_Time = date_Time;
	}


	public String getParent() {
		return Parent;
	}


	public void setParent(String parent) {
		Parent = parent;
	}
	
	

	
}
