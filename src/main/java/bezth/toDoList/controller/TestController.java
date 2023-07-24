package bezth.toDoList.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")    // 테스트 용
    public String test() {
        return "***test***";
    }
}
