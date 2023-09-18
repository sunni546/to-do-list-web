package bezth.toDoList.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:application-jwt.properties")
public class JwtTokenProvider {

    @Value("${secret.key}")
    private String JWT_SECRET;

    public String makeJwtToken(int pk, String token, Authentication authentication) {

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date iat = new Date();
        Date exp = null;
        switch (token) {
            case "accessToken" -> exp = new Date(iat.getTime() + Duration.ofMinutes(30).toMillis());    // 30분
            case "refreshToken" -> exp = new Date(iat.getTime() + Duration.ofDays(30).toMillis());      // 30일
            default -> {
            }
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(iat)   // 토큰 발급 시간(iat), Date 타입만
                .setExpiration(exp) // 토큰 만료 시간(exp), Date 타입만
                .claim("account_pk", pk)    // 비공개(private) 클레임
                .claim("tokenType", token)    // 비공개(private) 클레임
                .claim("auth", authorities)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)   // 해싱 알고리즘과 시크릿 키 설정
                .compact();
    }

    public Claims parseJwtToken(String token) {
        if (validateToken(token)) {
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);     // status code : 401
        }
    }

    public int findAccountPkByJwt(String token){
        Claims claims = parseJwtToken(token);
        return (int) claims.get("account_pk");
    }

    public String findTokenTypeByJwt(String token){
        Claims claims = parseJwtToken(token);
        return claims.get("tokenType").toString();
    }

    public Date findExpirationByJwt(String token){
        Claims claims = parseJwtToken(token);
        return claims.getExpiration();
    }

    public long getExpPeriodByJwt(String token){
        Date now = new Date();
        return findExpirationByJwt(token).getTime() - now.getTime();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseJwtToken(token);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {            // JWT의 기존 서명을 확인하지 못했을 때
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } catch (MalformedJwtException e) {         // JWT가 올바르게 구성되지 않았을 때
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } catch (ExpiredJwtException e) {           // JWT를 생성할 때 지정한 유효기간이 초과되었을 때
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } catch (UnsupportedJwtException e) {       // 예상하는 형식과 다른 형식이거나 구성의 JWT일 때
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty.: " + e);
        }
        return false;
    }
}
