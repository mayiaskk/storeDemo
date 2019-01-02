package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.BaseServlet;
import com.mayihi.store.Util.JedisUtils;
import com.mayihi.store.Util.JsonUtil;
import com.mayihi.store.Util.PageModel;
import com.mayihi.store.dao.CategoryDao;
import com.mayihi.store.domain.Category;
import com.mayihi.store.domain.Product;
import com.mayihi.store.service.CategoryService;
import com.mayihi.store.service.serviceImpl.CategoryServiceImpl;
import com.mayihi.store.service.serviceImpl.ProductServiceImpl;
import net.sf.json.util.JSONUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet", urlPatterns = "/IndexServlet")
public class IndexServlet extends BaseServlet {

    public String findAllCats(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //版本1：在文档加载时，同时在响应主页的servlet中查询分类信息返回。
        //版本2：在文档加载完毕后通过ajax异步请求此servlet中的该方法，查询数据库中所有分类信息，返回到页面上
//        版本3：考虑主页会被多次访问，因此若每次分类信息都到数据库中取出，则会影响速度，可以将分类信息存在redis中，下次读取，若redis中有即可直接读出
        try {
            Jedis jedis = JedisUtils.getJedis();
            jedis.auth("111");
            String cats = jedis.get("cats");
            if (null == cats){
                PageModel pageModel = new CategoryServiceImpl().getAllCatsWithPage(1);
                List list = pageModel.getRecords();
                cats = JsonUtil.list2json(list);
                jedis.set("cats", cats);
            }
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(cats);
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
