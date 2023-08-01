package bezth.toDoList;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Date;

@Component
@PropertySource("classpath:application-jwt.properties")
public class JwtTokenProvider {

    @Value("${secret.key}")
    private String JWT_SECRET;

    public String makeJwtToken(int pk) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // Header 타입(typ)
                .setIssuedAt(now)   // 토큰 발급 시간(iat), Date 타입만
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // 토큰 만료 시간(exp), Date 타입만
                .claim("account_pk", pk)    // 비공개(private) 클레임
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)   // 해싱 알고리즘과 시크릿 키 설정
                .compact();
    }

    public Claims parseJwtToken(String token) {
        validateToken(token);

        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public int findAccountIdByJwt(String token){
        Claims claims = parseJwtToken(token);
        return (int) claims.get("account_pk");
    }

    // JWT 토큰 유효성 검사
    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return;
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
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);     // status code : 401
    }
}
