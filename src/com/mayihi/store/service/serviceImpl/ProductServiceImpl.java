package com.mayihi.store.service.serviceImpl;

import com.mayihi.store.Util.PageModel;
import com.mayihi.store.dao.ProductDao;
import com.mayihi.store.dao.daoImpl.ProductDaoImpl;
import com.mayihi.store.domain.CategoryPageBean;
import com.mayihi.store.domain.Product;
import com.mayihi.store.service.ProductService;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    ProductDaoImpl productDao = new ProductDaoImpl();


    @Override
    public CategoryPageBean queryProductsByCid(String cid, int page) throws SQLException {
        List<Product> products = productDao.queryProductsByCid(cid, page);
        long count = productDao.queryProductCountByCid(cid);
        CategoryPageBean pageBean = new CategoryPageBean();
//        总页数查一下数据库该类别总共有多少商品，再除以一页商品数PageSize
//        若刚好除尽，则总页数为最后的结果，否则，总页数为最后的结果+1
        int totalPages = (int) (count % ProductDao.PageSize == 0 ? count / ProductDao.PageSize : count / ProductDao.PageSize + 1);

        pageBean.setPageSize(ProductDao.PageSize);
        pageBean.setTotalPages(totalPages);
        pageBean.setProducts(products);
        pageBean.setCid(cid);
        int startPage = 0;
        int lastPage = 0;
        int prePage = 0;
        int nextPage = 0;
        if (page > 1) {
            prePage = page - 1;
            startPage = 1;
        }
        if (page < totalPages) {
            nextPage = page + 1;
            lastPage = totalPages;
        }
        if (prePage == startPage) prePage = 0;
        if (nextPage == lastPage) nextPage = 0;
        pageBean.setStartPage(startPage);
        pageBean.setPrePage(prePage);
        pageBean.setNextPage(nextPage);
        pageBean.setLastPage(lastPage);
        pageBean.setPage(page);

        return pageBean;
    }

    @Override
    public List<Product> queryNewProducts() throws Exception {
        return productDao.queryNewProducts();
    }

    @Override
    public Product queryProductByPid(String pid) throws Exception {
        return productDao.queryProductByPid(pid);
    }

    @Override
    public PageModel showProductsWithPage(int onShelf,int page) throws Exception {
        List<Product> list =productDao.getProductsWithPage(onShelf,page);
        int count = productDao.getAllProductsCountWithPflag(onShelf);
        PageModel pageModel = new PageModel(page,count,ProductDao.PageSize);
        pageModel.setRecords(list);
        pageModel.setUrl("ProductManageServlet?method=showProductsWithPage&onShelf="+onShelf);
        return pageModel;
    }

    @Override
    public int updateProduct(Product product) throws Exception {
        return productDao.updateProduct(product);
    }

    @Override
    public int addProduct(Product product) throws Exception {
        return productDao.addProduct(product);
    }

    public List<Product> queryHotProducts() throws Exception {
        ProductDaoImpl productDao = new ProductDaoImpl();
        return productDao.queryHotProducts();

    }
}
