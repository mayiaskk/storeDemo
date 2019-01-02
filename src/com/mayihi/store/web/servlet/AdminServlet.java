package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.*;
import com.mayihi.store.domain.Category;
import com.mayihi.store.service.CategoryService;
import com.mayihi.store.service.serviceImpl.CategoryServiceImpl;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = "/AdminServlet")
public class AdminServlet extends BaseServlet {
    CategoryService categoryService = new CategoryServiceImpl();

    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if ("admin".equals(username) && "admin".equals(password)) {
            request.getSession().setAttribute("admin",username+"#$#J3KE1"+password);
            return "/admin/home.jsp";
        }
        request.setAttribute("msg", "管理员用户名密码错误！");
        return "/admin/index.jsp";
    }

    //getAllCats
    public String getAllCatsWithPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = Integer.parseInt(request.getParameter("num"));
        try {
            PageModel pageModel = categoryService.getAllCatsWithPage(page);
            request.setAttribute("page",pageModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/admin/category/list.jsp";
    }
    public String getAllCatsWithPage1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //        但同时需要更新redis中allCats的内容：删除原来的内容，再查一下，更新一下redis中的内容
        updateRedis(request);
        return "/admin/category/list.jsp";
    }

    private void updateRedis(HttpServletRequest request) {
        Jedis jedis = JedisUtils.getJedis();
        jedis.auth("111");
        jedis.del("cats");
        request.removeAttribute("allCatsInPage");
        try {
            PageModel pageModel = categoryService.getAllCatsWithPage(1);
            request.getSession().setAttribute("page", pageModel);
            List allCats = pageModel.getRecords();
            String cats = JsonUtil.list2json(allCats);
            jedis.set("cats", cats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addCategoryUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return "/admin/category/add.jsp";
    }
    //addCategory
    public String addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入addCategory方法");
        String cname = request.getParameter("cname");
        //将该类插入到数据库中
        Category category = new Category();
        category.setCid(UUIDUtils.getId());
        category.setCname(cname);
        try {
            categoryService.addCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        但同时需要更新redis中allCats的内容：删除原来的内容，再查一下，更新一下redis中的内容
        updateRedis(request);
        return "/admin/category/list.jsp";
    }
//    editCategory
    public String editCategoryUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        request.getSession().setAttribute("cid", cid);
        return "/admin/category/edit.jsp";
    }

    public String editCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cname = request.getParameter("cname");
        String cid = (String) request.getSession().getAttribute("cid");
        System.out.println(cid);
        Category category = new Category();
        category.setCname(cname);
        category.setCid(cid);
        try {
            categoryService.updateCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getAllCatsWithPage1(request,response);
    }

    public String delCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        try {
            categoryService.delCategory(cid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateRedis(request);
        return getAllCatsWithPage1(request,response);
    }




}
