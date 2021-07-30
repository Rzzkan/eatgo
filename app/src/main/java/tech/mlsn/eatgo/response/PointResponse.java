package tech.mlsn.eatgo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 29/07/2021.
 */
public class PointResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private PointDataResponse data;

    public PointResponse(Integer success, String message, PointDataResponse data) {
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

    public PointDataResponse getData() {
        return data;
    }

    public void setData(PointDataResponse data) {
        this.data = data;
    }
}
