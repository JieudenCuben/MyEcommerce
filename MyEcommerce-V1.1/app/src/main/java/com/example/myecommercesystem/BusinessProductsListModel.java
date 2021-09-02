package com.example.myecommercesystem;

public class BusinessProductsListModel {


    public String image_url;
    public String product_name;
    public String product_desc;
    public String item_push_id;


    public BusinessProductsListModel(String image_url, String product_name, String product_desc, String item_push_id) {
        this.image_url = image_url;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.item_push_id = item_push_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public String getItem_push_id() {
        return item_push_id;
    }
}
