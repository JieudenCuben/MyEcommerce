package com.example.myecommercesystem;

public class AllProductsListModel {


    public String image_url;
    public String product_name;
    public String product_desc;
    public String business_uid;


    public AllProductsListModel(String image_url, String product_name, String product_desc, String business_uid) {
        this.image_url = image_url;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.business_uid = business_uid;
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

    public String getBusiness_uid() {
        return business_uid;
    }
}
