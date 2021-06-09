package tech.mlsn.eatgo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 09/06/2021.
 */
public class DashboardAdminResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DashboardAdminDataResponse data;

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

    public DashboardAdminDataResponse getData() {
        return data;
    }

    public void setData(DashboardAdminDataResponse data) {
        this.data = data;
    }
}
