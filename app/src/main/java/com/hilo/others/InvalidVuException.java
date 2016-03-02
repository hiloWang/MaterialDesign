package com.hilo.others;

/**
 * Created by hilo on 16/3/2.
 */
public class InvalidVuException extends Exception {

    public InvalidVuException() {
    }

    public InvalidVuException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
