package com.bfs.hibernateprojectdemo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponse {

    @JsonProperty("id")
    private Long userId;

    private String email;

    private Integer role;

    private String username;

    private String password;

}
