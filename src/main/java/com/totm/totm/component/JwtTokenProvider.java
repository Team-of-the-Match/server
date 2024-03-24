package com.totm.totm.component;

import com.totm.totm.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {

    private Key key;
    private final String secret;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Value("${spring.jwt.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.refresh-expiration-time}")
    private long refreshExpirationTime;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret,
                            UserDetailsServiceImpl userDetailsServiceImpl) {
        this.secret = secret;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public String createAccessToken(Authentication authentication, Long id, String nickname, String type) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims()
                .setSubject(authentication.getName());

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        if(type.equals("member"))
            return Jwts.builder()
                    .setClaims(claims)
                    .claim("id", id)
                    .claim("nickname", nickname)
                    .claim("auth", authorities)
                    .setIssuedAt(now)
                    .setExpiration(expireDate)
                    .signWith(key)
                    .compact();
        else
            return Jwts.builder()
                    .setClaims(claims)
                    .claim("id", id)
                    .claim("auth", authorities)
                    .setIssuedAt(now)
                    .setExpiration(expireDate)
                    .signWith(key)
                    .compact();
    }

    public String createNewAccessToken(String username, Long id, String nickname, String type) {
        Claims claims = Jwts.claims()
                .setSubject(username);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        if(type.equals("member"))
            return Jwts.builder()
                    .setClaims(claims)
                    .claim("id", id)
                    .claim("nickname", nickname)
                    .claim("auth", type)
                    .setIssuedAt(now)
                    .setExpiration(expireDate)
                    .signWith(key)
                    .compact();
        else
            return Jwts.builder()
                    .setClaims(claims)
                    .claim("id", id)
                    .claim("auth", type)
                    .setIssuedAt(now)
                    .setExpiration(expireDate)
                    .signWith(key)
                    .compact();

    }

    public String createRefreshToken() {

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        if(claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
