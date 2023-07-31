package bezth.toDoList.controller;

import bezth.toDoList.service.ToDoService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class ToDoController {

    private final ToDoService toDoService = new ToDoService();

    @PostMapping("/todos")  // '할 일' 추가
    public void createToDo(@RequestBody HashMap<String, String> mapToDo,
                           @RequestHeader("Authorization") String authorization)  {
        toDoService.createToDo(mapToDo, authorization);
    }

    @GetMapping("/todos")    // '할 일' 목록 조회
    public JSONObject readToDo(@RequestHeader("Authorization") String authorization) {
        return toDoService.readToDo(authorization);
    }

    @PatchMapping("/todos/{id}")  // '할 일' 수정
    public void updateToDo(@PathVariable("id") int id,
                           @RequestBody HashMap<String, Optional<Object>> mapToDo,
                           @RequestHeader("Authorization") String authorization) {
        toDoService.updateToDo(id, mapToDo, authorization);
    }

    @DeleteMapping("/todos/{id}")  // '할 일' 삭제
    public void deleteToDo(@PathVariable("id") int id,
                           @RequestHeader("Authorization") String authorization) {
        toDoService.deleteToDo(id, authorization);
    }
}
