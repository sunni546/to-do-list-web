package bezth.toDoList.controller;

import bezth.toDoList.database.ToDo_SQLite;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class ToDoController {

    private final ToDo_SQLite toDoSqLite = new ToDo_SQLite();

    @PostMapping("/todos")  // '할 일' 추가
    public void createToDo(@RequestBody HashMap<String, String> mapToDO)  {
        toDoSqLite.insertDB(mapToDO.get("title"), mapToDO.get("content"));
    }

    @GetMapping("/todos")    // '할 일' 목록 조회
    public JSONObject readToDo() {
        return toDoSqLite.selectDB();
    }

    @PatchMapping("/todos/{id}")  // '할 일' 수정
    public void updateToDo(@PathVariable("id") int id,
                           @RequestBody HashMap<String, Optional<Object>> mapToDO) {
        toDoSqLite.updateDB(id, mapToDO.get("title"), mapToDO.get("content"), mapToDO.get("done"));
    }

    @DeleteMapping("/todos/{id}")  // '할 일' 삭제
    public void deleteToDo(@PathVariable("id") int id) {
        toDoSqLite.deleteDB(id);
    }
}
