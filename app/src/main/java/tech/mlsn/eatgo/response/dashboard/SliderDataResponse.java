package tech.mlsn.eatgo.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 03/06/2021.
 */
public class SliderDataResponse {
    @SerializedName("id_slider")
    @Expose
    private String idSlider;
    @SerializedName("image")
    @Expose
    private String image;

    public SliderDataResponse(String idSlider, String image) {
        this.idSlider = idSlider;
        this.image = image;
    }

    public String getIdSlider() {
        return idSlider;
    }

    public void setIdSlider(String idSlider) {
        this.idSlider = idSlider;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
