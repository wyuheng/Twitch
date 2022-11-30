package com.roadtomastery.project.service;

import com.roadtomastery.project.dao.RegisterDao;
import com.roadtomastery.project.entity.db.User;
import com.roadtomastery.project.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegisterService {

    @Autowired
    private RegisterDao registerDao;

    public boolean register(User user) throws IOException {
        user.setPassword(Util.encryptPassword(user.getUserId(), user.getPassword()));
        return registerDao.register(user);
    }
}

