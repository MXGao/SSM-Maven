package com.domain;

import java.util.List;
import java.util.Map;

public class Cart {

    private Map<String,CartItem> cartItemMap ;// 将每一种商品的购物条按pid为键，本身为值，存入到map集合中

    private double total;// 总价钱

    //private List<Map<String,CartItem>> cartItemMapList;

    /*public List<Map<String, CartItem>> getCartItemMapList() {
        return cartItemMapList;
    }

    public void setCartItemMapList(List<Map<String, CartItem>> cartItemMapList) {
        this.cartItemMapList = cartItemMapList;
    }*/

    public Map<String, CartItem> getCartItemMap() {
        return cartItemMap;
    }

    public void setCartItemMap(Map<String, CartItem> cartItemMap) {
        this.cartItemMap = cartItemMap;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartItemMap=" + cartItemMap +
                ", total=" + total +
                '}';
    }
}
