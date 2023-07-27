package bezth.toDoList.service;

import bezth.toDoList.database.Account_SQLite;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

public class AccountService {

    private final Account_SQLite accountSqLite = new Account_SQLite();

    public void createAccount(HashMap<String, String> mapAccount) {
       /*
       // (임시) ID/PW ':' 포함 검사
        if(mapAccount.get("id").contains(":") | mapAccount.get("password").contains(":")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);    // status code : 400
        }
         */
        accountSqLite.insertDB(mapAccount.get("id"), mapAccount.get("password"));
    }

    public JSONObject readAccount() {
        return accountSqLite.selectDB();
    }
}
