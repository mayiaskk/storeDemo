package com.mayihi.store.service;

import com.mayihi.store.Util.PageModel;
import com.mayihi.store.domain.CategoryPageBean;
import com.mayihi.store.domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    CategoryPageBean queryProductsByCid(String cid, int page) throws SQLException;

    List<Product> queryNewProducts() throws Exception;

    Product queryProductByPid(String pid) throws Exception;

    PageModel showProductsWithPage(int onshelf,int page)throws Exception;

    int updateProduct(Product product)throws Exception;

    int addProduct(Product product)throws Exception;

}
