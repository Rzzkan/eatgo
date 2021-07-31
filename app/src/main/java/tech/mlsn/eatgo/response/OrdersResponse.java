package tech.mlsn.eatgo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rzzkan on 30/07/2021.
 */
public class OrdersResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<OrdersDataResponse> data = null;

    public OrdersResponse(Integer success, String message, ArrayList<OrdersDataResponse> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

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

    public ArrayList<OrdersDataResponse> getData() {
        return data;
    }

    public void setData(ArrayList<OrdersDataResponse> data) {
        this.data = data;
    }
}
