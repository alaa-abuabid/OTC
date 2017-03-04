package models;

import java.util.ArrayList;

public class SearchResponse {

	boolean result;
	ArrayList<PublicChannel> ChannelNames;
	
	
	
	public SearchResponse(boolean result, ArrayList<PublicChannel> channelNames) {
		super();
		this.result = result;
		ChannelNames = channelNames;
	}
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public ArrayList<PublicChannel> getChannelNames() {
		return ChannelNames;
	}
	public void setChannelNames(ArrayList<PublicChannel> channelNames) {
		ChannelNames = channelNames;
	}
	
	
}
