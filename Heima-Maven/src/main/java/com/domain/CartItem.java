package com.domain;

import java.io.Serializable;

/**
 * 某一种商品的购物项
 */
public class CartItem implements Serializable {

    private int count;// 数量
    private Product product;// 商品信息
    private double subtotal;// 总价格

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "count=" + count +
                ", product=" + product +
                ", subtotal=" + subtotal +
                '}';
    }
}
