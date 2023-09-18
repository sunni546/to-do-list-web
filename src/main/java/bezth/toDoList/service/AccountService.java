package bezth.toDoList.service;

import bezth.toDoList.jwt.JwtTokenProvider;
import bezth.toDoList.database.Account_SQLite;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final Account_SQLite accountSqLite = new Account_SQLite();

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 회원가입
    public void createAccount(String id, String password) {
        accountSqLite.insertDB(id, password);
    }

    public JSONObject readAccount() {
        return accountSqLite.selectDB();
    }

    // 로그인
    public JSONObject authAccount(String id, String password) {

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        Map<String, String> map = new HashMap<>();
        int account_pk = accountSqLite.findPK(id, password);

        // Access Token 생성
        map.put("accessToken", jwtTokenProvider.makeJwtToken(account_pk, "accessToken", authentication));
        // Refresh Token 생성 : 새로운 Access Token을 발급하기 위한 용도로만 사용
        map.put("refreshToken", jwtTokenProvider.makeJwtToken(account_pk, "refreshToken", authentication));

        return new JSONObject(map);
    }

    public JSONObject refreshAccount(String refreshToken) {
        // Refresh Token인지 확인
        if (!jwtTokenProvider.findTokenTypeByJwt(refreshToken).equals("refreshToken")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);     // status code : 400
        }

        Map<String, String> map = new HashMap<>();

        // Access Token 새로 생성
        int account_pk = jwtTokenProvider.findAccountPkByJwt(refreshToken);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        map.put("accessToken", jwtTokenProvider.makeJwtToken(account_pk, "accessToken", authentication));

        // 토큰 만료 시간(exp) 7일 이하 시, Refresh Token 새로 생성
        if (jwtTokenProvider.getExpPeriodByJwt(refreshToken) <= Duration.ofDays(7).toMillis()) {
            map.put("refreshToken", jwtTokenProvider.makeJwtToken(account_pk, "refreshToken", authentication));
        }

        return new JSONObject(map);
    }
}
