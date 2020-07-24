package com.jakob.springbootdemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.expiration-rememberMe}")
    private Long expirationRememberMe;

    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Value("${jwt.role-claims}")
    private String roleClaims;

    @Value("${jwt.token-type}")
    private String tokenType;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(DatatypeConverter.parseBase64Binary(secret));
    }

    public String createToken(String username, List<String> roles, boolean rememberMe) {
        Long expirationTime = rememberMe ? expirationRememberMe : expiration;
        Date createDate = new Date();
        Date expireDate = new Date(createDate.getTime() + expirationTime * 1000);
        String token = Jwts.builder()
                .setHeaderParam("type", tokenType)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .claim(roleClaims, String.join(",", roles))
                .setIssuer("Jakob")
                .setIssuedAt(createDate)
                .setSubject(username)
                .setExpiration(expireDate)
                .compact();
        return tokenPrefix + token;
    }

    public String getUsernameFromToken(String token) {
        return getTokenBody(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expireDate = getTokenBody(token).getExpiration();
        return expireDate.before(new Date());
    }

    public Claims getTokenBody(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


}
