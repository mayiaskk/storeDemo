package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.BaseServlet;
import com.mayihi.store.domain.CategoryPageBean;
import com.mayihi.store.domain.Product;
import com.mayihi.store.service.serviceImpl.ProductServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet(name = "ProductServlet",urlPatterns = "/ProductServlet")
public class ProductServlet extends BaseServlet {

    public String list(HttpServletRequest request, HttpServletResponse response) {
        //哪个类别？多少页？
        String cid = request.getParameter("cid");
        int page = Integer.parseInt(request.getParameter("page"));
        //查出这个类别的所有商品，根据一页的数量查出需要显示的商品list返回页面
        //下次查询商品时也是这个方法，只需改变类别id和页数即可
        ProductServiceImpl service = new ProductServiceImpl();
        CategoryPageBean pageBean = null;
        try {
            pageBean = service.queryProductsByCid(cid, page);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //将list加入session，跳转到product_list.jsp页面
        request.setAttribute("pageBean", pageBean);
        //list为对应类别的相应页面的商品，我们还需要总的页数，当前页数。


        return "/jsp/product_list.jsp";
    }

    public String productInfo(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        try {
            Product product = new ProductServiceImpl().queryProductByPid(pid);
            request.setAttribute("product", product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/jsp/product_info.jsp";
    }
}
