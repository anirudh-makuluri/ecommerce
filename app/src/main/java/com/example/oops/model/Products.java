package com.example.oops.model;

public class Products
{
    private String pname,desc="String",price,image,date,time,pid,category,retailername,stock;

    public Products()
    {

    }

    public Products(String pname, String desc, String price, String image, String date, String time, String pid, String category,String retailername,String stock) {
        this.pname = pname;
        this.desc = desc;
        this.price = price;
        this.image = image;
        this.date = date;
        this.time = time;
        this.pid = pid;
        this.category = category;
        this.retailername=retailername;
        this.stock=stock;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getRetailername() {
        return retailername;
    }

    public void setRetailername(String retailername) {
        this.retailername = retailername;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
