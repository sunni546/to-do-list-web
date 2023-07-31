package bezth.toDoList.service;

import bezth.toDoList.database.Account_SQLite;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountService {

    private final Account_SQLite accountSqLite = new Account_SQLite();
    public static final Map<String, Integer> mapUUID = new HashMap<>();

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

    public void authAccount(HashMap<String, String> mapAccount) {
        int pk = accountSqLite.findPK(mapAccount.get("id"), mapAccount.get("password"));

        // UUID Random 생성
        UUID uuid = UUID.randomUUID();
        String strUUID = uuid.toString().replace("-", "");

        mapUUID.put(strUUID, pk);

        // 생성한 UUID 출력
        // System.out.println(pk + " strUUID" + ": " + strUUID);
    }
}
