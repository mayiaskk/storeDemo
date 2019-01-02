package com.mayihi.store.service;

import com.mayihi.store.domain.User;

import java.sql.SQLException;

public interface UserService {
    int regist(User user) throws SQLException;

    String queryUserByCode(String code) throws SQLException;

    int activateByUid(String uid)throws SQLException;

    User checkLogin(String username, String password) throws SQLException;




}
