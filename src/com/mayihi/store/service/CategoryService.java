package com.mayihi.store.service;

import com.mayihi.store.Util.PageModel;
import com.mayihi.store.domain.Category;

import java.util.List;

public interface CategoryService {

    PageModel getAllCatsWithPage(int page) throws Exception;


    int addCategory(Category category)throws Exception;

    int updateCategory(Category category)throws Exception;

    int delCategory(String cid)throws Exception;

    List<Category> getAllCats()throws Exception;
}
