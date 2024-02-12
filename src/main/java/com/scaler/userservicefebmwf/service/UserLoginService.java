package com.scaler.userservicefebmwf.service;

import com.scaler.userservicefebmwf.models.Token;
import com.scaler.userservicefebmwf.models.User;
import org.springframework.stereotype.Service;


public interface UserLoginService {

    User signUp(String username,String email, String password);


    public Token login(String emil, String password);

    public void logout(String token);


    public User validateToken(String token);

}
