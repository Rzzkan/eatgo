package tech.mlsn.eatgo.response.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 05/06/2021.
 */
public class AllMenuDataResponse {

    @SerializedName("id_menu")
    @Expose
    private String idMenu;
    @SerializedName("id_restaurant")
    @Expose
    private String idRestaurant;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("point_earned")
    @Expose
    private String pointEarned;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    public AllMenuDataResponse(String idMenu, String idRestaurant, String name, String description, String category, String image, String price, String pointEarned, String isActive) {
        this.idMenu = idMenu;
        this.idRestaurant = idRestaurant;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.price = price;
        this.pointEarned = pointEarned;
        this.isActive = isActive;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPointEarned() {
        return pointEarned;
    }

    public void setPointEarned(String pointEarned) {
        this.pointEarned = pointEarned;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
