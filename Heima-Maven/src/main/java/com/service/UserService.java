package com.service;

import com.domain.User;

import java.util.List;

public interface UserService {

    public User checkUsername(String username);

    boolean active(String code);

    public List<User> findAll();

    User findByUsernameAndPassword(String username, String password);

    public boolean register(User user);
}
