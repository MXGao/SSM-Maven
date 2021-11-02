package com.mapper;

import com.domain.Order;
import com.domain.OrderItem;
import com.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    public List<Product> findByCid(String cid);

    int findTotalCount(String cid);

    List<Product> findProductListByLimit(@Param("cid") String cid, @Param("offset") int index, @Param("limit") int pageSize);

    Product findByPid(String pid);

    void addOrder(Order order);

    void addOrderItem(OrderItem orderItem);

    List<Order> findOrderByUid(String uid);

    // 这里涉及到多表查询，orderitem product
    List<OrderItem> findOrderItemByOid(String oid);



    //void addToOrderItem(OrderItem orderItem);
}
