package com.mayihi.store.dao;

import com.mayihi.store.domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {

    int PageSize = 12;

    List<Product> queryProductsByCid(String cid, int page) throws SQLException;

    long queryProductCountByCid(String cid) throws SQLException;

    List<Product> queryNewProducts() throws Exception;

    List<Product> queryHotProducts() throws Exception;

    Product queryProductByPid(String pid)throws Exception;

    int getAllProductsCountWithPflag(int onshelf)throws Exception;

    List<Product> getProductsWithPage(int onshelf,int page)throws Exception;

    int updateProduct(Product product)throws Exception;

    int addProduct(Product product)throws Exception;


}
