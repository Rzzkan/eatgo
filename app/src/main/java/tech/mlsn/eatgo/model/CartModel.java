package tech.mlsn.eatgo.model;

/**
 * Created by Rzzkan on 03/08/2021.
 */
public class CartModel {
    int id_cart;
    String id_restaurant, id_menu ,name;
    int  qty, price, total;
    String img, desc;

    public CartModel(int id_cart, String id_restaurant, String id_menu, String name, int qty, int price, int total, String img, String desc) {
        this.id_cart = id_cart;
        this.id_restaurant = id_restaurant;
        this.id_menu = id_menu;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.img = img;
        this.desc = desc;
    }

    public int getId_cart() {
        return id_cart;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public String getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(String id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
