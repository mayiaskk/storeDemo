package com.mayihi.store.dao.daoImpl;

import com.mayihi.store.Util.JDBCUtils;
import com.mayihi.store.dao.ProductDao;
import com.mayihi.store.domain.Product;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public List<Product> queryProductsByCid(String cid, int page) throws SQLException {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        //page1:limit 12 offset 0   page2:limit 12 offset 12     page n:limit 12 offset 12*(n-1)
        List<Product> list = runner.query("select * from product where cid = ? limit ? offset ?", new BeanListHandler<Product>(Product.class), cid, PageSize, PageSize * (page - 1));
        return list;
    }

    @Override
    public long queryProductCountByCid(String cid) throws SQLException {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        long query = (long) runner.query("select count(*) from product where cid = ?", new ScalarHandler(), cid);
        return query;
    }

    @Override
    public List<Product> queryNewProducts() throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        return runner.query("select * from product where pflag=0 order by pdate desc limit 9", new BeanListHandler<Product>(Product.class));
    }


    public List<Product> queryHotProducts() throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        return runner.query("select * from product where pflag=0 and is_hot = 1 order by pdate desc limit 9", new BeanListHandler<Product>(Product.class));
    }

    @Override
    public Product queryProductByPid(String pid) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        return runner.query("select * from product where pid = ?", new BeanHandler<Product>(Product.class), pid);
    }

    @Override
    public int getAllProductsCountWithPflag(int onshelf) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        Long query = (Long) runner.query("select count(*) from product where pflag=?", new ScalarHandler(), onshelf);
        return query.intValue();
    }

    @Override
    public List<Product> getProductsWithPage(int onshelf, int page) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        List<Product> list = runner.query("select * from product where pflag=? limit ?,?", new BeanListHandler<Product>(Product.class), onshelf, (page - 1) * PageSize, PageSize);
        return list;
    }

    @Override
    public int updateProduct(Product product) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "";
        Object[] params = {};
        if (null != product.getPimage()) {
            sql = "update product set pname=?,market_price=?,shop_price=?,pimage=?,pdate=?,is_hot=?,pdesc=?,pflag=?,cid=? where pid = ?";
            params = new Object[]{product.getPname(), product.getMarket_price(), product.getShop_price(), product.getPimage(), new Date(),
                    product.getIs_hot(), product.getPdesc(), product.getPflag(), product.getCid(), product.getPid()};
        } else {
            sql = "update product set pname=?,market_price=?,shop_price=?,pdate=?,is_hot=?,pdesc=?,pflag=?,cid=? where pid = ?";
            params = new Object[]{product.getPname(), product.getMarket_price(), product.getShop_price(), new Date(),
                    product.getIs_hot(), product.getPdesc(), product.getPflag(), product.getCid(), product.getPid()};
        }

        return runner.update(sql, params);
    }

    @Override
    public int addProduct(Product product) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {product.getPid(), product.getPname(), product.getMarket_price(), product.getShop_price(), product.getPimage(),
                new Date(), product.getIs_hot(), product.getPdesc(), product.getPflag(), product.getCid()};
        return runner.update(sql, params);
    }

}
