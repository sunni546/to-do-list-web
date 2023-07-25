package bezth.toDoList.service;

import bezth.toDoList.database.Account_SQLite;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class AccountService {

    private final Account_SQLite accountSqLite = new Account_SQLite();

    public void createAccount(HashMap<String, String> mapAccount) {
        accountSqLite.insertDB(mapAccount.get("id"), mapAccount.get("password"));
    }

    public JSONObject readAccount() {
        return accountSqLite.selectDB();
    }
}
