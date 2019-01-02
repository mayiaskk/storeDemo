package com.mayihi.store.web.filter;

import com.mayihi.store.Util.CookieUtils;
import com.mayihi.store.domain.User;
import com.mayihi.store.service.serviceImpl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //判断session中有没有user
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cookie cookie = CookieUtils.getCookieByName(request.getCookies(),"UNandPW");
        if (null == user) {
            //user为空
            if (null != cookie) {
                //cookie中有用户名和密码，重新设置session中的值;
                String[] UNandPW = cookie.getValue().split("jQ#JLFKj3");
                String username = UNandPW[0];
                String password = UNandPW[1];
                try {
                    user = new UserServiceImpl().checkLogin(username, password);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                session.setAttribute("user", user);
                chain.doFilter(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {
    }

}
