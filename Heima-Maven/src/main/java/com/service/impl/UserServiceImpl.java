package com.service.impl;

import com.domain.User;
import com.mapper.UserMapper;
import com.service.UserService;
import com.utils.CommonsUtils;
import com.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUsername(String username) {
        User user = userMapper.checkUsername(username);
        return user;
    }



    public boolean register(User user){
        User user1 = userMapper.checkUsername(user.getUsername());
        if (user1!=null){
            // 表示该用户已存在
            return false;
        }
        // 表示用户不存在，可以注册，设置激活码和激活状态
        user.setCode(CommonsUtils.getUUID());
        user.setState(0);// 现在还没有激活
        userMapper.save(user);

        // 发送邮件
        String context = "<a href='http://localhost:8080/Heima-Maven/user/activeMail?code="+user.getCode()+"'>点击激活</a>";
        MailUtils.sendMail(user.getEmail(),context,"激活邮件");
        return true;
    }

    @Override
    public boolean active(String code) {
        // 根据code查找用户
        User user = userMapper.findByCode(code);
        System.out.println(user);
        if (user != null){
            // 改变激活状态为 1
            user.setState(1);
            // 更改数据库中的state
            System.out.println(user);
            userMapper.updateState(user);
            return true;
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = userMapper.findAll();
        return userList;
    }

    /**
     * 登录方法，根据用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userMapper.findByUsernameAndPassword(username,password);
    }

}
