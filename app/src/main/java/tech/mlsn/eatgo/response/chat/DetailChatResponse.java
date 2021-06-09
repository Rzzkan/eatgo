package tech.mlsn.eatgo.response.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rzzkan on 09/06/2021.
 */
public class DetailChatResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<DetailChatDataResponse> data = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<DetailChatDataResponse> getData() {
        return data;
    }

    public void setData(ArrayList<DetailChatDataResponse> data) {
        this.data = data;
    }
}
