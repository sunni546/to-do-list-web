package bezth.toDoList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountIDAlreadyExistException extends RuntimeException{

    public AccountIDAlreadyExistException(String message){
        super(message);
    }
}
