package com.tofurkishrobocracy.changerace.persistance.exception;

public class NotInAClanException extends Exception {

    public NotInAClanException() {
        super();
    }

    public NotInAClanException(String msg) {
        super(msg);
    }

    public NotInAClanException(String msg, Throwable t) {
        super(msg, t);
    }
}
