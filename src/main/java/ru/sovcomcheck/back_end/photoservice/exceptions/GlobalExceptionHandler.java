package ru.sovcomcheck.back_end.photoservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ImageUploadException.class})
    public ResponseEntity<Object> handleImageUploadException(ImageUploadException imageUploadException) {

        ExceptionBody imageException = new ExceptionBody(
                imageUploadException.getMessage(),
                imageUploadException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(imageException, imageException.getHttpStatus());

    }

    @ExceptionHandler(value = {ImageDownloadException.class})
    public ResponseEntity<Object> handleImageUploadException(ImageDownloadException imageDownloadException) {

        ExceptionBody imageException = new ExceptionBody(
                imageDownloadException.getMessage(),
                imageDownloadException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(imageException, imageException.getHttpStatus());

    }

}
