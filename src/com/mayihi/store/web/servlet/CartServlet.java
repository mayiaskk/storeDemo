package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.BaseServlet;
import com.mayihi.store.domain.Cart;
import com.mayihi.store.domain.CartItem;
import com.mayihi.store.domain.Product;
import com.mayihi.store.service.serviceImpl.ProductServiceImpl;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CartServlet", urlPatterns = "/CartServlet")
public class CartServlet extends BaseServlet {

    public String addToCart(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        //购物车放在session中，首先从session中获取购物车，如果没有，则创建一个，如果有，则将pid对应的CartItem加入购物车
        HttpSession session = request.getSession();
        Product product = null;
        CartItem cartItem = new CartItem();
        Cart cart = (Cart) session.getAttribute("cart");
        if (null == cart) {
            //，没有购物车，创建一个
            cart = new Cart();
        }
        try {
            product = new ProductServiceImpl().queryProductByPid(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cartItem.setProduct(product);
        cart.addItemToCart(cartItem, quantity + cartItem.getQuantity());
        session.setAttribute("cart", cart);
        try {
            response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String delCartItem(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        cart.delItemFromCartByPid(pid);
        session.setAttribute("cart", cart);
        try {
            response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String clearCart(HttpServletRequest request, HttpServletResponse response) {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart != null) {
            cart.clearCart();
        }
        try {
            response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}