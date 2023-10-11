package com.jwt.jwtreaciveimpl.config;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties {
    private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";

    // validity in milliseconds
    private final long validityInMs = 3600000; // 1h
}