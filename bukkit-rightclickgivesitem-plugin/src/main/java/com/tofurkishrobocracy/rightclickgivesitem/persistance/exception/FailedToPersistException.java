package com.tofurkishrobocracy.rightclickgivesitem.persistance.exception;

public class FailedToPersistException extends Exception {

    public FailedToPersistException() {
        super();
    }

    private FailedToPersistException(String msg) {
        super(msg);
    }

    private FailedToPersistException(String msg, Throwable t) {
        super(msg, t);
    }
}
