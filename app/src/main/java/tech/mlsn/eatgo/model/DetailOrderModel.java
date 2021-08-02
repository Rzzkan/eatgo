package tech.mlsn.eatgo.model;

/**
 * Created by Rzzkan on 03/08/2021.
 */
public class DetailOrderModel {
    String menu, price, qty;

    public DetailOrderModel(String menu, String price, String qty) {
        this.menu = menu;
        this.price = price;
        this.qty = qty;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
