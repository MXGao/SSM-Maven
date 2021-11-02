package com.service;

import com.domain.Order;
import com.domain.OrderItem;
import com.domain.PageBean;
import com.domain.Product;

import java.util.List;

public interface ProductService {

    public List<Product> findByCid(String cid);

    /**
     * 封装PageBean对象
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageBean findPageBeanFirst(String cid, int currentPage, int pageSize);

    /**
     * 根据pid查找
     * @param pid
     * @return
     */
    Product findByPid(String pid);

    /**
     * 将Order和OrderItem添加到数据库
     * @param order
     */
    void submitOrder(Order order);

    /**
     * 根据uid查询出所有的order，封装到list集合中
     * @param uid
     * @return
     */
    List<Order> findOrderByUid(String uid);

    /**
     * 根据oid查找所有的订单项
     * @param oid
     * @return
     */
    List<OrderItem> findOrderItemByOid(String oid);



    /**
     * 添加到购物车
     * @param count
     * @param pid
     */
    //void addToOrderItem(int count, String pid);

}
