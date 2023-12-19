package com.coviscon.postservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = CustomException.class)
    protected String handleCustomException(CustomException ex, Model model) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("[ErrorHandler handleCustomException] Exception : {}, Message : {}"
            ,errorCode.getHttpStatus(), errorCode.getMessage());

        model.addAttribute("error", errorCode.getMessage());
        return "error/" + errorCode.getPage();
    }

}
