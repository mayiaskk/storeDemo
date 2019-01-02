package com.mayihi.store.web.filter;

import com.mayihi.store.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = " PrivilegeFilter",urlPatterns = {"/OrderServlet","/jsp/order_list.jsp","/jsp/order_info.jsp"})
public class PrivilegeFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User user = (User) request.getSession().getAttribute("user");
        if (null != user) {
            chain.doFilter(req, resp);
        } else {
            request.setAttribute("tips","请登陆后查看！");
            request.getRequestDispatcher("/jsp/info.jsp").forward(request,response);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
