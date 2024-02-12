package com.scaler.userservicefebmwf.dto;

import com.scaler.userservicefebmwf.models.Role;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignupRequestDto {

    private String name;
    private String email;
    private String password;

    @ManyToMany
    private List<Role> roles;
    private boolean emailVerified;

}
