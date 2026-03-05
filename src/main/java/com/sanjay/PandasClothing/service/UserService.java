package com.sanjay.PandasClothing.service;

import com.sanjay.PandasClothing.entity.User;
import com.sanjay.PandasClothing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

}