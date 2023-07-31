package bezth.toDoList.controller;

import bezth.toDoList.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class AccountController {

    private final AccountService accountService = new AccountService();

    @PostMapping("/accounts")  // 계정 추가 (회원가입)
    public void createAccount(@RequestBody HashMap<String, String> mapAccount) {
        accountService.createAccount(mapAccount);
    }

    @GetMapping("/accounts")    // 전체 계정 조회
    public JSONObject readAccount() {
        return accountService.readAccount();
    }

    @PostMapping("/auth")  // 로그인
    public String authAccount(@RequestBody HashMap<String, String> mapAccount) {
        accountService.authAccount(mapAccount);
        return "***Log-in success***";
    }
}
