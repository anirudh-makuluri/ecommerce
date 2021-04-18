package com.example.oops.model;

public class Cart {
    private String pid,pname,price,quantity,discount,retailername;
    public Cart()
    {

    }

    public Cart(String pid, String pname, String price, String quantity, String discount,String retailername) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.retailername=retailername;
    }

    public String getRetailername() {
        return retailername;
    }

    public void setRetailername(String retailername) {
        this.retailername = retailername;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
