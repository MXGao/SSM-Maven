package com.domain;

import java.io.Serializable;

/**
 * 单个商品的订单
 */
public class OrderItem implements Serializable {
    private String itemid;// 需要用工具类随机创建一串数字
    private int count;// 商品数量
    private double subtotal;// 总价钱
    private Product product;// 商品信息，包括pid

    private Order order;// 订单

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "itemid='" + itemid + '\'' +
                ", count=" + count +
                ", subtotal=" + subtotal +
                ", product=" + product +
                ", order=" + order +
                '}';
    }
}
