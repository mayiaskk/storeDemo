package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.BaseServlet;
import com.mayihi.store.Util.PageModel;
import com.mayihi.store.domain.Order;
import com.mayihi.store.domain.OrderItem;
import com.mayihi.store.service.OrderService;
import com.mayihi.store.service.serviceImpl.OrderServiceImpl;
import net.sf.json.JSONArray;
import org.ietf.jgss.Oid;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderManageServlet", urlPatterns = "/OrderManageServlet")
public class OrderManageServlet extends BaseServlet {
    OrderService orderService = new OrderServiceImpl();

    //    showOrdersWithPage
    public String showOrdersWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int page = Integer.parseInt(request.getParameter("num"));
        String state = request.getParameter("state");
        PageModel pageModel = null;
        if (null == state || "".equals(state)) {
            pageModel = orderService.showOrdersWithPage(page);
        } else {
            int st = Integer.parseInt(state);
            pageModel = orderService.showOrdersWithPage(page, st);
        }
        request.setAttribute("page", pageModel);
        return "/admin/order/list.jsp";
    }

    //    showOrderDetail
    public String showOrderDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String oid = request.getParameter("oid");
        Order order = orderService.queryOrderByOid(oid);
        List<OrderItem> orderItems = order.getOrderItems();
        JSONArray jsonArray = JSONArray.fromObject(orderItems);
        String jsonStr = jsonArray.toString();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(jsonStr);
        return null;
    }

    //    updateOrderState
    public String updateOrderState(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String oid = request.getParameter("oid");
        Order order = orderService.queryOrderByOid(oid);
        order.setState(3);
        orderService.updateOrderByOrder(order);
        response.sendRedirect(request.getContextPath()+"/OrderManageServlet?method=showOrdersWithPage&state=3&num=1");
        return null;
    }


}
