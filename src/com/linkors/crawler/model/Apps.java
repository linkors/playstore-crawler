package com.linkors.crawler.model;

/**
 * Created by SRIN on 6/7/2018.
 */
public class Apps {

    private String name;
    private String packageName;
    private String icon;
    private String star;
    private String price;

    public Apps(String name, String packageName, String icon, String star, String price) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.star = star;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
