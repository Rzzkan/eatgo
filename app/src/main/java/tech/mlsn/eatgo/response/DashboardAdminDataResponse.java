package tech.mlsn.eatgo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 09/06/2021.
 */
public class DashboardAdminDataResponse {
    @SerializedName("total_user")
    @Expose
    private Integer totalUser;
    @SerializedName("total_resto")
    @Expose
    private Integer totalResto;
    @SerializedName("total_review")
    @Expose
    private Integer totalReview;
    @SerializedName("total_menu")
    @Expose
    private Integer totalMenu;

    public Integer getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Integer totalUser) {
        this.totalUser = totalUser;
    }

    public Integer getTotalResto() {
        return totalResto;
    }

    public void setTotalResto(Integer totalResto) {
        this.totalResto = totalResto;
    }

    public Integer getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(Integer totalReview) {
        this.totalReview = totalReview;
    }

    public Integer getTotalMenu() {
        return totalMenu;
    }

    public void setTotalMenu(Integer totalMenu) {
        this.totalMenu = totalMenu;
    }
}
