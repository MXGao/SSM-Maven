package com.service.impl;

import com.domain.Order;
import com.domain.OrderItem;
import com.domain.PageBean;
import com.domain.Product;
import com.mapper.ProductMapper;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> findByCid(String cid) {
        List<Product> productList = productMapper.findByCid(cid);
        return productList;
    }

    @Override
    public PageBean findPageBeanFirst(String cid, int currentPage, int pageSize) {
        // 封装PageBean对象
        PageBean pageBean = new PageBean();
        // 1. 总条目数，totalCount
        int totalCount = productMapper.findTotalCount(cid);
        pageBean.setTotalCount(totalCount);
        // 2. 总页数，totalPage
        int totalPage = (totalCount % pageSize ==0) ? (totalCount/pageSize):((totalCount/pageSize)+1);
        pageBean.setTotalPage(totalPage);
        // 3. 当前页
        pageBean.setCurrentPage(currentPage);
        // 4. 每页显示的条数
        pageBean.setPageSize(pageSize);
        // 5. Product对象集合，先计算从哪一页查找
        int index = (currentPage-1)*pageSize;
        List<Product> productList = productMapper.findProductListByLimit(cid,index,pageSize);
        pageBean.setProductList(productList);

        return pageBean;
    }

    /**
     * 根据pid查找
     * @param pid
     * @return
     */
    @Override
    public Product findByPid(String pid) {
        Product product = productMapper.findByPid(pid);
        return product;
    }

    /**
     * 将Order和OrderItem添加到数据库
     * @param order
     */
    @Override
    public void submitOrder(Order order) {
        // 将order添加到order表中
        productMapper.addOrder(order);
        // 从order中取出orderItem的list集合，一一往orderitem表中
        for (OrderItem orderItem:order.getOrderItems()){
            productMapper.addOrderItem(orderItem);
        }
    }

    /**
     * 根据uid查询出所有的order，封装到list集合中
     * @param uid
     * @return
     */
    @Override
    public List<Order> findOrderByUid(String uid) {
        List<Order> orderList = productMapper.findOrderByUid(uid);
        return orderList;
    }

    /**
     * 根据oid查找所有的订单项
     * @param oid
     * @return
     */
    @Override
    public List<OrderItem> findOrderItemByOid(String oid) {
        List<OrderItem> orderItemList = productMapper.findOrderItemByOid(oid);
        return orderItemList;
    }

    /**
     * 添加商品到购物车
     * @param count
     * @param pid
     */
    /*@Override
    public void addToOrderItem(int count, String pid) {
        OrderItem orderItem = new OrderItem();

        // 根据pid查询product
        Product product = productMapper.findByPid(pid);
        // 计算总价格
        double subtotal = product.getShop_price()*count;

        orderItem.setCount(count);
        orderItem.setSubtotal(subtotal);
        orderItem.setProduct(product);

        productMapper.addToOrderItem(orderItem);
    }*/
}
