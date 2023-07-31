package bezth.toDoList.service;

import bezth.toDoList.JwtTokenProvider;
import bezth.toDoList.database.Account_SQLite;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AccountService {

    private final Account_SQLite accountSqLite = new Account_SQLite();

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public void createAccount(HashMap<String, String> mapAccount) {
        accountSqLite.insertDB(mapAccount.get("id"), mapAccount.get("password"));
    }

    public JSONObject readAccount() {
        return accountSqLite.selectDB();
    }

    public String authAccount(HashMap<String, String> mapAccount) {
        int account_pk = accountSqLite.findPK(mapAccount.get("id"), mapAccount.get("password"));
        return jwtTokenProvider.makeJwtToken(account_pk);
    }
}
