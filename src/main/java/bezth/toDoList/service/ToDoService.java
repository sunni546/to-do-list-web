package bezth.toDoList.service;

import bezth.toDoList.jwt.JwtTokenProvider;
import bezth.toDoList.database.ToDo_SQLite;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDo_SQLite toDoSqLite = new ToDo_SQLite();

    private final JwtTokenProvider jwtTokenProvider;

    public void createToDo(HashMap<String, String> mapToDo, String authorization)  {
        // 인증
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        toDoSqLite.insertDB(mapToDo.get("title"), mapToDo.get("content"), account_pk);
    }

    public JSONObject readToDo(String authorization) {
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        return toDoSqLite.selectDB(account_pk);
    }

    public void updateToDo(int id, HashMap<String, Optional<Object>> mapToDo, String authorization) {
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        toDoSqLite.updateDB(id, mapToDo.get("title"), mapToDo.get("content"), mapToDo.get("done"), account_pk);
    }

    public void deleteToDo(int id, String authorization) {
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        toDoSqLite.deleteDB(id, account_pk);
    }

    public JSONObject readAllToDo() {
        return toDoSqLite.selectAllDB();
    }
}
