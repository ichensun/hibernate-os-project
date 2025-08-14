package com.bfs.hibernateprojectdemo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtProvider {

    @Value("$security.jwt.token.key")
    private String key;

    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Optional<AuthUserDetail> resolveToken(HttpServletRequest request){
        String prefixedToken = request.getHeader("Authorization"); // extract token value by key "Authorization"
        String token = prefixedToken.substring(7); // remove the prefix "Bearer "

        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody(); // decode

        String username = claims.getSubject();
        List<LinkedHashMap<String, String>> permissions = (List<LinkedHashMap<String, String>>) claims.get("permissions");
//        permissions.forEach(System.out::println);
        // convert the permission list to a list of GrantedAuthority
        List<GrantedAuthority> authorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.get("authority")))
                .collect(Collectors.toList());

        //return a userDetail object with the permissions the user has
        return Optional.of(AuthUserDetail.builder()
                .username(username)
                .authorities(authorities)
                .build());

    }

}
