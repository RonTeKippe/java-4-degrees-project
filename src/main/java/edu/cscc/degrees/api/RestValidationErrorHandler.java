package edu.cscc.degrees.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class RestValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody

    public HashMap<String, HashMap<String , String>> handleValidationErrors(
            MethodArgumentNotValidException exception){
        HashMap<String, String> fieldErrorMap = new HashMap<>();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();
        for(FieldError fieldError : fieldErrorList) {
            fieldErrorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        HashMap<String, HashMap<String, String >> response = new HashMap<>();
        response.put("fieldErrors", fieldErrorMap);
        return response;
    }

}
