package com.mapper;

import com.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    public User checkUsername(String username);

    public void save(User user);

    User findByCode(String code);

    List<User> findAll();

    void updateState(User user);

    User findByUsernameAndPassword(@Param(value = "username") String username, @Param(value = "password") String password);

}
