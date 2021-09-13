package com.game.controller;

import com.game.exceptions.NoSuchPlayerException;
import com.game.exceptions.NoValidPlayerException;
import com.game.exceptions.PlayerIncorrectData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlayerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(NoSuchPlayerException e){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(NoValidPlayerException e){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(e.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

}
