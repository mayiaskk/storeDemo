package com.mayihi.store.dao;

import com.mayihi.store.Util.PageModel;
import com.mayihi.store.domain.Order;
import com.mayihi.store.domain.OrderItem;
import com.mayihi.store.domain.User;

import java.sql.Connection;
import java.util.List;

public interface OrderDao {

    int PageSize = 3;
    /**
     * 未付款
     */
    int NotPaid = 1;
    /**
     * 已付款未发货
     */
    int PaidUnfilled = 2;
    /**
     * 未签收
     */
    int NotSign = 3;
    /**
     * 已签收，结束
     */
    int Received = 4;

    void saveOrder(Connection conn, Order order) throws Exception;

    void saveOrderItems(Connection conn, OrderItem orderItem) throws Exception;

    PageModel showMyOrdersWithPage(User user, int page)throws Exception;

    List<OrderItem> queryOrderItemsByOid(String oid) throws Exception;

    Order queryOrderByOid(String oid)throws Exception;

    int updateOrderByOrder(Order order)throws Exception;

    int updateOrderStateByOid(String oid,int state)throws Exception;

    int queryOrdersCount()throws Exception;

    List<Order> showOrdersWithPage(int page)throws Exception;

    List<Order> showOrdersWithPage(int page, int st)throws Exception;

    int queryOrdersCount(int st)throws Exception;
}
