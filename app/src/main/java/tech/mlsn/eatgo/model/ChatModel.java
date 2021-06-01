package tech.mlsn.eatgo.model;

import java.io.Serializable;

public class ChatModel implements Serializable{
	private long id;
	private String date;
	private String content;
	private boolean fromMe;
	private boolean showTime = true;

	public ChatModel(long id, String content, boolean fromMe, String date) {
		this.id = id;
		this.date = date;
		this.content = content;
		this.fromMe = fromMe;
	}

	public ChatModel(long id, String content, boolean fromMe, boolean showTime, String date) {
		this.id = id;
		this.date = date;
		this.content = content;
		this.fromMe = fromMe;
		this.showTime = showTime;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public boolean isFromMe() {
		return fromMe;
	}

	public boolean isShowTime() {
		return showTime;
	}
}