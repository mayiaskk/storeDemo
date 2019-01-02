package com.mayihi.store.dao.daoImpl;

import com.mayihi.store.Util.JDBCUtils;
import com.mayihi.store.dao.UserDao;
import com.mayihi.store.domain.Product;
import com.mayihi.store.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public int regist(User user) throws SQLException {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        int i = runner.update("insert into user values(?,?,?,?,?,?,?,?,?,?)",
                user.getUid(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getTelephone(),
                user.getBirthday(),
                user.getSex(), user.getState(),
                user.getCode());
        return i;
    }

    @Override
    public String queryUserByCode(String code) throws SQLException {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String uid = runner.query("select uid from user where code = ?", new ResultSetHandler<String>() {
            @Override
            public String handle(ResultSet resultSet) throws SQLException {
                String id = null;
                if (resultSet.next()) {
                    id = resultSet.getString("uid");
                }
                return id;
            }
        }, code);
        return uid;
    }

    @Override
    public int activateByUid(String uid) throws SQLException {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        int res = runner.update("update user set state = ?,code=null where uid = ?", 1, uid);
        return res;
    }

    @Override
    public User checkLogin(String username, String password) throws SQLException {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        User user = runner.query("select * from user where username = ? and password = ?", new BeanHandler<User>(User.class), username, password);
        return user;
    }







}
