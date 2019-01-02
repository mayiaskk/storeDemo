package com.mayihi.store.dao.daoImpl;

import com.mayihi.store.Util.JDBCUtils;
import com.mayihi.store.dao.CategoryDao;
import com.mayihi.store.domain.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    public List<Category> getAllCatsWithPage(int page) throws Exception{
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        List<Category> list = runner.query("select * from category limit ?,?", new BeanListHandler<Category>(Category.class),(page-1)*PageSize,PageSize);
        return list;
    }

    @Override
    public int addCategory(Category category) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "insert into category values(?,?)";
        return runner.update(sql, category.getCid(), category.getCname());
    }

    @Override
    public int getAllCatsCount() throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        Long res = (Long) runner.query("select count(*) from category", new ScalarHandler());
        return res.intValue();
    }

    @Override
    public int updateCategory(Category category) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        return runner.update("update category set cname=? where cid=?", category.getCname(), category.getCid());
    }

    @Override
    public int delCategory(String cid) throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        return runner.update("delete from category where cid = ?", cid);
    }

    @Override
    public List<Category> getAllCats() throws Exception {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        List<Category> list = runner.query("select * from category", new BeanListHandler<Category>(Category.class));
        return list;
    }
}
