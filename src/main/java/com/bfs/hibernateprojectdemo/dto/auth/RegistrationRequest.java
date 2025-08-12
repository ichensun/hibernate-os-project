package com.bfs.hibernateprojectdemo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @JsonProperty("username")
    private String userName;

    private String email;

    private String password;
}
