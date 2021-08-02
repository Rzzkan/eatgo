package tech.mlsn.eatgo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 03/08/2021.
 */
public class DetailOrderResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private OrdersDataResponse data;

    public DetailOrderResponse(Integer success, String message, OrdersDataResponse data) {
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

    public OrdersDataResponse getData() {
        return data;
    }

    public void setData(OrdersDataResponse data) {
        this.data = data;
    }
}
