package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.*;
import com.mayihi.store.domain.User;
import com.mayihi.store.service.serviceImpl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "UserServlet", urlPatterns = "/UserServlet")
public class UserServlet extends BaseServlet {
    //写方法就可以了
    public String regist(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = new User();
            String birthday = request.getParameter("birthday");
            Map<String, String[]> map = request.getParameterMap();
            MyBeanUtils.populate(user, map);
//            BeanUtils.populate(user,map);
            user.setUid(UUIDUtils.getId());
            user.setState(0);
            user.setCode(UUIDUtils.getUUID64());
            int ret = new UserServiceImpl().regist(user);
            if (ret > 0) {
                request.setAttribute("tips", "注册成功，请至邮箱激活账户!");
                MailUtils.sendMail(user.getEmail(), user.getCode());
                return "/jsp/info.jsp";
            }

        } catch (Exception e) {
            request.setAttribute("tips", "注册失败，请重新注册!");
            return "/jsp/info.jsp";
        }
        request.setAttribute("tips", "注册失败，请重新注册");
        return "/jsp/info.jsp";
    }

    public String registUI(HttpServletRequest request, HttpServletResponse response) {
        return "/jsp/register.jsp";
    }

    public String active(HttpServletRequest request, HttpServletResponse response) {
        //邮件发来的code
        String code = request.getParameter("code");
        //根据上述code查询数据库中的用户
        try {
            UserServiceImpl service = new UserServiceImpl();
            String uid = service.queryUserByCode(code);
            //若查不出用户，什么都不做，若查出，将对应的用户state置1，表示已激活，同时将该code置为空
//        另外，对那些一天之内没有激活的账户数据库进行清除
            if (null != uid) {
                int success = service.activateByUid(uid);
                if (success > 0) {
                    request.setAttribute("tips", "激活成功！赶快登录吧~");
                    return "/jsp/info.jsp";
                } else {
                    request.setAttribute("tips", "激活失败！");
                    return "/jsp/info.jsp";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String login(HttpServletRequest request, HttpServletResponse response){
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String autoLogin = request.getParameter("autoLogin");
            String rememberName = request.getParameter("rememberName");
            User user = new UserServiceImpl().checkLogin(username, password);
            if (null != user) {
                //添加session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                //设置自动登录
                if ("on".equalsIgnoreCase(autoLogin)) {
                    //添加cookie，*设置过期时间7天*
                    Cookie cookie = new Cookie("UNandPW", username + "jQ#JLFKj3" + password);
                    cookie.setMaxAge(60 * 60 * 24 * 7);
                    response.addCookie(cookie);
                }
                //记住用户名
                if ("on".equalsIgnoreCase(rememberName)) {
                    //添加session属性
                    session.setAttribute("rememberName", username);
                    //添加cookie
                    Cookie cookie = new Cookie("UserName", username);
                    cookie.setMaxAge(60 * 60 * 24 * 7);
                    response.addCookie(cookie);
                }
                System.out.println(request.getContextPath());
                response.sendRedirect(request.getContextPath()+"/index.jsp");
                return null;
            }
        } catch (Exception e) {
            request.setAttribute("msg", "用户名密码错误");
            return "/jsp/login.jsp";
        }
        return "/jsp/login.jsp";
    }

    public String loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*HttpSession session = request.getSession();
        session.removeAttribute("user");*/
        request.getSession().invalidate();
        Cookie uNandPW = CookieUtils.getCookieByName(request.getCookies(), "UNandPW");
        if (uNandPW != null) {
            uNandPW.setMaxAge(0);
            response.addCookie(uNandPW);
        }
        response.sendRedirect(request.getContextPath()+"/jsp/index.jsp");
        return null;
    }


}

