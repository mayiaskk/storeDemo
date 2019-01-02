package com.mayihi.store.service;

import com.mayihi.store.Util.PageModel;
import com.mayihi.store.domain.Order;
import com.mayihi.store.domain.User;

import java.util.List;

public interface OrderService {

    int saveOrderAndItems(Order order);

    PageModel showMyOrdersWithPage(User user,int page) throws Exception;

    Order queryOrderByOid(String oid) throws Exception;

    int updateOrderByOrder(Order order)throws Exception;

    PageModel showOrdersWithPage(int page)throws Exception;

    PageModel showOrdersWithPage(int page, int st)throws Exception;
}
