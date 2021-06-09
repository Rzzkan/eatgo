package tech.mlsn.eatgo.response.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 09/06/2021.
 */
public class DetailChatDataResponse {
    @SerializedName("id_chat")
    @Expose
    private String idChat;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("to_name")
    @Expose
    private String toName;
    @SerializedName("to_image")
    @Expose
    private String toImage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("date")
    @Expose
    private String date;

    public DetailChatDataResponse(String idChat, String from, String to, String toName, String toImage, String message, String date) {
        this.idChat = idChat;
        this.from = from;
        this.to = to;
        this.toName = toName;
        this.toImage = toImage;
        this.message = message;
        this.date = date;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToImage() {
        return toImage;
    }

    public void setToImage(String toImage) {
        this.toImage = toImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


