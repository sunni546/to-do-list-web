package bezth.toDoList.controller;

import bezth.toDoList.database.Account_SQLite;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

// 회원가입
@RestController
public class AccountController {

    private final Account_SQLite accountSqLite = new Account_SQLite();

    @PostMapping("/accounts")  // 계정 추가
    public void createAccount(@RequestBody HashMap<String, String> mapAccount) {
        accountSqLite.insertDB(mapAccount.get("id"), mapAccount.get("password"));
    }

    @GetMapping("/accounts")    // 전체 계정 조회
    public JSONObject readAccount() {
        return accountSqLite.selectDB();
    }

    /*
    @GetMapping("/reset")    // RESET
    public void resetAccount() {
        accountSqLite.resetDB();
    }
     */
}
