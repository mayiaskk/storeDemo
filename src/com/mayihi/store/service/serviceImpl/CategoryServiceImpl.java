package com.mayihi.store.service.serviceImpl;

import com.mayihi.store.Util.PageModel;
import com.mayihi.store.dao.CategoryDao;
import com.mayihi.store.dao.daoImpl.CategoryDaoImpl;
import com.mayihi.store.domain.Category;
import com.mayihi.store.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl  implements CategoryService {

    CategoryDao categoryDao = new CategoryDaoImpl();

    public PageModel getAllCatsWithPage(int page) throws Exception {
        List<Category> list =categoryDao.getAllCatsWithPage(page);
        int count = categoryDao.getAllCatsCount();
        PageModel pageModel = new PageModel(page,count,CategoryDao.PageSize);
        pageModel.setRecords(list);
        pageModel.setUrl("AdminServlet?method=getAllCatsWithPage");
        return pageModel;
    }

    @Override
    public int addCategory(Category category) throws Exception {
        return categoryDao.addCategory(category);
    }

    @Override
    public int updateCategory(Category category) throws Exception {
        return categoryDao.updateCategory(category);
    }

    @Override
    public int delCategory(String cid) throws Exception {
        return categoryDao.delCategory(cid);
    }

    @Override
    public List<Category> getAllCats() throws Exception {
        return categoryDao.getAllCats();
    }
}
