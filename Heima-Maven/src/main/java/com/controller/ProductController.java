package com.controller;

import com.domain.*;
import com.service.ProductService;
import com.utils.CommonsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @ResponseBody
    @RequestMapping("/productShow")
    public ModelAndView productShowFirst(String cid, HttpServletRequest request){
        // 获取当前页(第一次没有，默认为0)，和每页展示的条数（默认为6）
        String currentPageStr = request.getParameter("currentPage");// 注意，获取到的时字符串
        String pageSizeStr = request.getParameter("pageSize");
        int currentPage = 1;
        if (currentPageStr!=null && currentPageStr.length()>0){
            // 表示传入了当前页，不是第一次
            currentPage = Integer.parseInt(currentPageStr);
        }
        int pageSize = 12;
        if (pageSizeStr!=null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }
        // 调用service中的方法，获取到PageBean对象（封装有List<Product>）
        PageBean pageBean = productService.findPageBeanFirst(cid,currentPage,pageSize);
        System.out.println(pageBean.toString());

        //List<Product> productList = productService.findByCid(cid);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageBean",pageBean);
        modelAndView.setViewName("product_list");

        return modelAndView;
    }

    /**
     * 根据pid查找单个商品
     * @param pid
     * @return
     */
    @ResponseBody
    @RequestMapping("/findOne")
    public ModelAndView findOne(String pid){
        Product product = productService.findByPid(pid);
        System.out.println(product);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product",product);
        modelAndView.setViewName("product_info");
        return modelAndView;
    }
    private Map<String,CartItem> cartItemMap = new HashMap<String,CartItem>();

    @RequestMapping("/addCart")
    public String addCart(int count, String pid, HttpSession session){
        //System.out.println(count+"::::"+pid);
        // 先判断有没有登录
        User user = (User) session.getAttribute("user");
        if (user==null){
            // 表示没有登录，跳转到登录页面
            return "redirect:/login.jsp";
        }
        // 封装CartItem
        CartItem cartItem = new CartItem();
        Product product = productService.findByPid(pid);
        cartItem.setCount(count);
        cartItem.setProduct(product);
        cartItem.setSubtotal(product.getShop_price()*count);

        // productService.addToOrderItem(count,pid);
        // 从session中取出购物车cart，第一次肯定没有，就存入
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart==null){
            // 表示没有cart，就创建一个
            cart = new Cart();
        }
        // 获取cartItemMap，和total
        // 这里，不能创建map，这样每次都会是新的集合，就不能和前面的pid一起添加

        //List<Map<String,CartItem>> cartItemMapList = new ArrayList<Map<String,CartItem>>();
        double newSubTotal = 0.0;
        // 判断集合中是否有以pid为键的值，有则将数量相加，没有则直接加入
        if (cartItemMap.containsKey(pid)){
            // 表示有，将数量相加后，重新设置到CartItem中
            int oldCount = cartItemMap.get(pid).getCount();
            int newCount = oldCount + count;
            cartItemMap.get(pid).setCount(newCount);
            // 价格重新计算
            newSubTotal = cartItemMap.get(pid).getProduct().getShop_price() * newCount;
            cartItemMap.get(pid).setSubtotal(newSubTotal);
        }else {
            // 表示没有该商品，直接添加
            cartItemMap.put(pid,cartItem);
            newSubTotal = cartItem.getSubtotal();
        }
        // 将cartItemMap，和total设置到cart中
        cart.setCartItemMap(cartItemMap);
        double total = cart.getTotal() + newSubTotal;
        cart.setTotal(total);

        //cartItemMapList.add(cartItemMap);
        //cart.setCartItemMapList(cartItemMapList);
        System.out.println(cart);

        //将cart存到session中，再跳转到cart.jsp页面
        session.setAttribute("cart",cart);

        return "redirect:/cart.jsp";
    }

    /**
     * 根据pid删除购物车
     * @param pid
     * @return
     */
    @RequestMapping("/delCartByPid")
    public String delCartByPid(String pid,HttpSession session){
        // 从Session中获取购物车cart
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart!=null){
            // 根据pid从成员变量map集合中移除对应pid的键值对，同时还需要改变总价钱
            double total = cart.getTotal()-cartItemMap.get(pid).getSubtotal();
            cart.setTotal(total);
            cartItemMap.remove(pid);
            cart.setCartItemMap(cartItemMap);// 将集合重新设置到cart中
        }
        session.setAttribute("cart",cart);// 更新session域中的cart

        return "redirect:/cart.jsp";
    }

    /**
     * 清空购物车
     * @return
     */
    @RequestMapping("/clearCart")
    public String clearCart(HttpSession session){
        session.removeAttribute("cart");
        // 因为map定义在成员变量位置，所以这里没有删除map集合中的内容
        cartItemMap.clear();
        return "redirect:/cart.jsp";
    }

    /**
     * 提交订单
     * @return
     */
    @RequestMapping("/submitOrder")
    public String submitOrder(HttpSession session){
        // 先判断是否登录
        User user = (User) session.getAttribute("user");
        if (user==null){
            return "redirect:/login.jsp";
        }
        // 获取session中的cart
        Cart cart = (Cart) session.getAttribute("cart");
        // 封装Order
        Order order = new Order();
        order.setOid(CommonsUtils.getUUID());// 随机生成订单号
        order.setOrdertime(new Date());// 下单时间
        order.setTotal(cart.getTotal());// 总金额
        order.setState(0);// 设置状态，现在还没有付款
        // 收款地址，收款人，电话号码都还未知
        order.setAddress(null);
        order.setName(null);
        order.setTelephone(null);
        // 订单属于哪个用户
        order.setUser(user);// uid
        // OrderItem订单项集合 List<OrderItem> orderItems，每一个订单项对应购物车项
        // 遍历cartItemMap集合 Map<String,CartItem> cartItemMap = new HashMap<String,CartItem>()，分别将每一个购物栏设置到订单项中
        for(Map.Entry<String,CartItem> entry:cartItemMap.entrySet()){
            // 获取购物项
            CartItem cartItem = entry.getValue();
            // 封装OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setItemid(CommonsUtils.getUUID());// 随机生成id
            orderItem.setCount(cartItem.getCount());// 单种商品的数量
            orderItem.setSubtotal(cartItem.getSubtotal());// 单种商品总价钱
            orderItem.setProduct(cartItem.getProduct());// 商品对象
            orderItem.setOrder(order);// 商品订单
            // 将订单项一一设置到Order的orderItems集合中
            order.getOrderItems().add(orderItem);
        }
        // 封装完Order后，传递到service层，mapper中存到数据库中
        productService.submitOrder(order);

        // 将Order存到session域中
        session.setAttribute("order",order);

        return "redirect:/order_info.jsp";
    }

    /**
     * 查询某一个用户的所有订单
     * @return
     */
    @RequestMapping("/myOrders")
    public String myOrders(HttpSession session){
        // 先判断是否登录
        User user = (User) session.getAttribute("user");
        if (user==null){
            return "redirect:/login.jsp";
        }
        // 获取用户uid，根据uid查询出该用户的所有订单order
        String uid = user.getUid();
        List<Order> orderList = productService.findOrderByUid(uid);
        // 这种查询并没有查询出orderItems属性，需要单独封装
        if (orderList!=null){
            for (Order order:orderList){
                // 根据oid查询出订单项集合，再遍历集合封装每一个订单项
                String oid = order.getOid();
                List<OrderItem> orderItemList = productService.findOrderItemByOid(oid);
                for (OrderItem orderItem:orderItemList){
                    order.getOrderItems().add(orderItem);
                }
            }
        }
        // 将orderList存到session中
        session.setAttribute("orderList",orderList);
        for (Order order: orderList){
            System.out.println(order);
        }

        return "redirect:/order_list.jsp";
    }
}
