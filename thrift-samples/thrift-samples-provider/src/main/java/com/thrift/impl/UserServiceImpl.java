package com.thrift.impl;

import com.thrift.api.User;
import com.thrift.api.UserPage;
import com.thrift.api.UserService;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description 实现thrift 生成的的接口
 */
public class UserServiceImpl implements UserService.Iface {


    @Override
    public String sendString(String para) throws TException {
        System.out.println("i am provider --->>> sendString：size is " + para.length());
        return para;
    }

    @Override
    public boolean userExist(String email) throws TException {
        return false;
    }

    @Override
    public boolean createUser(User user) throws TException {
        return false;
    }

    @Override
    public User getUser(long id) throws TException {
        System.out.println("i am provider --->>> getUser");
        User user = new User();
        user.setId(id);
        user.setName(new String("醋酸菌"));
        user.setSex(1);
        user.setEmail(new String(" https://rain.baimuxym.cn"));
        user.setMobile(new String("18612345678"));
        user.setAddress(new String("北京市 中关村 中关村大街1号 鼎好大厦 1605"));
        user.setIcon(new String(" https://rain.baimuxym.cn"));
        user.setStatus(1);
        user.setUpdateTime(user.getCreateTime());

        List<Integer> permissions = new ArrayList<Integer>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 19, 88, 86, 89, 90, 91, 92));

        user.setPermissions(permissions);

        return user;
    }

    @Override
    public UserPage listUser(int pageNo) throws TException {
        return null;
    }
}
