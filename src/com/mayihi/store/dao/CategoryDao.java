package com.mayihi.store.dao;

import com.mayihi.store.domain.Category;

import java.util.List;

public interface CategoryDao {
    int PageSize = 6;

    List<Category> getAllCatsWithPage(int page) throws Exception;

    int addCategory(Category category)throws Exception;

    int getAllCatsCount()throws Exception;

    int updateCategory(Category category)throws Exception;

    int delCategory(String cid)throws Exception;

    List<Category> getAllCats()throws Exception;
}
