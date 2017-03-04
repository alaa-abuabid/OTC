package models;

public class CreatePrivateResponse {
	boolean result;
	private PrivateChannel channel;
	

	public CreatePrivateResponse(boolean result, PrivateChannel channel) {
		this.result = result;
		this.channel = channel;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public PrivateChannel getChannel() {
		return channel;
	}
	public void setChannel(PrivateChannel channel) {
		this.channel = channel;
	}
	

}
