package bezth.toDoList.controller;

import bezth.toDoList.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

// 회원가입
@RestController
public class AccountController {

    private final AccountService accountService = new AccountService();

    @PostMapping("/accounts")  // 계정 추가
    public void createAccount(@RequestBody HashMap<String, String> mapAccount) {
        accountService.createAccount(mapAccount);
    }

    @GetMapping("/accounts")    // 전체 계정 조회
    public JSONObject readAccount() {
        return accountService.readAccount();
    }
}
