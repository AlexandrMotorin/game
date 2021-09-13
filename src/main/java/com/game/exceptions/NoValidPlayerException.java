package com.game.exceptions;

public class NoValidPlayerException extends RuntimeException{
    public NoValidPlayerException(String message) {
        super(message);
    }
}
