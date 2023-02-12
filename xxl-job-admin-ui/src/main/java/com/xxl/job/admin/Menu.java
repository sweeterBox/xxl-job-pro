package com.xxl.job.admin;

/**
 * @author sweeter
 * @date 2022/11/27
 */
public class Menu {

    private String icon;

    private String code;

    private String name;

    private String url;

    private String aTartget;

    private int order;

    public Menu(String icon, String code, String name, String url, String aTartget,int order) {
        this.icon = icon;
        this.code = code;
        this.name = name;
        this.url = url;
        this.aTartget = aTartget;
        this.order = order;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getaTartget() {
        return aTartget;
    }

    public void setaTartget(String aTartget) {
        this.aTartget = aTartget;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
