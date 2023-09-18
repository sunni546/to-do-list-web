package bezth.toDoList.controller;

import bezth.toDoList.dto.AccountRequestDto;
import bezth.toDoList.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/accounts")  // 계정 추가 (회원가입)
    public void createAccount(@RequestBody AccountRequestDto accountRequestDto) {
        String accountId = accountRequestDto.getId();
        String password = accountRequestDto.getPassword();
        accountService.createAccount(accountId, password);
    }

    @GetMapping("/accounts")    // 전체 계정 조회
    public JSONObject readAccount() {
        return accountService.readAccount();
    }

    @PostMapping("/auth")  // 로그인
    public JSONObject authAccount(@RequestBody AccountRequestDto accountRequestDto) {
        String accountId = accountRequestDto.getId();
        String password = accountRequestDto.getPassword();
        return accountService.authAccount(accountId, password);
    }

    @PostMapping("/refresh")  // Refresh
    public JSONObject refreshAccount(@RequestHeader("Authorization") String authorization) {
        return accountService.refreshAccount(authorization);
    }
}
