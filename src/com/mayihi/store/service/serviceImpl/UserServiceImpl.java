package com.mayihi.store.service.serviceImpl;

import com.mayihi.store.dao.daoImpl.UserDaoImpl;
import com.mayihi.store.domain.User;
import com.mayihi.store.service.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    @Override
    public int regist(User user) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.regist(user);
    }

    @Override
    public String queryUserByCode(String code) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.queryUserByCode(code);
    }

    @Override
    public int activateByUid(String uid) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.activateByUid(uid);
    }

    @Override
    public User checkLogin(String username, String password) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.checkLogin(username,password);
    }







}
