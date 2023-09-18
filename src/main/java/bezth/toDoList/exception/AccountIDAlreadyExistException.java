package bezth.toDoList.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// status code : 400
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountIDAlreadyExistException extends RuntimeException{

    public AccountIDAlreadyExistException(String message){
        super(message);
    }
}
