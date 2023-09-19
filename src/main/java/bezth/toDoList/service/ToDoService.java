package bezth.toDoList.service;

import bezth.toDoList.jwt.JwtTokenProvider;
import bezth.toDoList.database.ToDo_SQLite;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDo_SQLite toDoSqLite = new ToDo_SQLite();

    private final JwtTokenProvider jwtTokenProvider;

    public void createToDo(HashMap<String, String> mapToDo, String authorization)  {
        IsAccessToken(authorization);
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        toDoSqLite.insertDB(mapToDo.get("title"), mapToDo.get("content"), account_pk);
    }

    public JSONObject readToDo(String authorization) {
        IsAccessToken(authorization);
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        return toDoSqLite.selectDB(account_pk);
    }

    public void updateToDo(int id, HashMap<String, Optional<Object>> mapToDo, String authorization) {
        IsAccessToken(authorization);
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        toDoSqLite.updateDB(id, mapToDo.get("title"), mapToDo.get("content"), mapToDo.get("done"), account_pk);
    }

    public void deleteToDo(int id, String authorization) {
        IsAccessToken(authorization);
        int account_pk = jwtTokenProvider.findAccountPkByJwt(authorization);
        toDoSqLite.deleteDB(id, account_pk);
    }

    // Access Token인지 확인
    public void IsAccessToken(String authorization)  {
        if (!jwtTokenProvider.findTokenTypeByJwt(authorization).equals("accessToken")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);     // status code : 400
        }
    }

    public JSONObject readAllToDo() {
        return toDoSqLite.selectAllDB();
    }
}
