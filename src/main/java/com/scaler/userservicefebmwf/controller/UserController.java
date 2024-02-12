package com.scaler.userservicefebmwf.controller;


import com.scaler.userservicefebmwf.dto.LoginRequestDto;
import com.scaler.userservicefebmwf.dto.LogoutRequestDto;
import com.scaler.userservicefebmwf.dto.SignupRequestDto;
import com.scaler.userservicefebmwf.dto.UserDto;
import com.scaler.userservicefebmwf.models.Token;
import com.scaler.userservicefebmwf.models.User;
import com.scaler.userservicefebmwf.service.UserLoginService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.support.HttpComponentsHeadersAdapter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserLoginService loginService;

    @Autowired
    public UserController(UserLoginService loginService){

        this.loginService = loginService;
    }

    public User convertUsertoUserDto(User user){

        SignupRequestDto requestDto  = new SignupRequestDto();

        requestDto.setName(user.getName());
        requestDto.setEmail(user.getEmail());
        requestDto.setPassword(user.getHashedPassword());
        requestDto.setRoles(user.getRoles());
        requestDto.setEmailVerified(user.isEmailVerified());

        return user;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignupRequestDto requestDto){

        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();


        return UserDto.from(loginService.signUp(name, email, password));
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto){

        return loginService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto){

        loginService.logout(requestDto.getToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto ValidateToken(@PathVariable("token") @NonNull String token){

        return UserDto.from(loginService.validateToken(token));
    }
}
