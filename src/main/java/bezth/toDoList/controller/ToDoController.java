package bezth.toDoList.controller;

import bezth.toDoList.ToDo_SQLite;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class ToDoController {

    private final ToDo_SQLite toDoList = new ToDo_SQLite();

    @PostMapping("/todos")  // '할 일' 추가
    public void createToDo(@RequestBody HashMap<String, String> mapToDO)  {
        toDoList.insertDB(mapToDO.get("title"), mapToDO.get("content"));
    }

    @GetMapping("/todos")    // '할 일' 목록 조회
    public JSONObject readToDo() {
        return toDoList.selectDB();
    }

    @PatchMapping("/todos/{id}")  // '할 일' 수정
    public void updateToDo(@PathVariable("id") int id,
                           @RequestBody HashMap<String, Optional<Object>> mapToDO) {
        toDoList.updateDB(id, mapToDO.get("title"), mapToDO.get("content"), mapToDO.get("done"));
    }

    @DeleteMapping("/todos/{id}")  // '할 일' 삭제
    public void deleteToDo(@PathVariable("id") int id) {
        toDoList.deleteDB(id);
    }

    @GetMapping("/test")    // 테스트 용
    public String test() {
        return "***test***";
    }
}
