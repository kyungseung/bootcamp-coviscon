package com.coviscon.itemservice.exception;

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

//    @ResponseBody
//    @ExceptionHandler(value = RuntimeException.class)
//    protected ResponseEntity<ErrorDto> notfound(CustomException ex, Model model) {
//        ErrorDto errorDto = new ErrorDto();
//        ErrorCode errorCode = ex.getErrorCode();
//        errorDto.setErrorCode(errorCode);
//        log.error("[ErrorHandler handleCustomException] Exception : {}, Message : {}"
//                ,errorCode.getHttpStatus(), errorCode.getMessage());
//
//        model.addAttribute("error", errorCode.getMessage());
//        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorDto);
//    }
}
