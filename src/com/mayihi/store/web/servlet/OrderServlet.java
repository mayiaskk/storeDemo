package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.BaseServlet;
import com.mayihi.store.Util.PageModel;
import com.mayihi.store.Util.PaymentUtil;
import com.mayihi.store.Util.UUIDUtils;
import com.mayihi.store.dao.OrderDao;
import com.mayihi.store.domain.*;
import com.mayihi.store.service.OrderService;
import com.mayihi.store.service.serviceImpl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = "/OrderServlet")
public class OrderServlet extends BaseServlet {
    public String confirmOrderWithOid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String address = request.getParameter("address");
        String name = request.getParameter("name");
        String telephone = request.getParameter("telephone");
        String oid = request.getParameter("oid");
        String pd_FrpId = request.getParameter("pd_FrpId");
        OrderService orderService = new OrderServiceImpl();
        //address,name,telephone还需要设置
        Order order = orderService.queryOrderByOid(oid);
        order.setAddress(address);
        order.setName(name);
        order.setTelephone(telephone);
        //更新数据库信息,幂等
        orderService.updateOrderByOrder(order);
        //进入支付
        /////////////// 把付款所需要的参数准备好://////////////////
        String p0_Cmd = "Buy";
        //商户编号
        String p1_MerId = "10001126856";
        //订单编号
        String p2_Order = oid;
        //金额
        String p3_Amt = "0.01";    //不要改!这是付款的金额
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        //接受响应参数的Servlet
        String p8_Url = "http://192.168.182.128:8080"+request.getContextPath()+"/OrderServlet?method=callBack";
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        //公司的秘钥
        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

        //调用易宝的加密算法,对所有数据进行加密,返回电子签名
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

        StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
        sb.append("p0_Cmd=").append(p0_Cmd).append("&");
        sb.append("p1_MerId=").append(p1_MerId).append("&");
        sb.append("p2_Order=").append(p2_Order).append("&");
        sb.append("p3_Amt=").append(p3_Amt).append("&");
        sb.append("p4_Cur=").append(p4_Cur).append("&");
        sb.append("p5_Pid=").append(p5_Pid).append("&");
        sb.append("p6_Pcat=").append(p6_Pcat).append("&");
        sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
        sb.append("p8_Url=").append(p8_Url).append("&");
        sb.append("p9_SAF=").append(p9_SAF).append("&");
        sb.append("pa_MP=").append(pa_MP).append("&");
        sb.append("pd_FrpId=").append(pd_FrpId).append("&");
        sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
        sb.append("hmac=").append(hmac);

//        System.out.println(sb.toString());
        // 使用重定向：
        response.sendRedirect(sb.toString());

        return null;
    }

    public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OrderService orderService = new OrderServiceImpl();
        // 验证请求来源和数据有效性
        // 阅读支付结果参数说明
        System.out.println("================进入callback函数==============================");
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");

        // hmac
        String hmac = request.getParameter("hmac");
        // 利用本地密钥和加密算法 加密数据
        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        if (isValid) {
            // 有效
            if (r9_BType.equals("1")) {
                //向数据库中写入成功信息，例如状态需要更新，但首先我们要在OrderServlet中写好更新数据库的信息
                Order order = orderService.queryOrderByOid(r6_Order);
                order.setState(OrderDao.PaidUnfilled);
                orderService.updateOrderByOrder(order);
                // 浏览器重定向
                request.setAttribute("tips","恭喜您，支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
            } else if (r9_BType.equals("2")) {
                // 修改订单状态:
                // 服务器点对点，来自于易宝的通知
                request.setAttribute("tips","收到易宝通知，修改订单状态！");
                // 回复给易宝success，如果不回复，易宝会一直通知
                response.getWriter().print("success");
            }
        } else {
            throw new RuntimeException("数据被篡改！");
        }
        return "/jsp/info.jsp";
    }

    public String submitOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //创建一个订单order对象，
        Order order = new Order();
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        User user = (User) session.getAttribute("user");
        List<OrderItem> list = new ArrayList<>();
        // 遍历创建购物项对象cartItem，将购物项对象添加到订单中
        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemid(UUIDUtils.getId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotal(item.getSubTotal());
            orderItem.setProduct(item.getProduct());
            orderItem.setOrder(order);
            list.add(orderItem);
        }
        order.setOid(UUIDUtils.getId());
        order.setOrdertime(new Date());
        order.setTotal(cart.getTotal());//
        order.setState(1);
        order.setUser(user);
        order.setOrderItems(list);
        //将订单持久化，存到数据库中，将订单项持久化存到数据库中==》这两个是一个事务，任何步骤出异常都要回滚，所以service层和dao层的异常都需要往上抛，在这里统一捕获
        int i = 0;
        i = new OrderServiceImpl().saveOrderAndItems(order);
        //将订单order放在session中?    request中
        session.removeAttribute("cart");
        if (i == 1) {
            req.setAttribute("order", order);  //!
            return "/jsp/order_info.jsp";
        } else {
            return "/jsp/cart.jsp";
        }
    }

    public String showMyOrders(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = (User) req.getSession().getAttribute("user");
        int page = Integer.parseInt(req.getParameter("num"));
        PageModel pageModel = new OrderServiceImpl().showMyOrdersWithPage(user, page);
        req.setAttribute("page", pageModel);
        return "/jsp/order_list.jsp";
    }

    //payOrder
    public String payOrderWithOid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String oid = req.getParameter("oid");
        OrderService orderService = new OrderServiceImpl();
        //从数据库查出order在order_info显示
        Order order = orderService.queryOrderByOid(oid);
        req.setAttribute("order", order);  //!
        return "/jsp/order_info.jsp";
    }

    public String updateOrderState(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String oid = req.getParameter("oid");
        int state = Integer.parseInt(req.getParameter("state"));
        OrderService orderService = new OrderServiceImpl();
        Order order = orderService.queryOrderByOid(oid);
        order.setState(state);
        orderService.updateOrderByOrder(order);
        resp.sendRedirect(req.getContextPath()+"/OrderServlet?method=showMyOrders&num=1");
        return null;
    }


}