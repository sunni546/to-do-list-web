package bezth.toDoList.service;

import bezth.toDoList.JwtTokenProvider;
import bezth.toDoList.database.Account_SQLite;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    private final Account_SQLite accountSqLite = new Account_SQLite();

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public void createAccount(HashMap<String, String> mapAccount) {
        accountSqLite.insertDB(mapAccount.get("id"), mapAccount.get("password"));
    }

    public JSONObject readAccount() {
        return accountSqLite.selectDB();
    }

    // 로그인
    public JSONObject authAccount(HashMap<String, String> mapAccount) {
        int account_pk = accountSqLite.findPK(mapAccount.get("id"), mapAccount.get("password"));

        Map<String, String> map = new HashMap<>();
        // Access Token 생성
        map.put("accessToken", jwtTokenProvider.makeJwtToken(account_pk, "accessToken"));
        // Refresh Token 생성 : 새로운 Access Token을 발급하기 위한 용도로만 사용
        map.put("refreshToken", jwtTokenProvider.makeJwtToken(account_pk, "refreshToken"));

        return new JSONObject(map);
    }

    public JSONObject refreshAccount(String refreshToken) {
        // Refresh Token인지 확인
        if (!jwtTokenProvider.findTokenTypeByJwt(refreshToken).equals("refreshToken")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);     // status code : 400
        }

        Map<String, String> map = new HashMap<>();
        // Access Token 새로 생성
        int account_pk = jwtTokenProvider.findAccountIdByJwt(refreshToken);
        map.put("accessToken", jwtTokenProvider.makeJwtToken(account_pk, "accessToken"));
        // 토큰 만료 시간(exp) 7일 이하 시, Refresh Token 새로 생성
        if (jwtTokenProvider.getExpPeriodByJwt(refreshToken) <= Duration.ofDays(7).toMillis()) {
            map.put("refreshToken", jwtTokenProvider.makeJwtToken(account_pk, "refreshToken"));
        }

        return new JSONObject(map);
    }
}
