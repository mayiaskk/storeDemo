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

@WebFilter(filterName = "LoginFilterHelper",urlPatterns = "/*/login.jsp")
public class LoginFilterHelper implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        String rememberName=(String)session.getAttribute("rememberName");
        Cookie cookie = CookieUtils.getCookieByName(request.getCookies(),"UserName");
        String username = cookie.getValue();
        if (null == rememberName) {
            //user为空
            if (null != cookie) {
                //cookie中有用户名和密码，重新设置session中的值;
                session.setAttribute("rememberName", rememberName);
            }
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
