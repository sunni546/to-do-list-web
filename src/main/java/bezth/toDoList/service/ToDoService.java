package bezth.toDoList.service;

import bezth.toDoList.database.ToDo_SQLite;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class ToDoService {

    private final ToDo_SQLite toDoSqLite = new ToDo_SQLite();

    public void createToDo(HashMap<String, String> mapToDo, String authorization)  {
        int account_pk = AccountService.mapUUID.get(authorization);
        toDoSqLite.insertDB(mapToDo.get("title"), mapToDo.get("content"), account_pk);
    }

    public JSONObject readToDo(String authorization) {
        int account_pk = AccountService.mapUUID.get(authorization);
        return toDoSqLite.selectDB(account_pk);
    }

    public void updateToDo(int id, HashMap<String, Optional<Object>> mapToDo, String authorization) {
        int account_pk = AccountService.mapUUID.get(authorization);
        toDoSqLite.updateDB(id, mapToDo.get("title"), mapToDo.get("content"), mapToDo.get("done"), account_pk);
    }

    public void deleteToDo(int id, String authorization) {
        int account_pk = AccountService.mapUUID.get(authorization);
        toDoSqLite.deleteDB(id, account_pk);
    }

    public JSONObject readAllToDo() {
        return toDoSqLite.selectAllDB();
    }
}
