package tech.mlsn.eatgo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 29/07/2021.
 */
public class PointDataResponse {

    @SerializedName("point")
    @Expose
    private String point;

    public PointDataResponse(String point) {
        this.point = point;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
