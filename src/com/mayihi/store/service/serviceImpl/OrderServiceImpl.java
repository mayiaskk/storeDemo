package com.mayihi.store.service.serviceImpl;

import com.mayihi.store.Util.JDBCUtils;
import com.mayihi.store.Util.PageModel;
import com.mayihi.store.dao.OrderDao;
import com.mayihi.store.dao.ProductDao;
import com.mayihi.store.dao.daoImpl.OrderDaoImpl;
import com.mayihi.store.domain.Order;
import com.mayihi.store.domain.OrderItem;
import com.mayihi.store.domain.User;
import com.mayihi.store.service.OrderService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    OrderDao orderDao = new OrderDaoImpl();

    /**
     * @param order
     * @return 1:没有异常   0：出现异常
     */
    @Override
    public int saveOrderAndItems(Order order) {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            JDBCUtils.startTransaction();
            orderDao.saveOrder(conn,order);
            for (OrderItem orderItem : order.getOrderItems()) {
                orderDao.saveOrderItems(conn,orderItem);
            }
            JDBCUtils.commitAndClose();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            JDBCUtils.rollbackAndClose();
            return 0;
        }finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public PageModel showMyOrdersWithPage(User user, int page) throws Exception {
        //从数据库中查询一些数据存放在PageModel中
        //order集合，order中包含一些orderItem，orderItem中包含product信息，order信息不用存放，不关联也是可以的
        PageModel pageModel = orderDao.showMyOrdersWithPage(user, page);
        return pageModel;
    }

    @Override
    public Order queryOrderByOid(String oid) throws Exception {
        return orderDao.queryOrderByOid(oid);
    }

    @Override
    public int updateOrderByOrder(Order order) throws Exception {
        return orderDao.updateOrderByOrder(order);
    }



    @Override
    public PageModel showOrdersWithPage(int page) throws Exception {
        int totalRecords = orderDao.queryOrdersCount();
        PageModel pageModel = new PageModel(page,totalRecords, ProductDao.PageSize);
        List<Order> list = orderDao.showOrdersWithPage(page);
        pageModel.setRecords(list);
//        OrderManageServlet?method=showOrdersWithPage&num=1
        pageModel.setUrl("OrderManageServlet?method=showOrdersWithPage");
        return pageModel;
    }

    @Override
    public PageModel showOrdersWithPage(int page, int st) throws Exception {
        int totalRecords = orderDao.queryOrdersCount(st);
        PageModel pageModel = new PageModel(page,totalRecords, ProductDao.PageSize);
        List<Order> list = orderDao.showOrdersWithPage(page,st);
        pageModel.setRecords(list);
//        OrderManageServlet?method=showOrdersWithPage&num=1
        pageModel.setUrl("OrderManageServlet?method=showOrdersWithPage&state="+st);
        return pageModel;
    }

}
