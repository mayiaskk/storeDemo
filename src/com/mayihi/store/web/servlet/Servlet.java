package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.BaseServlet;
import com.mayihi.store.domain.Product;
import com.mayihi.store.service.serviceImpl.ProductServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "Servlet", urlPatterns = "/Servlet")
public class Servlet extends BaseServlet {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProductServiceImpl productService = new ProductServiceImpl();
        List<Product> newProducts = productService.queryNewProducts();
        List<Product> hotProducts = productService.queryHotProducts();
        request.setAttribute("news", newProducts);
        request.setAttribute("hots", hotProducts);

        return "/jsp/index.jsp";
    }


}
