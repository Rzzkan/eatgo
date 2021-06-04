package tech.mlsn.eatgo.response.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 04/06/2021.
 */
public class ReviewDataResponse {
    @SerializedName("id_rating")
    @Expose
    private String idRating;
    @SerializedName("id_restaurant")
    @Expose
    private String idRestaurant;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review")
    @Expose
    private String review;

    public ReviewDataResponse(String idRating, String idRestaurant, String idUser, String name, String image, String rating, String review) {
        this.idRating = idRating;
        this.idRestaurant = idRestaurant;
        this.idUser = idUser;
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.review = review;
    }

    public String getIdRating() {
        return idRating;
    }

    public void setIdRating(String idRating) {
        this.idRating = idRating;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
