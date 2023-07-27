package bezth.toDoList.service;

import bezth.toDoList.database.Account_SQLite;
import bezth.toDoList.database.ToDo_SQLite;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class ToDoService {

    private final ToDo_SQLite toDoSqLite = new ToDo_SQLite();
    private final Account_SQLite accountSqLite = new Account_SQLite();

    public void createToDo(HashMap<String, String> mapToDo)  {
        int account_pk = accountSqLite.findPK(mapToDo.get("ID"), mapToDo.get("PW"));
        toDoSqLite.insertDB(mapToDo.get("title"), mapToDo.get("content"), account_pk);
    }

    public JSONObject readToDo(HashMap<String, String> mapAccount) {
        int account_pk = accountSqLite.findPK(mapAccount.get("ID"), mapAccount.get("PW"));
        return toDoSqLite.selectDB(account_pk);
    }

    public void updateToDo(int id, HashMap<String, Optional<Object>> mapToDo) {
        int account_pk = accountSqLite.findPK(mapToDo.get("ID").get().toString(), mapToDo.get("PW").get().toString());
        toDoSqLite.updateDB(id, mapToDo.get("title"), mapToDo.get("content"), mapToDo.get("done"), account_pk);
    }

    public void deleteToDo(int id, HashMap<String, String> mapAccount) {
        int account_pk = accountSqLite.findPK(mapAccount.get("ID"), mapAccount.get("PW"));
        toDoSqLite.deleteDB(id, account_pk);
    }

    public JSONObject readAllToDo() {
        return toDoSqLite.selectAllDB();
    }
}
