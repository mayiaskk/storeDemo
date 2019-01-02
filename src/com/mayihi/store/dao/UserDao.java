package com.mayihi.store.dao;

import com.mayihi.store.domain.Product;
import com.mayihi.store.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    int regist(User user) throws SQLException;

    String queryUserByCode(String code) throws SQLException;

    int activateByUid(String uid)throws SQLException;

    User checkLogin(String username, String password) throws SQLException;





}
