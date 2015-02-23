package com.tofurkishrobocracy.changerace.persistance.exception;

public class AlreadyInAClanException extends Exception {

    public AlreadyInAClanException() {
        super();
    }

    public AlreadyInAClanException(String msg) {
        super(msg);
    }

    public AlreadyInAClanException(String msg, Throwable t) {
        super(msg, t);
    }
}
