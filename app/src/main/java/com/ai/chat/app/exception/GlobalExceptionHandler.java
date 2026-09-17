package com.ai.chat.app.exception;

import com.ai.chat.app.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleConversationNotFoundException(ConversationNotFoundException conversationNotFoundException, HttpServletRequest request){
        ErrorResponseDto error=new ErrorResponseDto(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(),"CONVERSATION_NOT_FOUND",conversationNotFoundException.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException,HttpServletRequest request){
    FieldError fieldError=methodArgumentNotValidException.getBindingResult().getFieldErrors().get(0);
    String message=fieldError.getField()+" "+fieldError.getDefaultMessage();
    ErrorResponseDto error=new ErrorResponseDto(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),"VALIDATION_ERROR",message,request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequest(HttpMessageNotReadableException httpMessageNotReadableException,HttpServletRequest request){
        String message=httpMessageNotReadableException.getMessage();
        ErrorResponseDto error=new ErrorResponseDto(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),"MALFORMED_JSON",message,request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception) {

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                "/api/chat"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}


