package bezth.toDoList.controller;

import bezth.toDoList.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts")  // 계정 추가 (회원가입)
    public void createAccount(@RequestBody HashMap<String, String> mapAccount) {
        accountService.createAccount(mapAccount);
    }

    @GetMapping("/accounts")    // 전체 계정 조회
    public JSONObject readAccount() {
        return accountService.readAccount();
    }

    @PostMapping("/auth")  // 로그인
    public JSONObject authAccount(@RequestBody HashMap<String, String> mapAccount) {
        return accountService.authAccount(mapAccount);
    }

    @PostMapping("/refresh")  // Refresh
    public JSONObject refreshAccount(@RequestHeader("Authorization") String authorization) {
        return accountService.refreshAccount(authorization);
    }
}
