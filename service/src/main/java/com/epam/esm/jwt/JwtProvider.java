package com.epam.esm.jwt;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.mapper.UserDetailsMapper;
import com.epam.esm.service.UserService;
import com.epam.esm.utils.ServiceConstant;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secret;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    private UserService userService;
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    public JwtProvider(UserService userService, UserDetailsMapper userDetailsMapper) {
        this.userService = userService;
        this.userDetailsMapper = userDetailsMapper;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserDTO user) {

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put(ServiceConstant.ROLE.getValue(), user.getUserRole());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails user = userDetailsMapper.toUserDetails(userService.getUserByEmail(getUsername(token)));
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(ServiceConstant.AUTHORIZATION_HEADER.getValue());
        if (bearerToken != null && bearerToken.startsWith(ServiceConstant.BEARER.getValue())) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
