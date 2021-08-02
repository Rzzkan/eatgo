package tech.mlsn.eatgo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rzzkan on 30/07/2021.
 */
public class OrdersDataResponse {
    @SerializedName("id_order")
    @Expose
    private String idOrder;
    @SerializedName("id_restaurant")
    @Expose
    private String idRestaurant;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("menu_name")
    @Expose
    private List<String> menuName = null;
    @SerializedName("menu_price")
    @Expose
    private List<String> menuPrice = null;
    @SerializedName("menu_qty")
    @Expose
    private List<String> menuQty = null;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("date")
    @Expose
    private String date;

    public OrdersDataResponse(String idOrder, String idRestaurant, String idUser, String userName, String userPhone, List<String> menuName, List<String> menuPrice, List<String> menuQty, String total, String status, String payment, String date) {
        this.idOrder = idOrder;
        this.idRestaurant = idRestaurant;
        this.idUser = idUser;
        this.userName = userName;
        this.userPhone = userPhone;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuQty = menuQty;
        this.total = total;
        this.status = status;
        this.payment = payment;
        this.date = date;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public List<String> getMenuName() {
        return menuName;
    }

    public void setMenuName(List<String> menuName) {
        this.menuName = menuName;
    }

    public List<String> getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(List<String> menuPrice) {
        this.menuPrice = menuPrice;
    }

    public List<String> getMenuQty() {
        return menuQty;
    }

    public void setMenuQty(List<String> menuQty) {
        this.menuQty = menuQty;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
