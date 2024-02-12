package com.scaler.userservicefebmwf.service;

import com.scaler.userservicefebmwf.dto.LoginRequestDto;
import com.scaler.userservicefebmwf.dto.SignupRequestDto;
import com.scaler.userservicefebmwf.models.Token;
import com.scaler.userservicefebmwf.models.User;
import com.scaler.userservicefebmwf.repositories.TokenRepo;
import com.scaler.userservicefebmwf.repositories.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private UserRepo userRepo;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepo tokenRepo;


    public UserLoginServiceImpl(UserRepo userRepo,BCryptPasswordEncoder bCryptPasswordEncoder,
                                TokenRepo tokenRepo){
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepo = tokenRepo;
    }


    @Override
    public User signUp(String username, String email, String password) {

        User u = new User();

        u.setName(username);
        u.setEmail(email);
        u.setHashedPassword(bCryptPasswordEncoder.encode(password));

        User user = userRepo.save(u);

        return user;
    }

    @Override
    public Token login(String email, String password) {

        Optional<User> userOptional = userRepo.findByEmail(email);

        if(userOptional.isEmpty()){
            //throw usernot exist exception
            return null;
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())){
            //thorw password not match eception
            return null;
        }
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);

        // Convert LocalDate to Date
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setUser(user);
        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        Token savedToken =tokenRepo.save(token);

        return savedToken;
    }

    @Override
    public void logout(String token) {

        Optional<Token> tokenOptional = tokenRepo.findByValueAndDeletedEquals(token, false);

        if(tokenOptional.isEmpty()){
            //thrw token doesnt find excpetion
            return;
        }

        Token tkn = tokenOptional.get();

        tkn.setDeleted(true);
        tokenRepo.save(tkn);

        return;
    }

    @Override
    public User validateToken(String token) {

        Optional<Token> tknOpt = tokenRepo.findByValueAndDeletedEqualsAndExpiryAtGreaterThan(token, false, new Date());

        if(tknOpt.isEmpty()){
            return null;
        }

        return tknOpt.get().getUser();
    }
}
