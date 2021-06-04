package tech.mlsn.eatgo.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rzzkan on 03/06/2021.
 */
public class UserDataResponse {
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("id_restaurant")
    @Expose
    private String idRestaurant;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("role")
    @Expose
    private String role;

    public UserDataResponse(String idUser, String idRestaurant, String name, String username, String phone, String password, String image, String role) {
        this.idUser = idUser;
        this.idRestaurant = idRestaurant;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.role = role;
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
