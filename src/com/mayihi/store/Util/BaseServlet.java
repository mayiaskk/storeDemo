package com.mayihi.store.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseServlet")
public class BaseServlet extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用反射来根据方法名调用对应的方法，就不需要ifelse判断了
        String md = req.getParameter("method");
        Class clazz = this.getClass();
        if (md == null || md.length() == 0 || md.trim().length() == 0) {
            md = "execute";
        }
        try {
            Method method = clazz.getMethod(md,HttpServletRequest.class,HttpServletResponse.class);
            if (null != method) {
                String path = (String)method.invoke(this, req, resp);
                if (null != path) {
                    req.getRequestDispatcher(path).forward(req,resp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return null;
    }
}
